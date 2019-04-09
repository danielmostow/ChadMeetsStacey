package com.example.chadmeetsstacey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = "NewEventActivity";
    private FirebaseFirestore db;
    private EditText eventName;
    private EditText date;
    private EditText time;
    private EditText location;
    private EditText description;
    private List<EditText> allFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() method called!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // Get user information for log
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, user.getEmail());

        // Set up each text field
        eventName = (EditText) findViewById(R.id.event_name_text);
        date = (EditText) findViewById(R.id.date_text);
        time = (EditText) findViewById(R.id.time_text);
        location = (EditText) findViewById(R.id.location_text);
        description = (EditText) findViewById(R.id.description_text);

        // Add all fields to list
        allFields = new ArrayList<EditText>();
        allFields.add(eventName);
        allFields.add(date);
        allFields.add(time);
        allFields.add(location);
        allFields.add(description);

        // Grab submit button and set up on click listener
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                attemptToCreateEvent();
            }
        });
    }

    // Attempts to create a new event
    private void attemptToCreateEvent() {
        //TODO: Convert to date and time pickers
        // If not all fields are valid, don't let user create event
        if (!checkValidation()) {
            Toast.makeText(NewEventActivity.this, "Cannot create event with blank fields!", Toast.LENGTH_LONG).show();
            return;
        }

        // Create new event and add to event collection
        UserEvent event = new UserEvent(eventName.getText().toString(),
                date.getText().toString(), time.getText().toString(),
                description.getText().toString(), location.getText().toString());

        db.collection("events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        finish();
        Toast.makeText(getApplicationContext(), eventName.getText().toString() + " created!", Toast.LENGTH_LONG).show();
    }

    // Returns whether or not data has been entered in each field
    private boolean checkValidation() {
        boolean allValidated = true;

        // Cycle through all EditText fields
        for (EditText currField: allFields) {
            // If any field hasn't been filled, want to return false
            if (currField.getText().toString().equals("")) {
                allValidated = false;
                // Display red warning next to field
                currField.setError("Field cannot be blank!");
            }
        }

        return allValidated;
    }

}
