package com.example.chadmeetsstacey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG = "NewUserActivity";
    // Needs to be global, so selection can be passed into submitButton listener
    private int genderSelection;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText firstName;
    private EditText age;
    private EditText grade;
    private EditText greekOrg;
    private List<EditText> allFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Set up text fields
        email = (EditText) findViewById(R.id.email_text);
        password = (EditText) findViewById(R.id.password_text);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_text);
        firstName = (EditText) findViewById(R.id.first_name_text);
        age = (EditText) findViewById(R.id.age_text);
        grade = (EditText) findViewById(R.id.grade_text);
        greekOrg = (EditText) findViewById(R.id.greek_org_text);

        // Add all fields to list
        allFields = new ArrayList<EditText>();
        allFields.add(email);
        allFields.add(password);
        allFields.add(confirmPassword);
        allFields.add(firstName);
        allFields.add(age);
        allFields.add(grade);
        allFields.add(greekOrg);


        // Set up gender spinner
        final Spinner genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Setup listener to get data from gender spinner
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Gender selection will be 0 for male, 1 for female, 2 for other
                genderSelection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO: ?
            }
        });

        // Grab submit button and set up on click listener
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptToRegister();
            }
        });
    }

    // Attempts to register a new user
    private void attemptToRegister() {
        // If not all fields are valid, don't let user register
        if (!checkValidation()) {
            Toast.makeText(NewUserActivity.this, "Cannot register user with blank fields!", Toast.LENGTH_LONG).show();
            return;
        }
        //TODO: Convert to age, grade, and greek org spinners
        // TODO: save information if user hits back button
        // Ensure password = password confirmation
        //TODO: Check for valid password
        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            auth.createUserWithEmailAndPassword(
                    email.getText().toString().trim(),password.getText().toString().trim())
                    .addOnCompleteListener(NewUserActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewUserActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                auth.getCurrentUser().sendEmailVerification();

                                // Create document in User collection for new user
                                UserInfo user = new UserInfo(email.getText().toString(),
                                        firstName.getText().toString(), greekOrg.getText().toString(),
                                        Integer.parseInt(age.getText().toString()), genderSelection, grade.getText().toString());
                                db.collection("users")
                                        .document(email.getText().toString()).set(user);

                                // Create document in Settings collection for new user
                                // Set default preferred gender
                                // TODO: Handle other gender
                                int preferredGender = 1;
                                if (genderSelection == 1) {
                                    preferredGender = 0;
                                }
                                Settings settings = new Settings(email.getText().toString(), preferredGender);
                                db.collection("settings").document(email.getText().toString()).set(settings);
                                finish();
                            } else {
                                Log.d(TAG, "User has already been created previously");
                                Toast.makeText(NewUserActivity.this, "User has already been created previously!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(NewUserActivity.this, "Passwords do not match!", Toast.LENGTH_LONG).show();
        }
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
