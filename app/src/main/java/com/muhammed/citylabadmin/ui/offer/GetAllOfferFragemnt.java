package com.muhammed.citylabadmin.ui.offer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.SplashScreen;
import com.muhammed.citylabadmin.data.model.offer.AllOffer;
import com.muhammed.citylabadmin.data.model.offer.Datum;
import com.muhammed.citylabadmin.helper.Utile;
import com.muhammed.citylabadmin.ui.adapter.offer.OfferOnClick;
import com.muhammed.citylabadmin.ui.adapter.offer.OfferUpdata;

public class GetAllOfferFragemnt extends Fragment implements OfferOnClick {

    RecyclerView recyclerView;
    OfferUpdata offerUpdata;
    OfferViewModel offerViewModel;
     LinearLayoutManager layoutManager;
    OfferOnClick offerOnClick;
    ProgressBar progressBar;
    TextView textView;

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
        textView=view.findViewById(R.id.textnooffer);
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
                if(allOffer.getData().size()==0||allOffer.getData() ==null)
                {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
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
