package com.app.chhatrasal.groupexpensetracker;


import android.content.Context;
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

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfflineAddExpense extends Fragment {

    TextView textView;
    EditText editText1, editText2;
    Button button;
    Spinner spinner;
    SQLiteDatabaseHandle databaseHandle;
    String tableName = "";
    View rootView;
    ArrayList<String> memberList;

    public FragmentOfflineAddExpense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_offline_add_expense, container, false);

        initialize();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1, memberList);
        spinner.setAdapter(adapter);

        setSpinner();

        onButtonClick();
        return rootView;
    }

    private void setSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tableName = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void onButtonClick() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activity = editText1.getText().toString();
                if (TextUtils.isEmpty(activity) && TextUtils.isEmpty(editText2.getText()))
                    Toast.makeText(getActivity(), "Please enter all the fields.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(activity) && !TextUtils.isEmpty(editText2.getText()))
                    Toast.makeText(getActivity(), "Please enter the Expense Type.", Toast.LENGTH_SHORT).show();
                else if (!TextUtils.isEmpty(activity) && TextUtils.isEmpty(editText2.getText()))
                    Toast.makeText(getActivity(), "Please enter the Amount", Toast.LENGTH_SHORT).show();
                else {
                    int amount = Integer.parseInt(editText2.getText().toString());
                    databaseHandle.addExpense(getActivity(), tableName, activity, amount);
                    Toast.makeText(getActivity(), "Expense added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialize() {
        textView = (TextView) rootView.findViewById(R.id.add_expense_text);
        editText1 = (EditText) rootView.findViewById(R.id.expense_type);
        editText2 = (EditText) rootView.findViewById(R.id.amount_paid);
        button = (Button) rootView.findViewById(R.id.submit);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_name);

        databaseHandle = new SQLiteDatabaseHandle(getActivity());

        memberList = new ArrayList<>();
        memberList.clear();
        memberList.addAll(databaseHandle.getMemberList(getActivity()));
    }

}
