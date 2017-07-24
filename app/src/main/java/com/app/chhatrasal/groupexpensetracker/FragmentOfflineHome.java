package com.app.chhatrasal.groupexpensetracker;


import android.graphics.Color;
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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineHome extends Fragment{

    PieChart pieChart;
    ArrayList<Entry> entries;
    PieDataSet dataSet;
    ArrayList<String> memberList;
    ArrayList<String> labels;
    PieData pieData;
    View rootView;
    ArrayList<String> list;
    SQLiteDatabaseHandle databaseHandle;

    public FragmentOfflineHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_offline_home, container, false);

        initialize();

        for (int i = 0; i<memberList.size(); ++i){
            if (databaseHandle.checkData(getActivity(),memberList.get(i))){
                list.add(memberList.get(i));
            }
        }

        labels.addAll(list);

        for (int i = 0;i < memberList.size(); ++i){
            if (list.get(i).equalsIgnoreCase(memberList.get(i))){
                entries.add(new Entry(databaseHandle.getIndividualAmount(getActivity(),list.get(i)),i));
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

        pieChart.animateXY(2500,2500);
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
                Toast.makeText(getActivity(), "Rs. " + (int)e.getVal(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return rootView;
    }

    private void initialize() {
        pieChart = (PieChart) rootView.findViewById(R.id.pie_chart);
        databaseHandle = new SQLiteDatabaseHandle(getActivity());

        memberList = new ArrayList<>();
        list = new ArrayList<>();
        entries = new ArrayList<>();
        labels = new ArrayList<>();

        memberList.clear();
        list.clear();
        entries.clear();
        labels.clear();
        memberList.addAll(databaseHandle.getMemberList(getActivity()));
    }

}
