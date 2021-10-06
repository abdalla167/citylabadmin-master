package com.muhammed.citylabadmin.ui.addnewlocation;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muhammed.citylabadmin.data.model.location.LocationModle;

import java.util.ArrayList;

public class GetAllLocationViewModel   extends ViewModel {

    MutableLiveData<ArrayList<LocationModle>> locationModleMutableLiveData=new MediatorLiveData<ArrayList<LocationModle>>();
    LocationModle locationModlel=new LocationModle();
    public MutableLiveData<ArrayList<LocationModle>> getalllocation()
    {
        //get data from firebase
        ArrayList<LocationModle>locationModles=new ArrayList<>();
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference("location");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {

                        locationModlel = npsnapshot.getValue(LocationModle.class);
                        locationModles.add(locationModlel);
                    }
                    locationModleMutableLiveData.setValue(locationModles);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }


        });

        return locationModleMutableLiveData;
    }


}
