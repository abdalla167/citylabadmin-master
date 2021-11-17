package com.muhammed.citylabadmin.ui.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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
import com.muhammed.citylabadmin.HomeAdminScreen;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.data.model.admin.AdminModle;
import com.muhammed.citylabadmin.data.model.login.UserData;
import com.muhammed.citylabadmin.databinding.FragmentAddUserBinding;
import com.muhammed.citylabadmin.databinding.FragmentAddnewAdminBinding;
import com.muhammed.citylabadmin.databinding.FragmentLoginScreenBinding;
import com.muhammed.citylabadmin.helper.AllToken;
import com.muhammed.citylabadmin.helper.LoadingDialog;
import com.muhammed.citylabadmin.helper.MyPreference;
import com.muhammed.citylabadmin.helper.NetworkState;
import com.muhammed.citylabadmin.ui.login.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AddUserFragment extends Fragment {

    private UserViewModel viewModel;
    private FragmentAddUserBinding binding;
    private FragmentAddnewAdminBinding binding2;
    DatabaseReference mDatabaseref;
    FirebaseDatabase database;
    private String deviceToken ;

    public AddUserFragment() {
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
        if(HomeAdminScreen.stat==1) {
            return inflater.inflate(R.layout.fragment_add_user, container, false);
        }
        else
            return inflater.inflate(R.layout.fragment_addnew_admin, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        database = FirebaseDatabase.getInstance();
        mDatabaseref = database.getReference();


        if(HomeAdminScreen.stat==1) {
            AllToken allToken=new AllToken(this.getContext());
            allToken.SetnewToken();

            binding = FragmentAddUserBinding.bind(view);
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);

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
            binding.addUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addUser();
                }
            });
            observe();
        }
        if(HomeAdminScreen.stat==0)
        {
            binding2 = FragmentAddnewAdminBinding.bind(view);
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            binding2.addAdminBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = binding2.newUserNameAdmin.getText().toString().trim();
                    String phone = binding2.newUserPhonePassword.getText().toString().trim();

                    if (!name.equals("") && !phone.equals("")) {
                        AdminModle adminModle = new AdminModle();
                        adminModle.setName(name);
                        adminModle.setPassword(phone);



                        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                        Query queryToGetData = dbRef.child("admin")
                                .orderByChild("name").equalTo(name);
                        queryToGetData.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){
                                    String userId = dbRef.child("phone").push().getKey();
                                    dbRef.child("admin").child(userId).setValue(adminModle);
                                    Toast.makeText(getContext(), "تم اضافة ", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    {
                                        Toast.makeText(getContext(), "يوجد نفس الببانات ", Toast.LENGTH_SHORT).show();

                                    }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });



//                        mDatabaseref.child("admin").push().setValue(adminModle).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//
//                                    Toast.makeText(getContext(), "تم اضافة الموقع", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                      }
                    else
                        Toast.makeText(getContext(), "برجاء اكمال البيانات", Toast.LENGTH_SHORT).show();
                }
            });
            binding2.deletUserBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query applesQuery = ref.child("admin").orderByChild("name").equalTo(binding2.newUserNameAdmin.getText().toString().trim())
                            ;

                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                            Toast.makeText(getContext(), "تم مسح admin", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            });





        }
    }

    private void observe() {
        viewModel.addUserLiveData.observe(getViewLifecycleOwner(), new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                switch (networkState.status) {
                    case SUCCESS:
                        Toast.makeText(getContext(), "" + networkState.data.toString(),
                                Toast.LENGTH_SHORT).show();

                        binding.newUserName.setText("");
                        binding.newUserPhone.setText("");
                        LoadingDialog.hideDialog();
                        break;
                    case FAILED:
                        LoadingDialog.hideDialog();
                        Toast.makeText(getContext(), "" + networkState.message, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        LoadingDialog.showDialog(getActivity());
                        break;
                }
            }

        });


    }

    private void addUser() {
        String name = binding.newUserName.getText().toString().trim();
        String phone = binding.newUserPhone.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(requireContext(), "ادخل البيانات كاملة", Toast.LENGTH_SHORT).show();
        }
        viewModel.addNewUser(name, phone , deviceToken);


    }
}