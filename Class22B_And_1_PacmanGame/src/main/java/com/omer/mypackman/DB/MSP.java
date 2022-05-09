package com.omer.mypackman.DB;

import android.content.Context;
import android.content.SharedPreferences;

public class MSP {

    private final String SP_FILE = "SP_FILE";
    private static MSP me;
    private SharedPreferences sharedPreferences;

    public static MSP getMe() {
        return me;
    }

    private MSP(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static MSP initHelper(Context context) {
        if (me == null) {
            me = new MSP(context);
        }
        return me;
    }

    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }

    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }

}
