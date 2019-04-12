package com.example.chadmeetsstacey;

import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;


@RunWith(AndroidJUnit4.class)
public class NewUserActivityTest {
private String emailWorks = "dumbelite100@gmail.com";
private String passWorks = "QWERTY1234";
private String emailFail1 = "dmbelite100@gmail.com";
private String passFail1 = "qwerty1234";
private NewUserActivity mNewUserActivity;
private FirebaseFirestore db;

    @Rule
    public ActivityTestRule<NewUserActivity> mActivityRule = new ActivityTestRule<>(NewUserActivity.class);

    @Test
    public void AttemptToRegisterSuccessful() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertTrue(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertTrue(documentSnapshot.exists());
            }
        });

        // Delete user from database to keep consistency
        SystemClock.sleep(5000);
        db.collection("users").document("abc@osu.edu").delete();
        db.collection("settings").document("abc@osu.edu").delete();
    }

    @Test
    public void AttemptToRegisterEmptyEmail() {
        // Write dummy data to every field
        onView(withId(R.id.password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterEmptyPassword() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterEmptyConfirmPassword() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterEmptyName() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterEmptyAge() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterEmptyGrade() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterEmptyGreekOrg() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterNonMatchingPasswords() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcde1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterTooShortPasswords() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abc1")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abc1")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterMissingUppercasePasswords() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("abcd1234")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Test
    public void AttemptToRegisterMissingDigitPasswords() {
        // Write dummy data to every field
        onView(withId(R.id.email_text))
                .perform(typeText("abc@osu.edu")).perform(closeSoftKeyboard());
        onView(withId(R.id.password_text))
                .perform(typeText("Abcdefgh")).perform(closeSoftKeyboard());
        onView(withId(R.id.confirm_password_text))
                .perform(typeText("Abcdefgh")).perform(closeSoftKeyboard());
        onView(withId(R.id.first_name_text))
                .perform(typeText("Jake")).perform(closeSoftKeyboard());
        onView(withId(R.id.age_text))
                .perform(typeText("21")).perform(closeSoftKeyboard());
        onView(withId(R.id.grade_text))
                .perform(typeText("Junior")).perform(closeSoftKeyboard());
        onView(withId(R.id.greek_org_text)).perform(scrollTo());
        onView(withId(R.id.greek_org_text))
                .perform(typeText("ZBT")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.register_button)).perform(click());

        // Assert that new user was not created properly
        db.collection("users").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
        db.collection("settings").document("abc@osu.edu").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });

    }

    @Before
    public void setUp() throws Exception {
        mNewUserActivity=mActivityRule.getActivity();
        db = FirebaseFirestore.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        mNewUserActivity.finish();

    }
}