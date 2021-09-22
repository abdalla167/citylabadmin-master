package com.muhammed.citylabadmin.ui.adapter.offer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.data.model.offer.AllOffer;
import com.muhammed.citylabadmin.data.model.general.SimpleResponse;
import com.muhammed.citylabadmin.di.RetrofitClint;
import com.muhammed.citylabadmin.helper.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferUpdata extends RecyclerView.Adapter<OfferUpdata.OfferUpdataholder> {

    OfferOnClick offerOnClick;
    Context context;
    public AllOffer allOffers;

    public OfferUpdata(Context context, OfferOnClick offerOnClick)
    {
        this.offerOnClick=offerOnClick;
        this.context = context;
    }

    public void setAllOffers(AllOffer allOffers) {
        this.allOffers = allOffers;

    }


    @NonNull
    @Override
    public OfferUpdataholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer_update, parent, false);
        return new OfferUpdata.OfferUpdataholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferUpdataholder holder, int position) {
        holder.titel_offer.setText(allOffers.getData().get(position).getTitle() + " ");
        holder.delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.delet.setEnabled(false);

                RetrofitClint.getInstance().delete_offer(MyPreference.getSharedString(MyPreference.SHARED_USER_TOKEN),Integer.parseInt(String.valueOf(allOffers.getData().get(position).getOfferId()))).enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        if(response.isSuccessful())
                        allOffers.getData().remove(position);
                        notifyItemRemoved(position);
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {

                    }
                });
                holder.delet.setEnabled(true);
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerOnClick.update_offer(allOffers.getData().get(position));
            }
        });

    }


    @Override
    public int getItemCount() {
        if (allOffers.getData().size() > 0)
            return allOffers.getData().size();
        else
            return 0;
    }

    public static class OfferUpdataholder extends RecyclerView.ViewHolder {
        Button delet, update;
        TextView titel_offer;

        public OfferUpdataholder(@NonNull View itemView) {
            super(itemView);
            delet = itemView.findViewById(R.id.delet_offer);
            update = itemView.findViewById(R.id.updata_offer);

            titel_offer = itemView.findViewById(R.id.titel_offer);

        }


    }
}