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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG = "NewUserActivity";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Access a Cloud Firestore instance from your Activity
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Grab submit button and set up on click listener
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Add field verification
                //TODO: Convert to age, grade, and greek org pickers

                // Grab data from each text field
                final EditText email = (EditText) findViewById(R.id.email_text);
                final EditText password = (EditText) findViewById(R.id.password_text);
                final EditText confirmPassword = (EditText) findViewById(R.id.confirm_password_text);
                final EditText firstName = (EditText) findViewById(R.id.first_name_text);
                final EditText age = (EditText) findViewById(R.id.age_text);
                final EditText grade = (EditText) findViewById(R.id.grade_text);
                final EditText greekOrg = (EditText) findViewById(R.id.greek_org_text);

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
                                                Integer.parseInt(age.getText().toString()), grade.getText().toString());
                                        db.collection("users")
                                                .add(user)
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
        });
    }

}
