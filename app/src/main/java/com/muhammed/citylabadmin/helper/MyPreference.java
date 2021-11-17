package com.muhammed.citylabadmin.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.muhammed.citylabadmin.data.model.admin.AdminModle;
import com.muhammed.citylabadmin.data.model.login.UserData;

public class MyPreference {

    public static final String SHARED_USER_TOKEN = "token";
    public static final String SHARED_USER_NAME = "token";
    public static final String SHARED_USER_PHONE = "token";


    static private Context appContext;

    public static void init(Context context) {
        appContext = context;
    }

    private static SharedPreferences getSharedPreference() {
        return appContext.getSharedPreferences("shared_file", Context.MODE_PRIVATE);
    }

    public static void saveUser(UserData user) {
        getSharedPreference().edit().putString(SHARED_USER_TOKEN, user.getToken()).apply();
    }

    public static void SaveUserFirbas(AdminModle adminModle)
    {
        getSharedPreference().edit().putString("nameadmin",adminModle.getName()).apply();
        getSharedPreference().edit().putString("passwordadmin",adminModle.getPassword()).apply();
        getSharedPreference().edit().putBoolean("acc",adminModle.isAdmin_or_no()).apply();


    }
    public static void Logout()
    {
getSharedPreference().edit().remove("nameadmin").apply();
        getSharedPreference().edit().remove("passwordadmin").apply();
        getSharedPreference().edit().remove("acc").apply();

    }
    public static AdminModle GetdataAdmin()
    {

        AdminModle adminModle=new AdminModle();
        adminModle.setName(getSharedPreference().getString("nameadmin", ""));
        adminModle.setPassword(getSharedPreference().getString("passwordadmin", ""));
        adminModle.setAdmin_or_no(getSharedPreference().getBoolean("acc",false));
        return adminModle;
    }
    public static String getSharedString(String key) {

        return getSharedPreference().getString(key, "");
    }


}
