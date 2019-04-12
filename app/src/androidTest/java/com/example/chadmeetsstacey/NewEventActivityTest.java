package com.example.chadmeetsstacey;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NewEventActivityTest {
private NewEventActivity mNewEventActivity;
private FirebaseFirestore db;

    @Rule
    public ActivityTestRule<NewEventActivity> mActivityRule = new ActivityTestRule<>(NewEventActivity.class);

    @Test
    public void AttemptToCreateSuccessful() {
        // Write dummy data to every field
        onView(withId(R.id.event_name_text))
                .perform(typeText("Date Party")).perform(closeSoftKeyboard());
        onView(withId(R.id.date_text))
                .perform(typeText("2019-4-11")).perform(closeSoftKeyboard());
        onView(withId(R.id.time_text))
                .perform(typeText("8:00")).perform(closeSoftKeyboard());
        onView(withId(R.id.location_text))
                .perform(typeText("Bulls")).perform(closeSoftKeyboard());
        onView(withId(R.id.description_text))
                .perform(typeText("Fun!")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.submit_button)).perform(click());

        // Assert that new event was created properly
        db.collection("events").document("testEvent").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertTrue(documentSnapshot.exists());
            }
        });

        // Delete event from database to keep consistency
        SystemClock.sleep(5000);
        db.collection("events").document("testEvent").delete();
    }

    @Test
    public void AttemptToCreateMissingName() {
        // Write dummy data to every field
        onView(withId(R.id.event_name_text))
                .perform(typeText("Date Party")).perform(closeSoftKeyboard());
        onView(withId(R.id.date_text))
                .perform(typeText("2019-4-11")).perform(closeSoftKeyboard());
        onView(withId(R.id.time_text))
                .perform(typeText("8:00")).perform(closeSoftKeyboard());
        onView(withId(R.id.location_text))
                .perform(typeText("Bulls")).perform(closeSoftKeyboard());
        onView(withId(R.id.description_text))
                .perform(typeText("Fun!")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.submit_button)).perform(click());

        // Assert that new event was not created properly
        db.collection("events").document("testEvent").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
    }

    @Test
    public void AttemptToCreateMissingDate() {
        // Write dummy data to every field
        onView(withId(R.id.event_name_text))
                .perform(typeText("Date Party")).perform(closeSoftKeyboard());
        onView(withId(R.id.time_text))
                .perform(typeText("8:00")).perform(closeSoftKeyboard());
        onView(withId(R.id.location_text))
                .perform(typeText("Bulls")).perform(closeSoftKeyboard());
        onView(withId(R.id.description_text))
                .perform(typeText("Fun!")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.submit_button)).perform(click());

        // Assert that new event was not created properly
        db.collection("events").document("testEvent").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
    }

    @Test
    public void AttemptToCreateMissingTime() {
        // Write dummy data to every field
        onView(withId(R.id.event_name_text))
                .perform(typeText("Date Party")).perform(closeSoftKeyboard());
        onView(withId(R.id.date_text))
                .perform(typeText("2019-4-11")).perform(closeSoftKeyboard());
        onView(withId(R.id.location_text))
                .perform(typeText("Bulls")).perform(closeSoftKeyboard());
        onView(withId(R.id.description_text))
                .perform(typeText("Fun!")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.submit_button)).perform(click());

        // Assert that new event was not created properly
        db.collection("events").document("testEvent").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
    }

    @Test
    public void AttemptToCreateMissingLocation() {
        // Write dummy data to every field
        onView(withId(R.id.event_name_text))
                .perform(typeText("Date Party")).perform(closeSoftKeyboard());
        onView(withId(R.id.date_text))
                .perform(typeText("2019-4-11")).perform(closeSoftKeyboard());
        onView(withId(R.id.time_text))
                .perform(typeText("8:00")).perform(closeSoftKeyboard());
        onView(withId(R.id.description_text))
                .perform(typeText("Fun!")).perform(closeSoftKeyboard());

        // Simulate register button click
        onView(withId(R.id.submit_button)).perform(click());

        // Assert that new event was not created properly
        db.collection("events").document("testEvent").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
    }

    @Test
    public void AttemptToCreateMissingDescription() {
        // Write dummy data to every field
        onView(withId(R.id.event_name_text))
                .perform(typeText("Date Party")).perform(closeSoftKeyboard());
        onView(withId(R.id.date_text))
                .perform(typeText("2019-4-11")).perform(closeSoftKeyboard());
        onView(withId(R.id.time_text))
                .perform(typeText("8:00")).perform(closeSoftKeyboard());
        onView(withId(R.id.location_text))
                .perform(typeText("Bulls")).perform(closeSoftKeyboard());

        // Simulate register button clicks
        onView(withId(R.id.submit_button)).perform(click());

        // Assert that new event was not created properly
        db.collection("events").document("testEvent").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                assertFalse(documentSnapshot.exists());
            }
        });
    }


    @Before
    public void setUp() throws Exception {
        mNewEventActivity=mActivityRule.getActivity();
        db = FirebaseFirestore.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        mNewEventActivity.finish();

    }
}