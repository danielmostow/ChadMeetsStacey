package com.example.chadmeetsstacey;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EditEventActivity extends AppCompatActivity {

    private static final String TAG = "EditEventActivity";
    private String eventID;
    private FirebaseFirestore db;
    private EditText eventName;
    private EditText date;
    private EditText time;
    private EditText location;
    private EditText description;
    private List<EditText> allFields;
    private Button submitButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        // Get text fields and buttons
        eventName = (EditText) findViewById(R.id.event_name_text);
        date = (EditText) findViewById(R.id.date_text);
        time = (EditText) findViewById(R.id.time_text);
        location = (EditText) findViewById(R.id.location_text);
        description = (EditText) findViewById(R.id.description_text);
        submitButton = (Button) findViewById(R.id.submit_button);
        deleteButton = (Button) findViewById(R.id.delete_button);

        // Add all fields to list
        allFields = new ArrayList<EditText>();
        allFields.add(eventName);
        allFields.add(date);
        allFields.add(time);
        allFields.add(location);
        allFields.add(description);

        // Get event ID
        Intent intent = getIntent();
        eventID = intent.getStringExtra("CurrentEvent");

        // Load event data
        db.collection("events").document(eventID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    // Load data into each text field
                    eventName.setText((String)document.get("eventName"));
                    date.setText((String)document.get("date"));
                    time.setText((String)document.get("time"));
                    location.setText((String)document.get("location"));
                    description.setText((String)document.get("description"));
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // Set submit onclick listener...should end activity
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToEditEvent();
            }
        });

        // Set delete onclick listener...should end activity
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToDeleteEvent();
            }
        });

    }

    // Attempts to edit event
    private void attemptToEditEvent() {
        // If not all fields are valid, don't let user create event
        if (!checkValidation()) {
            Toast.makeText(EditEventActivity.this, "Cannot edit event with blank fields!", Toast.LENGTH_LONG).show();
            return;
        }

        // Create updated user event
        UserEvent event = new UserEvent(eventName.getText().toString(),
                date.getText().toString(), time.getText().toString(),
                description.getText().toString(), location.getText().toString());

        // Update event in database
        db.collection("events").document(eventID)
                .set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        finish();
                        Toast.makeText(getApplicationContext(), eventName.getText().toString() + " updated!", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        finish();
        Toast.makeText(getApplicationContext(), eventName.getText().toString() + " updated!", Toast.LENGTH_LONG).show();

    }

    // Attempts to delete event
    private void attemptToDeleteEvent() {
        // TODO: Delete matches on delete
        new AlertDialog.Builder(EditEventActivity.this)
                .setTitle("Delete event")
                .setMessage("Are you sure you want to delete this event?")

                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete event from database
                        db.collection("events").document(eventID)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        finish();
                                        Toast.makeText(getApplicationContext(), eventName.getText().toString() + " deleted!", Toast.LENGTH_LONG).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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
