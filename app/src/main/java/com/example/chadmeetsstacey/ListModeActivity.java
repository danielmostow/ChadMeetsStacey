package com.example.chadmeetsstacey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ListModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mode);

        // Set up listener for button
        Button findADateMode = (Button) findViewById(R.id.find_a_date_mode);
        findADateMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFindADateMode();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Load available events
        getPossibleEvents();
    }

    // Takes user to list mode
    public void goToFindADateMode() {
        Intent intent = new Intent(this, FindADateModeActivity.class);
        startActivity(intent);
        finish();
    }

    // Loads all possible events dynamically
    public void getPossibleEvents() {


    }

}
