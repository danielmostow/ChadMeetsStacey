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

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = "NewEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() method called!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Grab submit button and set up on click listener
        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO: Add field verification
                //TODO: Convert to date and time pickers

                // Grab data from each text field
                EditText eventName = (EditText) findViewById(R.id.event_name_text);
                EditText date = (EditText) findViewById(R.id.date_text);
                EditText time = (EditText) findViewById(R.id.time_text);
                EditText location = (EditText) findViewById(R.id.location_text);
                EditText description = (EditText) findViewById(R.id.description_text);

                // Get user information for log
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, user.getEmail());

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
        });
    }

}
