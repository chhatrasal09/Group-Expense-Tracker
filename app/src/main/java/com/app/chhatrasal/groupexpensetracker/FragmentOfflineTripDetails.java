package com.app.chhatrasal.groupexpensetracker;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineTripDetails extends Fragment {

    private TextView totalAmountView;
    private ListView listView;
    private View rootView;
    private SQLiteDatabaseHandle databaseHandle;
    private ArrayList<String> memberList;

    TextView textView1,textView2;
    Button button;

    public FragmentOfflineTripDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_offline_trip_details, container, false);
        initialize();

        textView2.setText("Group Size : " + memberList.size() + "\nDivided Amount : Rs." + databaseHandle.getTotalAmount(getActivity()));
        CalculateAdapter adapter = new CalculateAdapter(getActivity(),memberList,databaseHandle);
        listView.setAdapter(adapter);

        onButtonClick();

        return rootView;
    }

    private void onButtonClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (databaseHandle.deleteTrip(getActivity())) {
                    startActivity(new Intent(getActivity(), StartOrViewTrip.class));
                    getActivity().finish();
                }
            }
        });
    }

    private void initialize() {

        listView = (ListView) rootView.findViewById(R.id.trip_details_list);
        databaseHandle = new SQLiteDatabaseHandle(getActivity());
        textView1 = (TextView) rootView.findViewById(R.id.trip_details_name);
        textView2 = (TextView) rootView.findViewById(R.id.trip_details_size_amount);
        button = (Button) rootView.findViewById(R.id.finish_trip);
        textView1.setText("Trip name : " + getCapitalWord(databaseHandle.getTripName(getActivity())));

        memberList = new ArrayList<>();
        memberList.clear();
        memberList.addAll(databaseHandle.getMemberList(getActivity()));
    }

    public static class CalculateAdapter extends BaseAdapter {

        private ArrayList<String> memberList;
        private Context context;
        private View rootView;
        private LayoutInflater inflater;
        private TextView memberName,payOrReceive,amountPaid;
        private SQLiteDatabaseHandle databaseHandle;

        public CalculateAdapter(Context context, ArrayList<String> memberList, SQLiteDatabaseHandle databaseHandle) {
            this.memberList = memberList;
            this.context = context;
            this.databaseHandle = databaseHandle;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return memberList.size();
        }

        @Override
        public Object getItem(int position) {
            return memberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            rootView = inflater.inflate(R.layout.calculate_list_view,parent,false);

            initializeRoot();

            assignView(position);

            return rootView;
        }

        private void assignView(int position) {
            memberName.setText(memberList.get(position));
            int amount = databaseHandle.getIndividualAmount(context.getApplicationContext(),memberList.get(position));

            amountPaid.setText("Amount paid : Rs." + amount);
            amount -= databaseHandle.getTotalAmount(context);

            if (amount <= 0){
                amount *= (-1);
                payOrReceive.setText("Amount to pay : Rs." + amount);
            }else {
                payOrReceive.setText("Amount to receive : Rs." + amount);
            }
        }

        private void initializeRoot() {
            memberName = (TextView) rootView.findViewById(R.id.member_name);
            payOrReceive = (TextView) rootView.findViewById(R.id.pay_or_receive);
            amountPaid = (TextView) rootView.findViewById(R.id.amount_paid);
        }
    }

    public static String getCapitalWord(String input){
        return input.substring(0,1).toUpperCase() + input.substring(1);
    }
}
