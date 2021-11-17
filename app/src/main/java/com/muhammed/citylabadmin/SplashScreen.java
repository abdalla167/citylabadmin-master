package com.muhammed.citylabadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muhammed.citylabadmin.data.model.login.UserData;
import com.muhammed.citylabadmin.helper.LoadingDialog;
import com.muhammed.citylabadmin.helper.MyPreference;
import com.muhammed.citylabadmin.helper.NetworkState;
import com.muhammed.citylabadmin.ui.login.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@AndroidEntryPoint
public class SplashScreen extends AppCompatActivity {
    public static int stat=0;
    private static int SPLASH_SCREEN_TIME_OUT = 6500;
    public LoginViewModel viewModel;
    boolean ok=false;
    private String deviceToken ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash_screen);

        viewModel =  new ViewModelProvider(this).get(LoginViewModel.class);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.side_slide);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setDuration(1500);
        final ImageView splash = findViewById(R.id.imagelogogsplash);
        splash.startAnimation(animation);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        deviceToken = task.getResult();
                        // Log and toast
                    }
                });
        if ( Conectedinternt())

                if (Conectedinternt()==true)
                {
                    viewModel.userLogin("01119082271" , deviceToken);
                    viewModel.loginLiveData.observe(SplashScreen.this, new Observer<NetworkState>() {
                        @Override
                        public void onChanged(NetworkState networkState) {

                            switch (networkState.status) {
                                case SUCCESS:
                                    MyPreference.saveUser((UserData) networkState.data);
                                    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                                    dbRef.child("token").setValue(MyPreference.getSharedString(MyPreference.SHARED_USER_TOKEN)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            {

                                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                                finish();
                                            }
                                        }
                                    });

                                    ok=true;
                                    break;
                                case FAILED:
                                    Toast.makeText(SplashScreen.this, ""+networkState.message, Toast.LENGTH_SHORT).show();
                                    ok=false;
                                    break;
                                default:
                                    break;
                            }
                        }
                    });


                }

else 
        {

            Toast.makeText(this, "يجب الاتصال بالانترنت", Toast.LENGTH_SHORT).show();
        }



    }
    public boolean Conectedinternt()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return   true;
        }
        else
            return false;
    }
}