package com.app.chhatrasal.groupexpensetracker;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnlineHome extends Fragment {

    private PieChart pieChart;
    private ArrayList<Entry> entries;
    private PieDataSet dataSet;
    private ArrayList<String> labels;
    private PieData pieData;
    private View rootView;
    private ArrayList<String> list;

    private DatabaseReference reference;
    private ModelTrip trip;
    private ModelMember member;
    private ModelExpense expense;
    private ArrayList<ModelMember> memberList1, memberList2;
    private ArrayList<ModelExpense> expenseList;

    public FragmentOnlineHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_online_home, container, false);

        initialize();

        for (int i = 0; i < memberList2.size(); ++i) {
            list.add(memberList2.get(i).getMemberName());
        }

        labels.addAll(list);

        for (int i = 0;i < memberList2.size(); ++i){
            if (list.get(i).equalsIgnoreCase(memberList2.get(i).getMemberName())){
                entries.add(new Entry(memberList2.get(i).getTotal(),i));
            }
            else{
                entries.add(new Entry(0,i));
            }
        }

        dataSet = new PieDataSet(entries, "");
        pieData = new PieData(labels, dataSet);

        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setValueTextSize(18);

        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.BLACK);
        pieData.setHighlightEnabled(true);

        pieChart.animateXY(2500, 2500);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setData(pieData);
        pieChart.setDescription("Description");
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setTransparentCircleAlpha(90);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e == null)
                    return;
                Toast.makeText(getActivity(), "Rs. " + (int) e.getVal(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return rootView;
    }

    private void initialize() {
        pieChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        memberList1 = new ArrayList<>();
        memberList2 = new ArrayList<>();
        list = new ArrayList<>();
        entries = new ArrayList<>();
        labels = new ArrayList<>();

        memberList1.clear();
        memberList2.clear();
        list.clear();
        entries.clear();
        labels.clear();

        trip = new ModelTrip();
        expense = new ModelExpense();
        member = new ModelMember();

        getMemberList();
//        memberList.addAll(databaseHandle.getMemberList(getActivity()));
    }

    private void getMemberList() {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://groupexpensetracker-6a2ff.firebaseio.com/Trip/NdBq1ul3ymNaJdYpjKaOsywgE7N2");
//                reference = FirebaseDatabase.getInstance().getReference().child("Trip").child(StartOrViewTrip.getFirebaseUser().getUid());
                reference.orderByChild("tripStatus").equalTo("active").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getRef().addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        trip = dataSnapshot.getValue(ModelTrip.class);
                                        int i = 1, j = 1;
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.getKey().equals("member " + i) || ds.getKey().equals("member " + j)) {
                                                for (DataSnapshot dsChild : ds.getChildren()) {
                                                    if (dsChild.getKey().equals("expenses") && dsChild.hasChildren()) {
                                                        member = ds.getValue(ModelMember.class);
                                                        memberList1.add(member);
                                                        ++i;
                                                    }
                                                }
                                                member = ds.getValue(ModelMember.class);
                                                memberList2.add(member);
                                                ++j;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getRef().addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        trip = dataSnapshot.getValue(ModelTrip.class);
                                        int i = 1, j = 1;
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.getKey().equals("member " + i) || ds.getKey().equals("member " + j)) {
                                                for (DataSnapshot dsChild : ds.getChildren()) {
                                                    if (dsChild.getKey().equals("expenses") && dsChild.hasChildren()) {
                                                        member = ds.getValue(ModelMember.class);
                                                        memberList1.add(member);
                                                        ++i;
                                                    }
                                                }
                                                member = ds.getValue(ModelMember.class);
                                                memberList1.add(member);
                                                ++j;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        trip = dataSnapshot.getValue(ModelTrip.class);
                                        int i = 1, j = 1;
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.getKey().equals("member " + i) || ds.getKey().equals("member " + j)) {
                                                for (DataSnapshot dsChild : ds.getChildren()) {
                                                    if (dsChild.getKey().equals("expenses") && dsChild.hasChildren()) {
                                                        member = ds.getValue(ModelMember.class);
                                                        memberList1.add(member);
                                                        ++i;
                                                    }
                                                }
                                                member = ds.getValue(ModelMember.class);
                                                memberList1.add(member);
                                                ++j;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        dataSnapshot.getRef().addValueEventListener(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        trip = dataSnapshot.getValue(ModelTrip.class);
                                        int i = 1, j = 1;
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.getKey().equals("member " + i) || ds.getKey().equals("member " + j)) {
                                                for (DataSnapshot dsChild : ds.getChildren()) {
                                                    if (dsChild.getKey().equals("expenses") && dsChild.hasChildren()) {
                                                        member = ds.getValue(ModelMember.class);
                                                        memberList1.add(member);
                                                        ++i;
                                                    }
                                                }
                                                member = ds.getValue(ModelMember.class);
                                                memberList1.add(member);
                                                ++j;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return null;
            }
        }.execute();
    }

}
