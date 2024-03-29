package com.example.chadmeetsstacey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class OtherEventActivity extends AppCompatActivity {

    private static final String TAG = "EditEventActivity";
    private TextView eventName;
    private TextView host;
    private TextView date;
    private TextView time;
    private TextView location;
    private TextView description;
    private ImageView userProfile;
    private Button acceptButton;
    private Button declineButton;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_other);

        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get text fields and buttons
        eventName = (TextView) findViewById(R.id.other_event_name);
        host = (TextView) findViewById(R.id.other_event_host);
        date = (TextView) findViewById(R.id.other_event_date);
        time = (TextView) findViewById(R.id.other_event_time);
        location = (TextView) findViewById(R.id.other_event_location);
        description = (TextView) findViewById(R.id.other_event_description);
        acceptButton = (Button) findViewById(R.id.accept);
        declineButton = (Button) findViewById(R.id.decline);
        userProfile = (ImageView) findViewById(R.id.user_profile);

        // Load data into each text field
        Intent intent = getIntent();
        eventName.setText(intent.getStringExtra("eventName"));
        host.setText("Host: " + intent.getStringExtra("host"));
        date.setText("Date: " + intent.getStringExtra("date"));
        time.setText("Time: " +intent.getStringExtra("time"));
        location.setText("Location: " + intent.getStringExtra("location"));
        description.setText("Description: " + intent.getStringExtra("description"));

        // Load user picture
        //TODO: link to their profile
        String hostEmail = intent.getStringExtra("emailAddress");
        db.collection("users").document(hostEmail)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserInfo hostUser = documentSnapshot.toObject(UserInfo.class);
                        if (hostUser.getHasProfilePic()) {
                            StorageReference picRef = storage.getReference().child("profilePics/" + hostUser.getEmailAddress());
                            picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(userProfile);
                                }
                            });
                        }

                    }
                });

        // Set up listeners for buttons
        final String eventId = intent.getStringExtra("eventId");
        final String currUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("events").document(eventId)
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Add current user to event's list of potential matches
                        UserEvent event = documentSnapshot.toObject(UserEvent.class);
                        event.addPotentialMatch(currUserEmail);
                        db.collection("events").document(eventId)
                                .set(event)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "User added to potential matches!");

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                        // Create match
                        String hostEmail = event.getCreatingUser();
                        String eventName = event.getEventName();
                        Match match = new Match(hostEmail, currUserEmail,
                                                eventName, eventId);
                        // Add match to database with key of host + current + eventId
                        db.collection("matches").document(hostEmail+currUserEmail+eventId)
                                .set(match).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Match created!");

                                }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                        // Add to user's events swiped on
                        db.collection("users").document(currUserEmail)
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                UserInfo user = documentSnapshot.toObject(UserInfo.class);
                                user.addEventSwipedOn(eventId);
                                db.collection("users").document(currUserEmail)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "User added to potential matches!");

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });

                            }
                        });

                        finish();
                    }
                });
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("events").document(eventId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Add current user to event's list of declined matches
                        UserEvent event = documentSnapshot.toObject(UserEvent.class);
                        event.addDeclinedMatch(currUserEmail);
                        db.collection("events").document(eventId)
                                .set(event)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "User added to declined matches!");

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });
                        finish();
                    }
                });
            }
        });


    }

}
