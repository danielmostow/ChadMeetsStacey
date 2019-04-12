package com.example.chadmeetsstacey;

import org.junit.After;
import org.junit.Before;

import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
private String emailWorks = "dumbelite100@gmail.com";
private String passWorks = "QWERTY1234";
private String emailFail1 = "dmbelite100@gmail.com";
private String passFail1 = "qwerty1234";
private LoginActivity mLoginActivity;
private FirebaseAuth auth;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void Login() {
        auth.signInWithEmailAndPassword(emailWorks, passWorks)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail)
                                    {
                                       auth.signOut();
                                    }
                                    // if user enters wrong password.

                                    catch (FirebaseAuthInvalidCredentialsException e)
                                    {
                                        auth.signOut();
                                    }
                                    catch (Exception e)
                                    {
                                        auth.signOut();
                                    }
                                }
                            }
                        }

                );
        SystemClock.sleep(7000);
        assertNotNull(auth.getCurrentUser());
    }

    @Test
    public void LoginFailEmail() {
        auth.signInWithEmailAndPassword(emailFail1, passWorks)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail)
                                    {
                                        auth.signOut();
                                    }
                                    // if user enters wrong password.

                                    catch (FirebaseAuthInvalidCredentialsException e)
                                    {
                                        auth.signOut();
                                    }
                                    catch (Exception e)
                                    {
                                        auth.signOut();
                                    }
                                }
                            }
                        }

                );
        SystemClock.sleep(7000);
        assertNull(auth.getCurrentUser());
    }

    @Test
    public void LoginFailPass() {
        auth.signInWithEmailAndPassword(emailWorks, passFail1)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail)
                                    {
                                        auth.signOut();
                                    }
                                    // if user enters wrong password.

                                    catch (FirebaseAuthInvalidCredentialsException e)
                                    {
                                        auth.signOut();
                                    }
                                    catch (Exception e)
                                    {
                                        auth.signOut();
                                    }
                                }
                            }
                        }

                );
        SystemClock.sleep(7000);
        assertNull(auth.getCurrentUser());
    }


    @Before
    public void setUp() throws Exception {
        mLoginActivity=mActivityRule.getActivity();
        auth = FirebaseAuth.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        mLoginActivity.finish();

    }
}