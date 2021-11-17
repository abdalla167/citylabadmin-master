package com.muhammed.citylabadmin.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.data.model.admin.AdminModle;
import com.muhammed.citylabadmin.data.model.login.UserData;
import com.muhammed.citylabadmin.databinding.FragmentLoginScreenBinding;
import com.muhammed.citylabadmin.helper.LoadingDialog;
import com.muhammed.citylabadmin.helper.MyPreference;
import com.muhammed.citylabadmin.helper.NetworkState;
import com.muhammed.citylabadmin.helper.Utile;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class LoginScreen extends Fragment {

    private LoginViewModel viewModel;
    private FragmentLoginScreenBinding binding;
    public NavController navController;
    private String phone = "";

    private String deviceToken ;

    public LoginScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login_screen, container, false);

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentLoginScreenBinding.bind(view);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        if( !MyPreference.GetdataAdmin().getName().equals(""))
        {
            Log.d("TAG", "onViewCreated: "+MyPreference.GetdataAdmin().getName()+" "+MyPreference.GetdataAdmin().isAdmin_or_no());
            Navigation.findNavController(requireView()).navigate(R.id.action_loginScreen_to_homeAdminScreen);
        }

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



        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name =binding.loginUserPhone.getText().toString().trim();
                String pass=binding.loginUserPass.getText().toString().trim();
                if (name.equals("mahmod")&&pass.equals("01144167167"))
                {
                    AdminModle adminModle=new AdminModle();
                    adminModle.setAdmin_or_no(true);
                    adminModle.setPassword(pass);
                    adminModle.setName(name);
                    MyPreference.SaveUserFirbas(adminModle);
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginScreen_to_homeAdminScreen);

                }
                else
                {
                    final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                    Query queryToGetData = dbRef.child("admin")
                            .orderByChild("name").equalTo(name);
                    queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    if(pass.equals(  appleSnapshot.child("password").getValue().toString()))
                                    {
                                        Toast.makeText(getContext(), "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                                        AdminModle adminModle=new AdminModle();
                                        adminModle.setAdmin_or_no(false);
                                        adminModle.setPassword(pass);
                                        adminModle.setName(name);
                                        MyPreference.SaveUserFirbas(adminModle);
                                        Navigation.findNavController(requireView()).navigate(R.id.action_loginScreen_to_homeAdminScreen);

                                    }
                                }
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {


                        }


                    });

                }
                if(name.equals("") || pass.equals(""))
                {
                    Toast.makeText(getContext(), "يوجد نفس الببانات ", Toast.LENGTH_SHORT).show();
                }




                //
            }
        });



    }
}