package com.app.chhatrasal.groupexpensetracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class TripHistory extends AppCompatActivity {

    private RecyclerView tripHistoryRecycler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        
        initialize();
    }

    private void initialize() {

    }
}
