package com.omer.mypackman.DB;

import com.omer.mypackman.objects.Records;

import java.util.ArrayList;
import java.util.Collections;
public class myDataBase {
    private ArrayList<Records> records;
    private static myDataBase myDB;
    public myDataBase() {
        this.records =  new ArrayList<>();
    }

    public static myDataBase initMyDB() {
        if (myDB == null) {
            myDB = new myDataBase();
        }
        return myDB;
    }

    public void addRecord (Records record) {

        records.add(record);
    }

    public Records getSpecificRecord (int position) {
        return records.get(position);
    }

    public ArrayList<Records> getRecords() {
        return records;
    }

    public void  sortByScore () {
        Collections.sort(records);
    }
}
