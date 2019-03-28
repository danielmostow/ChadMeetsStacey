package com.example.chadmeetsstacey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class MyProfileActivity extends AppCompatActivity {

    private static final String TAG = "MyProfileActivity";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ImageView profilePic;
    private TextView profileInfo;
    private TextView bio;
    private Button profileButton;
    private Button homeButton;
    private Button editButton;
    private int state;
    private int REQUEST_IMAGE_CAPTURE = 1;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // Access a Cloud Firestore instance and storage instance
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Get current user's email
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Set state equal to 0
        // 0 = display
        // 1 = edit
        state = 0;

        // Grab widgets
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        profileInfo = (TextView) findViewById(R.id.profile_info);
        bio = (TextView) findViewById(R.id.bio);
        profileButton = (Button) findViewById(R.id.profile_button);
        homeButton = (Button) findViewById(R.id.home_icon);
        editButton = (Button) findViewById(R.id.edit_profile_icon);

        // Load profile pic, bio, and profile info
        loadProfileDetails();

        // Setup button listeners
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFindADateMode();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: edit profile
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: allow for edit or settings
                goToSettings();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Only let user add picture if they don't already have one
                //if (!currUser.getProfilePicReference().equals("")) {
                    addProfilePic();
                //}
            }
        });

    }

    // Loads profile pic, bio, and info
    public void loadProfileDetails() {
        // Create User object
        db.collection("users").document(userEmail)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserInfo currUser = documentSnapshot.toObject(UserInfo.class);
                // Set profile info to name, greek org, grade
                profileInfo.setText(currUser.getFirstName() + ", " + currUser.getGreekOrg()
                        + "\n" + currUser.getGrade());

                // Set bio if it exists
                if (!currUser.getBiography().equals("")) {
                    bio.setTextSize(24);
                    bio.setText(currUser.getBiography());
                }
                // Set profile pic if it exists
                if (currUser.getHasProfilePic()) {
                    StorageReference picRef = storage.getReference().child("profilePics/" + currUser.getEmailAddress());
                    picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profilePic);
                        }
                    });
                }
            }
        });
    }

    // Takes user to find a date mode
    public void goToFindADateMode() {
        Intent intent = new Intent(this, FindADateModeActivity.class);
        startActivity(intent);
        finish();
    }

    // Takes user to settings page
    public void goToSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    // Lets user upload a picture from their phone to their profile
    public void addProfilePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Store image taken in profile pic
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profilePic.setImageBitmap(imageBitmap);

            // Upload pic to Firebase in folder with filename equal to email
            StorageReference picRef = storage.getReference().child("profilePics/" + userEmail);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] picBytes = baos.toByteArray();
            UploadTask uploadTask = picRef.putBytes(picBytes);

            // Set hasProfilePic = true
            db.collection("users").document(userEmail).update("hasProfilePic", true);

        }
    }

}
