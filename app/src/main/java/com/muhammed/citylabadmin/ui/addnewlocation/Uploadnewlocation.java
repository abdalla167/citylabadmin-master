package com.muhammed.citylabadmin.ui.addnewlocation;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muhammed.citylabadmin.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Uploadnewlocation extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Geocoder geo;
    EditText addresse;
    Button conferm,cancel;
    SupportMapFragment mapFragment;

    public Uploadnewlocation() {
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
        View view= inflater.inflate(R.layout.fragment_uploadnewlocation, container, false);


        mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            geo = new Geocoder(getActivity(), Locale.getDefault());

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    try {
                        if (geo == null)
                            geo = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (address.size() > 0) {
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Name:" + address.get(0).getCountryName()
                                    + ". Address:" + address.get(0).getAddressLine(0)));

//                            txtMarkers.setText("Name:" + address.get(0).getCountryName()
//                                    + ". Address:" + address.get(0).getAddressLine(0));



                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.custom_add_new_location);


                            dialog.show();

                            addresse= dialog.findViewById(R.id.editTextuploadloacation);
                            conferm=dialog.findViewById(R.id.conferuploadloacation);
                            cancel=dialog.findViewById(R.id.canseluploadloacation);
                            conferm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                    mMap.clear();
                                }
                            });

                        }
                    } catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(getActivity(), "Error:" + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Log.d("TAG", "onMarkerClick: "+marker.getTitle().toString() + " Lat:" + marker.getPosition().latitude + " Long:" + marker.getPosition().longitude);


                    return false;
                }
            });
        }

    }

}