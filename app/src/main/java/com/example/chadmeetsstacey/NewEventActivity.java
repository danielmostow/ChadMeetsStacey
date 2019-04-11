package com.example.chadmeetsstacey;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
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
    private String currUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private final String filename = currUser + "myEvents.txt";
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
        date.setInputType(InputType.TYPE_NULL);
        time = (EditText) findViewById(R.id.time_text);
        time.setInputType(InputType.TYPE_NULL);
        location = (EditText) findViewById(R.id.location_text);
        description = (EditText) findViewById(R.id.description_text);

        // Set up date picker
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDatePicker();
            }
        });

        // Set up time picker
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTimePicker();
            }
        });

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
        // If not all fields are valid, don't let user create event
        if (!checkValidation()) {
            Toast.makeText(NewEventActivity.this, "Cannot create event with blank fields!", Toast.LENGTH_LONG).show();
            return;
        }

        // Create new event and add to event collection
        final UserEvent event = new UserEvent(eventName.getText().toString(),
                date.getText().toString(), time.getText().toString(),
                description.getText().toString(), location.getText().toString());

        db.collection("events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        // Add event to cache
                        try {
                            File myEventsCache = new File(getApplicationContext().getCacheDir(), filename);
                            BufferedWriter writer = new BufferedWriter(new FileWriter(myEventsCache.getAbsoluteFile(), true));
                            writer.write(documentReference.getId());
                            writer.newLine();
                            writer.write(event.getEventName());
                            writer.newLine();
                            writer.write(event.getPotentialMatches().size() + "");
                            writer.newLine();
                            writer.close();
                        } catch (Exception e) {
                            Log.d(TAG, "Cache not available!");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // DELAY SO DATA CAN BE WRITTEN TO CACHE
                finish();
                Toast.makeText(getApplicationContext(), eventName.getText().toString() + " created!", Toast.LENGTH_LONG).show();
            }
        }, 500);

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

    // Sets up date picker to current date and sets callback function for updated date
    private void setUpDatePicker() {
        // Get current date
        final Calendar c = Calendar.getInstance();
        final int calYear = c.get(Calendar.YEAR);
        final int calMonth = c.get(Calendar.MONTH);
        final int calDay = c.get(Calendar.DAY_OF_MONTH);

        // Use calendar date, unless date has been set
        int year = calYear;
        int month = calMonth;
        int day = calDay;
        if (date.getText().toString().length() != 0) {
            // If a date is set, use that to start from
            String dateString = date.getText().toString();
            int firstDash = dateString.indexOf('-');
            int secondDash =dateString.indexOf('-', firstDash+1);
            year = Integer.parseInt(dateString.substring(0,firstDash));
            month = Integer.parseInt(dateString.substring(firstDash+1, secondDash))-1;
            day = Integer.parseInt(dateString.substring(secondDash+1));
        }


        // Create and show date picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Prevent past date from being set
                        if ((year > calYear) ||
                            (year == calYear && monthOfYear > calMonth) ||
                            (year == calYear && monthOfYear == calMonth && dayOfMonth >= calDay)) {
                            date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } else {
                            Toast.makeText(getApplicationContext(), "Can't set event date in the past!", Toast.LENGTH_LONG).show();

                        }

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Sets up time picker to current time and sets callback function for updated time
    private void setUpTimePicker() {
        // Get current time
        final Calendar c = Calendar.getInstance();
        final int calHour = c.get(Calendar.HOUR_OF_DAY);
        final int calMinute = c.get(Calendar.MINUTE);

        // Use calendar time, unless time has been set
        int hour = calHour;
        int minute = calMinute;
        if (time.getText().toString().length() != 0) {
            // If a time is set, use that to start from
            String timeString = time.getText().toString();
            int colon = timeString.indexOf(':');
            hour = Integer.parseInt(timeString.substring(0,colon));
            minute = Integer.parseInt(timeString.substring(colon+1));
        }


        // Create and show time picker
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (minute <= 9) {
                            time.setText(hourOfDay + ":0" + minute);
                        } else {
                            time.setText(hourOfDay + ":" + minute);
                        }
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

}
