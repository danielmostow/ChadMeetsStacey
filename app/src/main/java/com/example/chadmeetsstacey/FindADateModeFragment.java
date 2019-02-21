package com.example.chadmeetsstacey;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class FindADateModeFragment extends Fragment {
    private static final String TAG = "FindADateModeFragment";
    public FindADateModeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() method called!");
        return inflater.inflate(R.layout.fragment_find_adate_mode, container, false);

    }
}
