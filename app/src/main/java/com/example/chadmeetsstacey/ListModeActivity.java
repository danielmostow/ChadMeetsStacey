package com.example.chadmeetsstacey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListModeActivity extends AppCompatActivity {
    private static final String TAG = "ListModeActivity";
    private LinearLayout cardHolder;
    private int cardId;
    private Context context;
    private LinearLayout.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mode);

        // Set up listener for button
        Button findADateMode = (Button) findViewById(R.id.find_a_date_mode);
        findADateMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFindADateMode();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();
        layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // Load available events
        getPossibleEvents();
    }

    // Takes user to list mode
    public void goToFindADateMode() {
        Intent intent = new Intent(this, FindADateModeActivity.class);
        startActivity(intent);
        finish();
    }

    // Loads all possible events dynamically
    public void getPossibleEvents() {
        cardHolder = (LinearLayout) findViewById(R.id.other_events_wrapper);
        // Avoid duplicating events on addition of new event
        cardHolder.removeAllViews();
        cardId = 0;

        // Access a Cloud Firestore instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*
            Get all events where current user's preferred gender = event creator's gender
         */
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currUserEmail = currUser.getEmail();

        // Get user's preferred gender via settings
        db.collection("settings").document(currUserEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Settings userSettings = documentSnapshot.toObject(Settings.class);
                final int preferredGender = userSettings.getPreferredGender();

                // Get all events in database
                db.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot event : task.getResult()) {
                                Log.d(TAG, event.getId() + " => " + event.getData());
                                // Get user associated with event
                                String eventEmail = (String) event.get("creatingUser");
                                db.collection("users").document(eventEmail).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Log.d(TAG, event.getId());
                                        UserInfo creatingUser = documentSnapshot.toObject(UserInfo.class);
                                        UserEvent userEvent = event.toObject(UserEvent.class);
                                        // Add event if preferred gender matches creating user's gender and user hasn't swiped on event
                                        if (preferredGender == creatingUser.getGender() &&
                                            !userEvent.getDeclinedMatches().contains(currUserEmail) &&
                                            !userEvent.getPotentialMatches().contains(currUserEmail)) {
                                            // Add card to list for event
                                            addEvent(userEvent, creatingUser, event.getId());
                                        }
                                    }
                                });

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });

    }

    // Load one card dynamically
    public void addEvent(final UserEvent event, final UserInfo creatingUser, final String eventId) {
        // Create clickable card
        CardView card = new CardView(context);
        card.setId(cardId);
        cardId++;
        card.setLayoutParams(layoutparams);
        card.setRadius(15);
        card.setUseCompatPadding(true); // Want padding between cards
        card.setPadding(25, 25, 25, 25);
        card.setCardBackgroundColor(Color.WHITE);

        // Create textview with event name, user's name, and event date
        TextView text = new TextView(context);
        text.setText(event.getEventName() + "\n" + creatingUser.getFirstName() + "\n" + event.getDate());
        text.setMaxLines(3);
        text.setWidth(800);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        text.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        text.setPadding(25,25,25,25);
        text.setGravity(Gravity.LEFT);

        // Create linear layout with text and button
        LinearLayout linLay = new LinearLayout(context);
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.addView(text);

        // Add linear layout to card and card to layout
        card.addView(linLay);
        cardHolder.addView(card);

        // Link up card click with event details
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherEventActivity.class);
                // Add required Strings to intent
                intent.putExtra("eventId", eventId);
                intent.putExtra("host", creatingUser.getFirstName());
                intent.putExtra("emailAddress", creatingUser.getEmailAddress());
                intent.putExtra("eventName", event.getEventName());
                intent.putExtra("date", event.getDate());
                intent.putExtra("time", event.getTime());
                intent.putExtra("location", event.getLocation());
                intent.putExtra("description", event.getDescription());
                startActivity(intent);
            }
        });

    }

}
