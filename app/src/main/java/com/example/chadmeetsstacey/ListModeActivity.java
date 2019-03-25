package com.example.chadmeetsstacey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListModeActivity extends AppCompatActivity {
    private static final String TAG = "ListModeActivity";
    private LinearLayout cardHolder;
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

        // Access a Cloud Firestore instance
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*
            Get all events where current user's preferred gender = event creator's gender
         */
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        String currUserEmail = currUser.getEmail();

        // Get user's preferred gender via settings


    }

}
