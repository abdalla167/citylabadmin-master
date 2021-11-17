package com.muhammed.citylabadmin.helper;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muhammed.citylabadmin.data.model.login.UserData;

public class AllToken {

    static Context context=new Application();

    public AllToken(Context context) {
    this.context=context;
    }

    public static void SetnewToken()
    {
if(Conectedinternt()==false)
{

    Toast.makeText(context, "برجاء الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
}
        else {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference("token");

    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {


            String value = snapshot.getValue(String.class);
            UserData userData = new UserData();
            userData.setToken(value);

            MyPreference.saveUser(userData);
              Log.d("TAG", "onDataChange: "+value);
             Log.d("TAG", "onDataChange:token "+MyPreference.SHARED_USER_TOKEN);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

}
    }
    public static boolean Conectedinternt()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return   true;
        }
        else
            return false;
    }
}
