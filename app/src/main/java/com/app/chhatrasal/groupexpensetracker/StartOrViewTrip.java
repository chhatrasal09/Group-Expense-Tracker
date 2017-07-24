package com.app.chhatrasal.groupexpensetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class StartOrViewTrip extends AppCompatActivity {

    private Button newUser, existingUser;
    int position = -1;
    private static boolean offlineMode = false;
    private static boolean onlineMode = false;

    private boolean databaseExist = false;

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 47;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SQLiteDatabaseHandle databaseHandle = new SQLiteDatabaseHandle(this);
    private static FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_or_view_trip);

        boolean status = databaseHandle.checkCurrentTrip(this);
        if (status) {
            startActivity(new Intent(this, OfflineHome.class));
            finish();
        }

        initialize();

        onButtonClick();
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public static void setFirebaseUser(FirebaseUser firebaseUser) {
        StartOrViewTrip.firebaseUser = firebaseUser;
    }

    private void initialize() {

        newUser = (Button) findViewById(R.id.new_user_button);
        existingUser = (Button) findViewById(R.id.existing_user_button);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setOnlineMode(true);
                    setOfflineMode(false);
                    setFirebaseUser(user);
                    checkDatabase();
                    startActivity(new Intent(getApplicationContext(), OnlineRegistration.class));
                    finish();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new
                GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

    }

    private void onButtonClick() {

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStorageTypeAlertDialog();
            }
        });

        existingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(),Home.class));
            }
        });
    }

    private void getStorageTypeAlertDialog() {

        String storageType[] = {"Offline", "Online"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select the type of storage").setSingleChoiceItems(storageType, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                position = which;
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (position == 0) {
                    setOfflineMode(true);
                    startActivity(new Intent(getApplicationContext(), OfflineRegistration.class));
                    finish();
                } else if (position == 1) {
                    setOnlineMode(true);
                    signIn();
                } else
                    Toast.makeText(StartOrViewTrip.this, "Please select the option to advance.", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).create().show();
    }

    public static boolean isOfflineMode() {
        return offlineMode;
    }

    public static void setOfflineMode(boolean offlineMode) {
        StartOrViewTrip.offlineMode = offlineMode;
    }

    public static boolean isOnlineMode() {
        return onlineMode;
    }

    public static void setOnlineMode(boolean onlineMode) {
        StartOrViewTrip.onlineMode = onlineMode;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            setFirebaseUser(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(StartOrViewTrip.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
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
                                startActivity(new Intent(getApplicationContext(), OnlineHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
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
}
