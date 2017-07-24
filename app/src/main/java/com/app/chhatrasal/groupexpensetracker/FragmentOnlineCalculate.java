package com.app.chhatrasal.groupexpensetracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnlineCalculate extends Fragment {

    private TextView totalAmountView;
    private ListView listView;
    private View rootView;
    private SQLiteDatabaseHandle databaseHandle;

    public FragmentOnlineCalculate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_online_calculate, container, false);

        initialize();

        return rootView;
    }

    private void initialize() {

        totalAmountView = (TextView) rootView.findViewById(R.id.divided_amount);
        listView = (ListView) rootView.findViewById(R.id.amount_list);
        databaseHandle = new SQLiteDatabaseHandle(getActivity());

        totalAmountView.setText(databaseHandle.getTotalAmount(getActivity()));
    }

}
