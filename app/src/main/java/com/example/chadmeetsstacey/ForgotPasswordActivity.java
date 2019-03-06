package com.example.chadmeetsstacey;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firestore.v1.FirestoreGrpc;

import org.w3c.dom.Text;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText mEmailView;
    private Button mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth = FirebaseAuth.getInstance();
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mSubmit = (Button) findViewById(R.id.submit_button);
        mSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = mEmailView.getText().toString().trim();
                auth.sendPasswordResetEmail(email);
                Toast toast = Toast.makeText(getApplicationContext(), "A link to reset your password has been sent to your email", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        });




    }

}
