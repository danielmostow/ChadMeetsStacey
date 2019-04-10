package com.example.chadmeetsstacey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FindADateModeActivity extends AppCompatActivity {
    private static final String TAG = "FindADateModeActivity";
    private LinearLayout cardHolder;
    private Context context;
    private LayoutParams layoutparams;
    private int prevCardViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_adate_mode);
        Log.d(TAG, "onCreate() method called!");

        // Set up listeners for buttons
        Button createEventButton = (Button) findViewById(R.id.create_event);
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

        Button listModeButton = (Button) findViewById(R.id.list_mode);
        listModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListMode();
            }
        });

        Button profileButton = (Button) findViewById(R.id.profile_icon);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile();
            }
        });

        Button messagesButton = (Button) findViewById(R.id.messages_icon);
        messagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMatches();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() method called!");

        // Add all of user's events to card view
        context = getApplicationContext();
        layoutparams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        GetUserEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() method called!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() method called!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() method called!");
    }

    // Takes user to create event screen
    public void createEvent() {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    // Takes user to list mode
    public void goToListMode() {
        Intent intent = new Intent(this, ListModeActivity.class);
        startActivity(intent);
    }

    // Takes user to their profile
    public void goToProfile() {
        Intent intent = new Intent(this, MyProfileActivity.class);
        startActivity(intent);
    }

    // Takes user to their matches
    public void goToMatches() {
        Intent intent = new Intent(this, MatchesActivity.class);
        startActivity(intent);
    }

    // Loads all of user's events dynamically
    public void GetUserEvents() {
        cardHolder = (LinearLayout) findViewById(R.id.my_events_wrapper);
        // Avoid duplicating events on addition of new event
        cardHolder.removeAllViews();
        prevCardViewId = 0;

        // Access a Cloud Firestore instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get all of user's events
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("events").whereEqualTo("creatingUser", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        // Create card with text view and edit button for each of user's events
                        UserEvent event = document.toObject(UserEvent.class);
                        addOneEvent(event.getEventName(), document.getId(), event.getPotentialMatches().size());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

    // Creates card for one event and adds it to layout
    private void addOneEvent(String eventName, final String eventID, int numMatches) {
        // Create card with text view and edit button for each of user's events
        CardView card = new CardView(context);
        int curCardViewId = prevCardViewId + 1;
        card.setId(curCardViewId);
        card.setLayoutParams(layoutparams);
        card.setRadius(15);
        card.setUseCompatPadding(true); // Want padding between cards
        card.setPadding(25, 25, 25, 25);
        card.setCardBackgroundColor(Color.WHITE);

        // Create text to go in card
        TextView text = new TextView(context);
        int numMatchObjects = numMatches;
        text.setText(eventName + "\n"+ numMatchObjects + " matches");
        text.setMaxLines(2);
        text.setLayoutParams(layoutparams);
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        text.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        text.setPadding(25,25,25,25);
        text.setGravity(Gravity.CENTER);

        // Create edit button to go in card
        ImageView editButton = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(150,150);
        editButton.setLayoutParams(imageParams);
        editButton.setImageResource(R.drawable.edit_icon);
        editButton.setClickable(true);

        // Pass event ID into edit activity when edit button clicked
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditEventActivity.class);
                intent.putExtra("CurrentEvent", eventID);
                startActivity(intent);
            }
        });

        // Create linear layout with text and button
        LinearLayout linLay = new LinearLayout(context);
        linLay.setOrientation(LinearLayout.HORIZONTAL);
        linLay.addView(text);
        linLay.addView(editButton);
        card.addView(linLay);

        // Add card to layout
        cardHolder.addView(card);
        prevCardViewId = curCardViewId;
    }

}
