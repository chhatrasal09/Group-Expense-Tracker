package com.app.chhatrasal.groupexpensetracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineModifyExpense extends Fragment {

    TextView textView;
    Spinner spinner1, spinner2;
    EditText editText;
    Button button;
    String tablename = "", activity = "";
    int text = 0;
    View rootView;
    SQLiteDatabaseHandle databaseHandle;
    ArrayList<String> memberList, expenseList;

    public FragmentOfflineModifyExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_offline_modify_expense, container, false);

        initialize();

        setSpinners();

        onButtonClick();
//
//        ArrayList<String> list = databaseDB.getactivity(tablename).getMemberNameListObject();
//        if (list == null){
//            list = new ArrayList<>();
//        }
//        list.add(0,"Select");
//
//
//        editText.setText(databaseDB.itemAmountPaid(tablename,activity).getItemAmountPaidObject());


        return rootView;
    }

    private void onButtonClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText())) {
                    int amount = Integer.parseInt(editText.getText().toString());
                    if (databaseHandle.updateExpense(getActivity(),tablename.toLowerCase(),activity.toLowerCase(),amount))
                        Toast.makeText(getActivity(), "Expense Modified!!!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Modification unsuccessfull", Toast.LENGTH_SHORT).show();;
                } else
                    Toast.makeText(getActivity(), "Please enter the new amount.", Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void setSpinners() {

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, memberList);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tablename = parent.getItemAtPosition(position).toString();
                expenseList.clear();
                expenseList.addAll(databaseHandle.getSimpleExpenseList(getActivity(), tablename));
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                        android.R.id.text1, expenseList);
                adapter2.notifyDataSetChanged();
                spinner2.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activity = parent.getItemAtPosition(position).toString();
                if (!activity.equalsIgnoreCase("Select")) {
                    text = databaseHandle.getAmount(getActivity(), tablename, activity);
                    editText.setText("" + text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initialize() {
        textView = (TextView) rootView.findViewById(R.id.add_expense_text_modify);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner_name_modify);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner_item);
        editText = (EditText) rootView.findViewById(R.id.amount_paid_modify);
        button = (Button) rootView.findViewById(R.id.submit_modify);

        databaseHandle = new SQLiteDatabaseHandle(getActivity());

        memberList = new ArrayList<>();
        expenseList = new ArrayList<>();
        memberList.clear();
        expenseList.clear();
        memberList.addAll(databaseHandle.getMemberList(getActivity()));
    }

}
