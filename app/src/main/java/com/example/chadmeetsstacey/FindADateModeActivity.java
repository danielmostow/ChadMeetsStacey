package com.example.chadmeetsstacey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_adate_mode);
        Log.d(TAG, "onCreate() method called!");
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

    // Takes user to create event screen
    public void createEvent(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

    // Loads all of user's events dynamically
    public void GetUserEvents() {
        cardHolder = (LinearLayout) findViewById(R.id.my_events_wrapper);
        // Avoid duplicating events on addition of new event
        cardHolder.removeAllViews();

        // Access a Cloud Firestore instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get all of user's events
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("events").whereEqualTo("creatingUser", user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    int prevCardViewId = 0;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        // Create card with text view for each of user's events
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

                        int numMatchObjects = ((List<String>) document.get("potentialMatches")).size();
                        text.setText(document.get("eventName") + "\n"+ numMatchObjects + " matches");
                        text.setMaxLines(2);
                        text.setLayoutParams(layoutparams);
                        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                        text.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                        text.setPadding(25,25,25,25);
                        text.setGravity(Gravity.CENTER);

                        // Add text to card and card to layout
                        card.addView(text);
                        cardHolder.addView(card);
                        prevCardViewId = curCardViewId;
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

}
