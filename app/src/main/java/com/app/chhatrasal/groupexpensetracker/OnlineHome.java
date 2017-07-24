package com.app.chhatrasal.groupexpensetracker;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.app.chhatrasal.groupexpensetracker.R.id.toolbar;

public class OnlineHome extends AppCompatActivity {

    private Toolbar toolbar1;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Fragment addExpense=null;
    private FragmentTransaction fragmentTransaction=null;

    private LayoutInflater toastInflater;
    private View toastLayout;
    private Toast toast;
    private TextView toastView;

    private DatabaseReference childRef;
    private ArrayList<ModelMember> memberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        memberList = AddMembers.getList();
        if (AddMembers.isAddMemberTransition()){
            childRef = FirebaseDatabase.getInstance().getReference().child("Trip").child(StartOrViewTrip.getFirebaseUser().getUid());
            childRef.orderByChild("tripStatus").equalTo("active").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DatabaseReference ref = dataSnapshot.getRef();
                    int i = 0;
                    while (i<memberList.size())
                        ref.child("member " + (i+1 )).setValue(memberList.get(i++));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    DatabaseReference ref = dataSnapshot.getRef();
                    int i = 0;
                    while (i<memberList.size())
                        ref.child("member " + (i+1 )).setValue(memberList.get(i++));
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    DatabaseReference ref = dataSnapshot.getRef();
                    int i = 0;
                    while (i<memberList.size())
                        ref.child("member " + (i+1 )).setValue(memberList.get(i++));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        setContentView(R.layout.activity_online_home);

        toolbar1=(Toolbar)findViewById(toolbar);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

//        setting the toolbar as actionbar
//        setSupportActionBar(toolbar1);
//
//        setting background color of toolbar
//        toolbar1.setBackgroundColor(Color.parseColor("#0081c9"));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if(menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return displayFragment(menuItem.getItemId());
            }
        });

        //creating ActionBarDrawerToggle
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar1,R.string.draweropened,R.string.drawerclosed)
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        //binding ActionBarDrawerToggle to DrawerLayout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is compulsory to avoid unusual behaviour as home button wont show up
        actionBarDrawerToggle.syncState();
        //showing addExpense for FirstTime

        toastInflater = getLayoutInflater();
        toastLayout = toastInflater.inflate(R.layout.custom_toast_layout, (ViewGroup) findViewById(R.id.custom_layout));
        toastView = (TextView) toastLayout.findViewById(R.id.toast_view);

        if(savedInstanceState==null)
        {
            displayFragment(R.id.home);
        }
    }

    public boolean displayFragment(int id)
    {
        switch (id)
        {
            case R.id.home:
                createToast("Home Selected");
                addExpense=new FragmentOnlineHome();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.members:
                createToast("Members Selected");                addExpense=new FragmentOnlineHome();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.add_expense:
                createToast("Add Expense Selected");
                addExpense=new FragmentOnlineHome();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.modify_expense:
                createToast("Modify Expense Selected");
                addExpense=new FragmentOnlineHome();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.calculate:
                createToast("Calculate Selected");
                addExpense=new FragmentOnlineHome();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.trip_details:
                createToast("Trip Details Selected");
                addExpense=new FragmentOnlineHome();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void createToast(String message){
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toastView.setText(message);
        toast.setGravity(Gravity.CENTER|Gravity.BOTTOM,0,100);
        toast.setView(toastLayout);
        toast.show();
    }
}
