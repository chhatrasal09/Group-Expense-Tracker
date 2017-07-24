package com.app.chhatrasal.groupexpensetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.chhatrasal.groupexpensetracker.ModelExpense;
import com.app.chhatrasal.groupexpensetracker.ModelMember;
import com.app.chhatrasal.groupexpensetracker.ModelTrip;
import com.app.chhatrasal.groupexpensetracker.SQLiteDatabaseHandle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMembers extends AppCompatActivity {

    private static boolean addMemberTransition = false;
    private static ArrayList<ModelMember> list = new ArrayList<>();
    private LinearLayout child;
    private EditText inputName;
    private List<EditText> editTextList = new ArrayList<>();
    private Button submit;


    private int size = 0, tripID = 0;
    private String tripName = "";
    private SQLiteDatabaseHandle databaseHandle;
    private ArrayList<String> names = new ArrayList<>();

    private ModelTrip modelTrip;
    private ModelMember[] modelMember;
    private ModelExpense modelExpense;
    private DatabaseReference reference, childRef;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (FirebaseAuth.getInstance().getCurrentUser() !=null){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        initialize();

        onButtonClick();
    }

    public static ArrayList<ModelMember> getList() {
        return list;
    }

    public static void setList(ArrayList<ModelMember> list) {
        AddMembers.list = list;
    }

    public static boolean isAddMemberTransition() {
        return addMemberTransition;
    }

    public static void setAddMemberTransition(boolean addMemberTransition) {
        AddMembers.addMemberTransition = addMemberTransition;
    }

    private void initialize() {

        databaseHandle = new SQLiteDatabaseHandle(this);

        submit = (Button) findViewById(R.id.submit_button);

        child = (LinearLayout) findViewById(R.id.child_layout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        size = getIntent().getExtras().getInt("groupsize");
        tripName = getIntent().getExtras().getString("tripname");
        tripID = getIntent().getExtras().getInt("tripid");

        String hint = "";

        for (int i = 0; i < size; ++i) {
            hint = "Enter member " + (i + 1) + " name";
            inputName = new EditText(this);
            inputName.setId(i);
            inputName.setLayoutParams(params);
            inputName.setTextSize(20);
            inputName.setSingleLine();
            inputName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            inputName.setHint(hint);
            editTextList.add(inputName);
            child.addView(inputName, i + 1);
        }
    }

    private void onButtonClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean emptyField = false;
                for (int i = 0; i < size; ++i) {
                    if (TextUtils.isEmpty(editTextList.get(i).toString()))
                        emptyField = true;
                }
                if (emptyField)
                    Toast.makeText(AddMembers.this, "All the fields are mandatory", Toast.LENGTH_SHORT).show();
                else {
                    if (StartOrViewTrip.isOnlineMode()) {
                        for (int i = 0; i < size; ++i) {
                            names.add(editTextList.get(i).getText().toString());
                        }
                        createOnlineTrip();
                        setAddMemberTransition(true);
                        Intent intent = new Intent(AddMembers.this, OnlineHome.class);
                        startActivity(intent);
                        finish();
                    }
                    if (StartOrViewTrip.isOfflineMode()) {
                        for (int i = 0; i < size; ++i) {
                            String field = editTextList.get(i).getText().toString();
                            field = field.replaceAll("\\s+", "_");
                            names.add(field);
                        }
                        boolean status = databaseHandle.addTrip(tripName.toLowerCase(), tripID, size, getApplicationContext());
                        databaseHandle.addMember(tripID, names, names.size(), getApplicationContext());
                        if (status) {
                            startActivity(new Intent(AddMembers.this, OfflineHome.class));
                        }
                    }
                }
            }
        });
    }

    private void createOnlineTrip() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String user = StartOrViewTrip.getFirebaseUser().getUid();
                reference = FirebaseDatabase.getInstance().getReference().child("Trip");

                modelTrip = new ModelTrip();
                modelMember = new ModelMember[size];
                list.clear();
                for (int i = 0; i < size; ++i) {
                    modelMember[i] = new ModelMember();
                    modelMember[i].setMemberName(names.get(i));
                    modelMember[i].setTotal(0);
                    list.add(modelMember[i]);
                }
                modelTrip.setTripName(tripName);
                modelTrip.setTotalMembers(size);
                modelTrip.setTripStatus("active");
                Date date = new Date();
                modelTrip.setStartDate(date);
                reference.child(user).push().setValue(modelTrip);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }.execute();


    }

}
