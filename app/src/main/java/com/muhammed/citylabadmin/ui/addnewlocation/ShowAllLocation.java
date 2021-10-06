package com.muhammed.citylabadmin.ui.addnewlocation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.data.model.location.LocationModle;
import com.muhammed.citylabadmin.ui.adapter.location.LocationAdapter;

import java.util.ArrayList;


public class ShowAllLocation extends Fragment {

    RecyclerView recyclerViewShowAllLocation;
    ImageView AddnewLocation;
    GetAllLocationViewModel getAllLocationViewModel;
    LinearLayoutManager layoutManager;
LocationAdapter locationAdapter;
    public ShowAllLocation() {
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
        View view = inflater.inflate(R.layout.fragment_show_all_location, container, false);
        intialzation(view);
        if (isConnected()) {
            getAllLocationViewModel = ViewModelProviders.of(this).get(GetAllLocationViewModel.class);
            getAllLocationViewModel.getalllocation().observe(getViewLifecycleOwner(), new Observer<ArrayList<LocationModle>>() {
                        @Override
                        public void onChanged(ArrayList<LocationModle> locationModles) {
locationAdapter=new LocationAdapter(getContext());
locationAdapter.setlist(locationModles);
                            layoutManager=new LinearLayoutManager(requireContext());
                            recyclerViewShowAllLocation.setLayoutManager(layoutManager);
                            recyclerViewShowAllLocation.setAdapter(locationAdapter);


                        }
                    });


        }
        AddnewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).
                        navigate(R.id.action_showalllocation_to_uploadlocation);
            }
        });
        return view;
    }

    public void intialzation(View view) {
        AddnewLocation = view.findViewById(R.id.add_new_location);
        recyclerViewShowAllLocation = view.findViewById(R.id.recycler_id_location);

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

}