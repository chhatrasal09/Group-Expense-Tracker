package com.app.chhatrasal.groupexpensetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineRegistration extends AppCompatActivity {

    private EditText tripName;
    private EditText groupSize;
    private Button submit;
    private static int size = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkDatabase();

        setContentView(R.layout.activity_online_registration);

        initialize();

        onButtonClick();
    }

    private void checkDatabase() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Trip");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(StartOrViewTrip.getFirebaseUser().getUid())) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().orderByChild("tripStatus").equalTo("active").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                startActivity(new Intent(getApplicationContext(), OnlineHome.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(StartOrViewTrip.getFirebaseUser().getUid())) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().orderByChild("tripStatus").equalTo("active").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                startActivity(new Intent(getApplicationContext(), OnlineHome.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().equals(StartOrViewTrip.getFirebaseUser().getUid())) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().orderByChild("tripStatus").equalTo("active").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                startActivity(new Intent(getApplicationContext(), OnlineHome.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(StartOrViewTrip.getFirebaseUser().getUid())) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ds.getRef().orderByChild("tripStatus").equalTo("active").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                startActivity(new Intent(getApplicationContext(), OnlineHome.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        OnlineRegistration.size = size;
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
                    Intent intent = new Intent(getApplicationContext(), AddMembers.class);
                    intent.putExtra("tripname", tripName.getText().toString());
                    intent.putExtra("groupsize", getSize());
                    startActivity(intent);
                    finish();
                } else if (TextUtils.isEmpty(tripName.getText()) && !TextUtils.isEmpty(groupSize.getText()))
                    Toast.makeText(OnlineRegistration.this, "Please enter the destination name.", Toast.LENGTH_SHORT).show();
                else if (!TextUtils.isEmpty(tripName.getText()) && TextUtils.isEmpty(groupSize.getText()))
                    Toast.makeText(OnlineRegistration.this, "Please enter the group size.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(OnlineRegistration.this, "Please enter all the fields.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
