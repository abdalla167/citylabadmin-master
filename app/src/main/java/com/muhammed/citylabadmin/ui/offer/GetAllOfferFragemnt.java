package com.muhammed.citylabadmin.ui.offer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.SplashScreen;
import com.muhammed.citylabadmin.data.model.AllOffer;
import com.muhammed.citylabadmin.data.model.Datum;
import com.muhammed.citylabadmin.data.model.user.User;
import com.muhammed.citylabadmin.helper.LoadingDialog;
import com.muhammed.citylabadmin.helper.NetworkState;
import com.muhammed.citylabadmin.helper.Utile;
import com.muhammed.citylabadmin.ui.adapter.OfferOnClick;
import com.muhammed.citylabadmin.ui.adapter.OfferUpdata;
import com.muhammed.citylabadmin.ui.adapter.user.UserAdapter;
import com.muhammed.citylabadmin.ui.users.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetAllOfferFragemnt extends Fragment implements OfferOnClick {

    RecyclerView recyclerView;
    OfferUpdata offerUpdata;
    OfferViewModel offerViewModel;
     LinearLayoutManager layoutManager;
    OfferOnClick offerOnClick;
    ProgressBar progressBar;

    public GetAllOfferFragemnt() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           View view=inflater.inflate(R.layout.fragment_get_all_offer_fragemnt, container, false);
        offerViewModel= ViewModelProviders.of(getActivity()).get(OfferViewModel.class);
        recyclerView=view.findViewById(R.id.recycler_id_offer);
        progressBar=view.findViewById(R.id.prograsgetoffer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        offerUpdata=new OfferUpdata(getContext(),this);

        offerViewModel.getallpffer().observe(getViewLifecycleOwner(), new Observer<AllOffer>() {
            @Override
            public void onChanged(AllOffer allOffer) {
                if (allOffer !=null)
                {
                    offerUpdata.setAllOffers(allOffer);
                    layoutManager=new LinearLayoutManager(requireContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(offerUpdata);
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        return view;

    }


    @Override
    public void update_offer(Datum datum) {
        //go to upload offer with the old data and after tha update
        Bundle args = new Bundle();
        String personJsonString = Utile.getGsonParser().toJson(datum);
        args.putString("object_offer", personJsonString);
        SplashScreen.stat=1;
        Navigation.findNavController(getView()).
                navigate(R.id.action_updateOfferScreen_to_uploadOfferScreen,args);
    }
}
