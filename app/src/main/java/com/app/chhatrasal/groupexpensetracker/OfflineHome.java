package com.app.chhatrasal.groupexpensetracker;

import android.content.Intent;
import android.graphics.Color;
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

import static com.app.chhatrasal.groupexpensetracker.R.id.action_mode_close_button;
import static com.app.chhatrasal.groupexpensetracker.R.id.activity_offline_home;
import static com.app.chhatrasal.groupexpensetracker.R.id.activity_start_or_view_trip;
import static com.app.chhatrasal.groupexpensetracker.R.id.thing_proto;
import static com.app.chhatrasal.groupexpensetracker.R.id.toolbar;

public class OfflineHome extends AppCompatActivity {

    private Toolbar toolbar1;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Fragment addExpense=null;
    private FragmentTransaction fragmentTransaction=null;
    private LayoutInflater toastInflater;
    private View toastLayout;
    private Toast toast;
    private TextView toastView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_home);

        toolbar1=(Toolbar)findViewById(toolbar);
        navigationView=(NavigationView)findViewById(R.id.navigationview);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        //setting the toolbar as actionbar
//        setSupportActionBar(toolbar1);

        //setting background color of toolbar
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
                addExpense=new FragmentOfflineHome();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.members:
                createToast("Members Selected");
                addExpense=new FragmentOfflineMember();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.add_expense:
                createToast("Add Expense Selected");
                addExpense=new FragmentOfflineAddExpense();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.modify_expense:
                createToast("Modify Expense Selected");
                addExpense=new FragmentOfflineModifyExpense();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.calculate:
                createToast("Calculate Selected");
                addExpense=new FragmentOfflineCalculate();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,addExpense);
                fragmentTransaction.commit();
                return true;
            case R.id.trip_details:
                createToast("Trip Details Selected");
                addExpense=new FragmentOfflineTripDetails();
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
