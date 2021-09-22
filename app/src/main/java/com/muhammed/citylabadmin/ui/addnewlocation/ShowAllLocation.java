package com.muhammed.citylabadmin.ui.addnewlocation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.muhammed.citylabadmin.R;


public class ShowAllLocation extends Fragment {

    RecyclerView recyclerViewShowAllLocation;
    ImageView AddnewLocation;
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
        View view=inflater.inflate(R.layout.fragment_show_all_location, container, false);
        intialzation(view);
        AddnewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).
                        navigate(R.id.action_showalllocation_to_uploadlocation);
            }
        });
        return view;
    }
    public void intialzation(View view)
    {
        AddnewLocation=view.findViewById(R.id.add_new_location);
        recyclerViewShowAllLocation=view.findViewById(R.id.recycler_id_location);

    }
}