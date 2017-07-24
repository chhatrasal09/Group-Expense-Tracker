package com.app.chhatrasal.groupexpensetracker;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineCalculate extends Fragment {

    private static int totalAmount = 0;
    private ArrayList<ChildObject> expensePaidList = new ArrayList<>();

    private TextView totalAmountView;
    private ListView listView;
    private View rootView;
    private SQLiteDatabaseHandle databaseHandle;
    private ArrayList<String> memberList;

    public FragmentOfflineCalculate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_offline_calculate, container, false);

        initialize();

        RootCalculateAdapter adapter = new RootCalculateAdapter(getActivity(),memberList,databaseHandle);
        listView.setAdapter(adapter);
        return rootView;
    }

    private void initialize() {

        totalAmountView = (TextView) rootView.findViewById(R.id.divided_amount);
        listView = (ListView) rootView.findViewById(R.id.amount_list);
        databaseHandle = new SQLiteDatabaseHandle(getActivity());

        totalAmount = databaseHandle.getTotalAmount(getActivity());
        String amount = "Expense per person : Rs." + totalAmount;
        totalAmountView.setText(amount);
        expensePaidList.clear();

        memberList = new ArrayList<>();
        memberList.clear();
        memberList.addAll(databaseHandle.getMemberList(getActivity()));
    }

    public static class RootCalculateAdapter extends BaseAdapter{

        private ArrayList<String> memberList;
        private Context context;
        private View rootView;
        private LayoutInflater inflater;
        private TextView memberName,payOrReceive,amountPaid;
        private SQLiteDatabaseHandle databaseHandle;
        private ListView listView;

        public RootCalculateAdapter(Context context, ArrayList<String> memberList, SQLiteDatabaseHandle databaseHandle) {
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

            assignRootView(position);

            return rootView;
        }

        private void assignRootView(int position) {
            memberName.setText(memberList.get(position));
            int amount = databaseHandle.getIndividualAmount(context.getApplicationContext(),memberList.get(position));

            amountPaid.setText("Amount paid : Rs." + amount);
            amount -= databaseHandle.getTotalAmount(context);

            if (amount <= 0){
                amount *= (-1);
                payOrReceive.setText("Amount to pay : Rs." + amount);
//                ChildCalculateAdapter1 adapter = new ChildCalculateAdapter1(context.getApplicationContext(),memberList,totalAmount,databaseHandle);
//                listView.setAdapter(adapter);

            }else {
                payOrReceive.setText("Amount to receive : Rs." + amount);
//                ChildCalculateAdapter2 adapter = new ChildCalculateAdapter2(context.getApplicationContext(),memberList,totalAmount,databaseHandle);
//                listView.setAdapter(adapter);
            }
        }

        private void initializeRoot() {
            memberName = (TextView) rootView.findViewById(R.id.member_name);
            payOrReceive = (TextView) rootView.findViewById(R.id.pay_or_receive);
            amountPaid = (TextView) rootView.findViewById(R.id.amount_paid);
            listView = (ListView) rootView.findViewById(R.id.child_list);
        }
    }

    public static class ChildCalculateAdapter1 extends BaseAdapter{

        private Context context;
        private View childView;
        private LayoutInflater inflater;
        private ArrayList<String> memberList;
        private ArrayList<ChildObject> expenseList;
        private int amountRemaining;
        private SQLiteDatabaseHandle databaseHandle;
        private LinearLayout child;


        public ChildCalculateAdapter1(Context context, ArrayList<String> memberList,int amountRemaining,SQLiteDatabaseHandle databaseHandle) {
            this.context = context;
            this.memberList = memberList;
            this.inflater = LayoutInflater.from(context);
            this.amountRemaining = amountRemaining;
            this.databaseHandle = databaseHandle;

            expenseList.clear();
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

            childView = inflater.inflate(R.layout.calculate_list_view,parent,false);

            initializeChild();

            assignChildView(position);

            return childView;
        }

        private void initializeChild() {

            child = (LinearLayout) childView.findViewById(R.id.calculate_child_layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            for (int i = 0; i <memberList.size(); ++i){
                ChildObject child = new ChildObject();
                child.setMemberName(memberList.get(i));
                child.setAmount(databaseHandle.getIndividualAmount(context.getApplicationContext(),memberList.get(i)));
                expenseList.add(child);
            }
        }

        private void assignChildView(int position) {
        }


    }

    public static class ChildCalculateAdapter2 extends BaseAdapter{

        private Context context;
        private View childView;
        private LayoutInflater inflater;
        private ArrayList<String> memberList;
        private ArrayList<ChildObject> expenseList;
        private int amountRemaining;
        private SQLiteDatabaseHandle databaseHandle;
        private LinearLayout child;
        private TextView textView;


        public ChildCalculateAdapter2(Context context, ArrayList<String> memberList,int amountRemaining,SQLiteDatabaseHandle databaseHandle) {
            this.context = context;
            this.memberList = memberList;
            this.inflater = LayoutInflater.from(context);
            this.amountRemaining = amountRemaining;
            this.databaseHandle = databaseHandle;

            expenseList.clear();
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

            childView = inflater.inflate(R.layout.calculate_list_view,parent,false);

            initializeChild();

            assignChildView(position);

            return childView;
        }

        private void initializeChild() {

            child = (LinearLayout) childView.findViewById(R.id.calculate_child_layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 0,individualAmount = 0;

            individualAmount = databaseHandle.getIndividualAmount(context.getApplicationContext(),memberList.get(i));
            textView = new TextView(context.getApplicationContext());
            textView.setId(i);
            textView.setLayoutParams(params);
            textView.setTextSize(20);
//            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText("Receive from");
            for ( i = 0; i <memberList.size(); ++i){
                ChildObject child = new ChildObject();
                child.setMemberName(memberList.get(i));
                child.setAmount(databaseHandle.getIndividualAmount(context.getApplicationContext(),memberList.get(i)));
                expenseList.add(child);
            }
        }

        private void assignChildView(int position) {
        }


    }

    public static class ChildObject{
        String memberName;
        int amount;

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
