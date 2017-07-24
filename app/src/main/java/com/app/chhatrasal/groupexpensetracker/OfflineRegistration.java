package com.app.chhatrasal.groupexpensetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class OfflineRegistration extends AppCompatActivity {

    private static int size = 0;
    private static int tripID = 0;
    private EditText tripName;
    private EditText groupSize;
    private Button submit;
    private SQLiteDatabaseHandle databaseHandle = new SQLiteDatabaseHandle(this);

    Random r = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_registration);

        boolean status = databaseHandle.checkCurrentTrip(this);
        if (status) {
            startActivity(new Intent(this, OfflineHome.class));
            finish();
        }

        initialize();

        onButtonClick();
    }


    public static int getSize() {
        return size;
    }

    public static int getTripID() {
        return tripID;
    }

    public static void setTripID(int tripID) {
        OfflineRegistration.tripID = tripID;
    }

    private void initialize() {
        tripName = (EditText) findViewById(R.id.trip_name);
        groupSize = (EditText) findViewById(R.id.group_size);
        submit = (Button) findViewById(R.id.trip_submit_button);

    }

    private void onButtonClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(tripName.getText()) && !TextUtils.isEmpty(groupSize.getText())) {
                    size = Integer.parseInt(groupSize.getText().toString());
                    setTripID(r.nextInt(9999));
                    Intent intent = new Intent(getApplicationContext(), AddMembers.class);
                    intent.putExtra("groupsize", getSize());
                    intent.putExtra("tripname", tripName.getText().toString());
                    intent.putExtra("tripid",r.nextInt(9999));
                    startActivity(intent);
                } else if (TextUtils.isEmpty(tripName.getText()) && !TextUtils.isEmpty(groupSize.getText()))
                    Toast.makeText(OfflineRegistration.this, "Please enter the destination name.", Toast.LENGTH_SHORT).show();
                else if (!TextUtils.isEmpty(tripName.getText()) && TextUtils.isEmpty(groupSize.getText()))
                    Toast.makeText(OfflineRegistration.this, "Please enter the group size.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(OfflineRegistration.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
