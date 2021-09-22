package com.muhammed.citylabadmin.ui.addnewlocation;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.muhammed.citylabadmin.data.model.location.LocationModle;
import com.muhammed.citylabadmin.data.model.reservation.Booking;

public class GetAllLocationViewModel   extends ViewModel {

    MutableLiveData<LocationModle> locationModleMutableLiveData=new MediatorLiveData<>();

    public MutableLiveData<LocationModle>getalllocation()
    {
        //get data from firebase

        return locationModleMutableLiveData;
    }


}
