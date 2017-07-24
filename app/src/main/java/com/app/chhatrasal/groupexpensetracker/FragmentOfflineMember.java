package com.app.chhatrasal.groupexpensetracker;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FragmentOfflineMember extends Fragment {

    private RecyclerView recyclerView;
    private View rootView;
    private SQLiteDatabaseHandle databaseHandle;
    private ArrayList<String> member,expense;

    public FragmentOfflineMember() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_offline_member, container, false);

        initialize();

        getMemberAndExpenseList();

        MemberAdapter adapter = new MemberAdapter(getActivity(),member,expense);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void getMemberAndExpenseList() {

        member.clear();
        expense.clear();
        member.addAll(databaseHandle.getMemberList(getActivity()));
        expense.addAll(databaseHandle.getExpenseList(getActivity(),member));
    }

    private void initialize() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseHandle = new SQLiteDatabaseHandle(getActivity());
        member = new ArrayList<>();
        expense = new ArrayList<>();
    }

    public static class MemberAdapter extends RecyclerView.Adapter<MyViewHolder>{

        private Context context;
        private ArrayList<String> memberList;
        private ArrayList<String> expenseList;

        public MemberAdapter(Context context, ArrayList<String> memberList, ArrayList<String> expenseList) {
            this.context = context;
            this.memberList = memberList;
            this.expenseList = expenseList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.member_recycler_view,parent,false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String nam = memberList.get(position);
            holder.name.setText(nam);
            if (expenseList.size() != 0)
                holder.expense.setText(expenseList.get(position));
            else
                holder.expense.setText("");
        }

        @Override
        public int getItemCount() {
            return memberList.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,expense;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.member_name);
            expense = (TextView) itemView.findViewById(R.id.expense_name_list);
        }
    }
}
