package com.app.chhatrasal.groupexpensetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.github.mikephil.charting.utils.EntryXIndexComparator;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandle extends SQLiteOpenHelper {

    private Cursor cursor;
    public static final String db_name = "myDatabase";
    public static final int dbVersion = 1;
    private SQLiteDatabase database;

    public SQLiteDatabaseHandle(Context context) {
        super(context, db_name, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table trip");
    }

    public boolean addTrip(String destination, int tripId, int groupSize, Context context) {
        database = getWritableDatabase();
        database.execSQL("create table if not exists trip(destination varchar(30),tripid int(4),groupsize int(2))");
        try {
            database.execSQL("insert into trip values (\'" + destination + "\'," + tripId + "," + groupSize + ")");
            database.close();
            return true;
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean addMember(int tripID,List<String> names, int size, Context context) {
        database = getWritableDatabase();
        try {
            database.execSQL("create table if not exists members(name varchar(20), tripid int(4))");
            for (int i = 0; i < size; ++i) {
                String name = names.get(i);
                database.execSQL("insert into members values(\'" + name.toLowerCase() + "\'," + tripID + ")");
                database.execSQL("create table if not exists " + name.toLowerCase() + "(expense varchar(20),amount int(5))");
            }
            database.close();
            return true;
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public ArrayList<String> getMemberList(Context context) {
        ArrayList<String> memberList = new ArrayList<>();
        database = getReadableDatabase();
        try {
            cursor = database.rawQuery("select * from members", null);
            if (!cursor.moveToNext())
                return memberList;
            cursor.moveToFirst();
            do {

                    memberList.add(FragmentOfflineTripDetails.getCapitalWord(cursor.getString(0)));
            } while (cursor.moveToNext());
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return memberList;
    }

    public ArrayList<String> getExpenseList(Context context, ArrayList<String> memberList) {
        database = getReadableDatabase();
        ArrayList<String> expenseList = new ArrayList<>();
        String data = "";
        try {
            for (int i = 0; i < memberList.size(); ++i) {
                cursor = database.rawQuery("select * from " + memberList.get(i), null);
                if (!cursor.moveToNext())
                    return expenseList;
                cursor.moveToFirst();
                data = "";
                do {
                    data = data + FragmentOfflineTripDetails.getCapitalWord(cursor.getString(0) )+ " - " + cursor.getInt(1) + "\n";
                } while (cursor.moveToNext());
                data = data.trim();
                expenseList.add(data);
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return expenseList;
    }

    public boolean addExpense(Context context,String name, String expense,int amount){
        database = getWritableDatabase();
        try{
            database.execSQL("insert into " + name.toLowerCase() + " values(\'" + expense.toLowerCase() + "\'," + amount +")");
            database.close();
            return true;
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean checkCurrentTrip(Context context){
        database = getReadableDatabase();
        try{
            cursor = database.rawQuery("select * from trip",null);
            if (!cursor.moveToNext())
                return false;
            database.close();
            return true;
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public ArrayList<String> getSimpleExpenseList(Context contetx, String name){
        ArrayList<String> expenseList = new ArrayList<>();
        expenseList.clear();
        database = getReadableDatabase();
        try{
            cursor = database.rawQuery("select * from " + name,null);
            if (!cursor.moveToNext())
                return expenseList;
            cursor.moveToFirst();
            do {
                expenseList.add(FragmentOfflineTripDetails.getCapitalWord(cursor.getString(0)));
            }while (cursor.moveToNext());
            cursor.close();
        }catch (Exception e){
            Toast.makeText(contetx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return expenseList;
    }

    public int getAmount(Context context,String name,String expense){
        int amount = 0;
        database = getReadableDatabase();
        try{
            cursor = database.rawQuery("select amount from " + name.toLowerCase() + " where expense = \'" + expense.toLowerCase() + "\'",null);
            if (!cursor.moveToFirst())
                return 0;
            cursor.moveToFirst();
            amount = cursor.getInt(0);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return amount;
    }

    public boolean updateExpense(Context context, String name, String expense, int amount){
        database = getWritableDatabase();
        try{
            database.execSQL("update " + name + " set amount = " + amount + " where expense = \'" + expense + "\'");
            database.close();
            return true;
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public int getTotalAmount(Context context){
        database = getReadableDatabase();
        int totalAmount = 0;
        ArrayList<String> memberList = new ArrayList<>();
        memberList.clear();
        try{
            cursor = database.rawQuery("select * from members",null);
            if (!cursor.moveToNext())
                return 0;
            cursor.moveToFirst();
            do {
                memberList.add(cursor.getString(0));
            }while (cursor.moveToNext());
            cursor.close();
            for (int i = 0; i < memberList.size(); ++i){
                cursor = database.rawQuery("select amount from " + memberList.get(i),null);
                if (!cursor.moveToFirst())
                    return 0;
                cursor.moveToFirst();
                do {
                    totalAmount += cursor.getInt(0);
                }while (cursor.moveToNext());
                cursor.close();
            }
            totalAmount /= memberList.size();
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return totalAmount;
    }

    public int getIndividualAmount(Context context,String name){
        database = getReadableDatabase();
        int amount = 0;
        try{
            cursor = database.rawQuery("select amount from " + name,null);
            if (!cursor.moveToNext())
                return 0;
            cursor.moveToFirst();
            do {
                amount += cursor.getInt(0);
            }while (cursor.moveToNext());
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return amount;
    }

    public boolean deleteTrip(Context context){
        database = getWritableDatabase();
        int tripID = 0;
        ArrayList<String> memberList = getMemberList(context);
        try{
            cursor = database.rawQuery("select * from trip",null);
            cursor.moveToFirst();
            tripID = cursor.getInt(1);
            cursor.close();
            database.execSQL("delete from trip where tripid = " + tripID);
            for (int i = 0; i< memberList.size(); ++i){
                database.execSQL("drop table " + memberList.get(i));
            }
            database.execSQL("drop table members");
            database.execSQL("drop table trip");
            return true;
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean checkData(Context context, String name){
        database = getReadableDatabase();
        try{
            cursor = database.rawQuery("select * from " + name,null);
            cursor.moveToFirst();
            cursor.close();
            return true;
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public String getTripName(Context context){
        String tripName = "";
        database = getReadableDatabase();
        try{
            cursor = database.rawQuery("select * from trip",null);
            cursor.moveToFirst();
            tripName += cursor.getString(0);
            cursor.close();
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();;
        }
        return tripName;
    }

}
