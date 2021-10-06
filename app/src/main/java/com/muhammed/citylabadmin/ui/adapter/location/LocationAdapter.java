package com.muhammed.citylabadmin.ui.adapter.location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.data.model.location.LocationModle;

import java.util.ArrayList;

public class LocationAdapter  extends RecyclerView.Adapter<LocationAdapter.LocationAdapterHolder> {
    Context mcontext;
   public ArrayList<LocationModle> locationModles=new ArrayList<>();
    public LocationAdapter (Context mcontext)
    {
        this.mcontext=mcontext;

    }
    public void setlist( ArrayList<LocationModle>locationModles)
    {
        this.locationModles=locationModles;
notifyDataSetChanged();

    }
    @NonNull
    @Override
    public LocationAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location,parent,false);
return new LocationAdapter.LocationAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapterHolder holder, int position) {

        holder.adderss.setText(locationModles.get(position).getBigadd());
        holder.partnner.setText(locationModles.get(position).getNamelabe());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageView.setClickable(false);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("location").orderByChild("bigadd").equalTo(locationModles.get(position).getBigadd().toString());

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                        locationModles.remove(position);
                        notifyItemRemoved(position);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

             holder.imageView.setClickable(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (locationModles==null)
            return 0;
        if (locationModles.size()>0)
            return locationModles.size();
        return 0;
    }

    public static class LocationAdapterHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView partnner, adderss;
        public LocationAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.deletlocation);
            partnner=itemView.findViewById(R.id.ocationprattner);
            adderss=itemView.findViewById(R.id.address_location);


        }



    }
}
