package com.example.chadmeetsstacey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class FindADateModeActivity extends AppCompatActivity {
    private static final String TAG = "FindADateModeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_adate_mode);
        Log.d(TAG, "onCreate() method called!");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() method called!");
    }

    public void createEvent(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
    }

}
