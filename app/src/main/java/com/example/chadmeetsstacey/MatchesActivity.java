package com.example.chadmeetsstacey;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MatchesActivity extends AppCompatActivity {

    private static final String TAG = "MatchesActivity";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String currEmail;
    private LinearLayout myEventsLayout;
    private LinearLayout otherEventsLayout;
    private Context context;
    private LinearLayout.LayoutParams layoutparams;

    // Enum used to decide whether to display my events or other events
    private enum EventType {
        MY_EVENTS, OTHER_EVENTS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        // Access a Cloud Firestore instance and storage instance
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get View objects
        myEventsLayout = (LinearLayout) findViewById(R.id.my_events_matches);
        otherEventsLayout = (LinearLayout) findViewById(R.id.other_events_matches);
        context = getApplicationContext();
        layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Get current user's email
        currEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Clear views from layouts
        myEventsLayout.removeAllViews();
        otherEventsLayout.removeAllViews();

        // Load matches for all of user's individual events
        loadMyMatches();

        // Load matches for all other events that user has swiped on
        loadOtherMatches();
    }

    // Loads all of the people who have swiped on current user's events
    public void loadMyMatches() {
        // Find each event for user
        db.collection("events").whereEqualTo("creatingUser", currEmail)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (final QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            // Create Event object
                            UserEvent event = document.toObject(UserEvent.class);
                            displayEventWithMatches(event, EventType.MY_EVENTS, document.getId());
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    // Load matches for all other events that user has swiped on
    public void loadOtherMatches() {
        // Search through all events user has swiped on
        db.collection("users").document(currEmail)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Create user object
                UserInfo currUser = documentSnapshot.toObject(UserInfo.class);

                // Iterate over all events swiped on
                for (final String eventId: currUser.getEventsSwipedOn()) {
                    db.collection("events").document(eventId)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // Create Event object
                            UserEvent event = documentSnapshot.toObject(UserEvent.class);
                            displayEventWithMatches(event, EventType.OTHER_EVENTS, eventId);
                        }
                    });
                }
            }
        });
    }


    // Creates view objects for single event
    public void displayEventWithMatches(UserEvent event, EventType type, String eventId) {
        // Add horizontal layout to overall linear layout
        LinearLayout eventLayout = new LinearLayout(context);
        eventLayout.setLayoutParams(layoutparams);
        eventLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Add TextView for event to horizontal layout
        TextView eventText = new TextView(context);
        eventText.setLayoutParams(layoutparams);
        eventText.setText(event.getEventName());
        eventText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        eventText.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        eventText.setPadding(0, 0, 0, 100);
        eventLayout.addView(eventText);

        if (type == EventType.MY_EVENTS) {
            myEventsLayout.addView(eventLayout);
            // Display all matches for my event
            // Add horizontal scroll view to horizontal layout
            LinearLayout matches = new LinearLayout(context);
            matches.setLayoutParams(layoutparams);
            matches.setOrientation(LinearLayout.HORIZONTAL);
            eventLayout.addView(matches);

            // Add all matches for that event to horizontal scroll view
            for (String otherUser: event.getPotentialMatches()) {
                // Create vertical linear layout to hold name and pic
                LinearLayout matchLayout = new LinearLayout(context);
                matchLayout.setLayoutParams(layoutparams);
                matchLayout.setOrientation(LinearLayout.VERTICAL);
                //TODO: CENTER
                matchLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                matches.addView(matchLayout);
                addMatchToView(otherUser, matchLayout, eventId);
            }

        } else if (type == EventType.OTHER_EVENTS) {
            otherEventsLayout.addView(eventLayout);
            // Display only creating user for other event
            // Create horizontal linear layout to hold name and pic
            LinearLayout matchLayout = new LinearLayout(context);
            matchLayout.setLayoutParams(layoutparams);
            matchLayout.setOrientation(LinearLayout.VERTICAL);
            //TODO: CENTER
            matchLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
            eventLayout.addView(matchLayout);
            addMatchToView(event.getCreatingUser(), matchLayout, eventId);
        }

    }

    // Add a match to the corresponding view and link match icon to message screen
    public void addMatchToView(String otherUser, final LinearLayout linLay, final String eventId) {
        // Get user whose info will be displayed
        db.collection("users").document(otherUser)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final UserInfo user = documentSnapshot.toObject(UserInfo.class);
                final ImageView profilePic = new ImageView(context);
                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(150,150);
                profilePic.setLayoutParams(imageParams);
                // Download user's pic if it exists
                if (user.getHasProfilePic()) {
                    StorageReference picRef = storage.getReference().child("profilePics/" + user.getEmailAddress());
                    picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profilePic);
                        }
                    });
                } else {
                    profilePic.setImageResource(R.drawable.profile_icon);
                }

                // Add user's pic to view
                linLay.setPadding(50, 0, 0, 0);
                linLay.addView(profilePic);

                // Create TextView with user's name
                final TextView userName = new TextView(context);
                userName.setLayoutParams(layoutparams);
                userName.setText(user.getFirstName());
                userName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                userName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

                // Add user's name to view
                linLay.addView(userName);

                // Link profile pic to their respective message screen
                profilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linkToMessage(user.getEmailAddress(), eventId);
                    }
                });
            }
        });
    }

    // Links the profile pic to the corresponding message screen
    public void linkToMessage(String otherEmail, String eventId) {
        final Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("currEmail", currEmail);
        intent.putExtra("otherEmail", otherEmail);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }
}
