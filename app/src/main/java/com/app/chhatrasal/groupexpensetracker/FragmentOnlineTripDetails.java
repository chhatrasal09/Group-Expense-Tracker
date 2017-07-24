package com.app.chhatrasal.groupexpensetracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnlineTripDetails extends Fragment {

    TextView textView1,textView2,textView3;
    ListView listView;
    Button button;

    public FragmentOnlineTripDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_online_trip_details, container, false);
        textView1 = (TextView) rootView.findViewById(R.id.trip_details_text);
        textView2 = (TextView) rootView.findViewById(R.id.trip_details_name);
        textView3 = (TextView) rootView.findViewById(R.id.trip_details_size_amount);
        listView = (ListView) rootView.findViewById(R.id.trip_details_list);
        button = (Button) rootView.findViewById(R.id.finish_trip);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),StartOrViewTrip.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}
