package com.example.chadmeetsstacey;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView matchProfile;
    private TextView matchName;
    private LinearLayout messagesWrapper;
    private EditText newMessageBody;
    private Button sendMessageButton;
    private Context context;
    private String otherEmail;
    private String userEmail;
    private String hostUser;
    private String nonHost;
    private String eventId;
    private DocumentReference matchDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Access a Cloud Firestore instance and storage instance
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get View objects
        matchProfile = (ImageView) findViewById(R.id.match_profile);
        matchName = (TextView) findViewById(R.id.match_name);
        messagesWrapper = (LinearLayout) findViewById(R.id.messages_wrapper);
        newMessageBody = (EditText) findViewById(R.id.new_message_body);
        sendMessageButton = (Button) findViewById(R.id.send_message);
        context = getApplicationContext();

        // Get intent extras
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("currEmail");
        otherEmail = intent.getStringExtra("otherEmail");
        eventId = intent.getStringExtra("eventId");
        // Set host and non-host user
        db.collection("events").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserEvent event = documentSnapshot.toObject(UserEvent.class);
                hostUser = event.getCreatingUser();
                nonHost = userEmail;
                if (hostUser.equals(userEmail)) {
                    nonHost = otherEmail;
                }
                matchDocument = db.collection("matches").document(hostUser+nonHost+eventId);

                // NOTE: THESE STATEMENTS MUST ALL BE IN HERE BECAUSE OF FIREBASE ASYNC TASKS
                // Load match pic and name
                loadMatchInfo();

                // Set up listener for message submissions
                sendMessageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        attemptToSendMessage();
                    }
                });

                // Set up listener for match updates (new message sent)
                matchDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        loadMessages();
                    }
                });
            }
        });

    }

    // Load match pic and name
    public void loadMatchInfo() {
        //TODO: Link to their profile
        db.collection("users").document(otherEmail)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Load user photo
                UserInfo hostUser = documentSnapshot.toObject(UserInfo.class);
                if (hostUser.getHasProfilePic()) {
                    StorageReference picRef = storage.getReference().child("profilePics/" + otherEmail);
                    picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(matchProfile);
                        }
                    });
                }

                // Load user name
                matchName.setText(hostUser.getFirstName());
            }
        });
    }

    // Load all messages onto screen
    public void loadMessages() {
        // Clear existing messages
        messagesWrapper.removeAllViews();

        // Get match
        matchDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Match match = documentSnapshot.toObject(Match.class);
                // Get messages list associated with match
                List<Message> messages = match.getMessages();

                // Add each message to screen
                for (Message m: messages) {
                    addMessage(m);
                }
            }
        });
    }

    // Adds an individual message to screen
    public void addMessage(Message message) {
        // Create layout params
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutparams.setMargins(20, 0, 20, 40);

        // Create TextView to put message in
        TextView messageText = new TextView(context);
        messageText.setText(message.getMessageBody());
        messageText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        messageText.setPadding(20, 20, 20, 20);

        // Check who message was sent by and act accordingly
        if (message.getSenderEmail().equals(userEmail)) {
            // Current user sent message
            messageText.setTextColor(ContextCompat.getColor(context, R.color.myMessageText));
            messageText.setBackgroundColor(ContextCompat.getColor(context, R.color.myMessageBackground));
            layoutparams.gravity = Gravity.END;
        } else {
            // Other user sent message
            messageText.setTextColor(ContextCompat.getColor(context, R.color.otherMessageText));
            messageText.setBackgroundColor(ContextCompat.getColor(context, R.color.otherMessageBackground));
            layoutparams.gravity = Gravity.START;
        }
        messageText.setLayoutParams(layoutparams);
        messagesWrapper.addView(messageText);
    }

    // Sends message if body is not empty
    public void attemptToSendMessage() {
        // Create new message and add to list if text isn't empty
        if (newMessageBody.getText().length() != 0) {
            // Get text from message body
            String messageText = newMessageBody.getText().toString();

            // Create new message object
            final Message message = new Message(messageText, userEmail);

            // Add message to list in Match
            matchDocument.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Match match = documentSnapshot.toObject(Match.class);
                    match.addMessage(message);
                    // Write back new match with added message
                    matchDocument.set(match);
                }
            });

            // Clear message body
            newMessageBody.setText("");
        } else {
            // Let user know message is blank
            Toast.makeText(context, "Message body can't be blank!", Toast.LENGTH_LONG).show();
        }
    }

}
