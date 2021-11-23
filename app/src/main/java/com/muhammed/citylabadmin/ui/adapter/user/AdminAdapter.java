package com.muhammed.citylabadmin.ui.adapter.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.data.model.admin.AdminModle;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder2> {
    Context mContext;
    ArrayList<AdminModle> adminModles=new ArrayList<>();

    @NonNull
    @Override
    public AdminAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin,parent,false);
        return new AdminAdapter.ViewHolder2(view);
    }

    public AdminAdapter( Context mContext,ArrayList<AdminModle> adminModles) {

        this.mContext = mContext;
        this.adminModles=adminModles;
    }

    public AdminAdapter() {
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.ViewHolder2 holder, int position) {
       holder.name.setText(adminModles.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(adminModles.size()>0)
            return adminModles.size();
        else
            return 0;
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder
    {

        TextView name;
        Button delet;
        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name_admin);
            delet=itemView.findViewById(R.id.deleteadminlist);
           delet.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   delet.setClickable(false);
                   DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                   Query applesQuery = ref.child("admin").orderByChild("name").equalTo(adminModles.get(getAdapterPosition()).getName().toString().trim())
                           ;

                   applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                               appleSnapshot.getRef().removeValue();
                           }
                           Toast.makeText(mContext, "تم مسح admin", Toast.LENGTH_SHORT).show();
                       }
                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                       }
                   });
adminModles.remove(getAdapterPosition());
                   notifyItemRemoved(getAdapterPosition());
                   delet.setClickable(true);

               }
           });
        }
    }


}

