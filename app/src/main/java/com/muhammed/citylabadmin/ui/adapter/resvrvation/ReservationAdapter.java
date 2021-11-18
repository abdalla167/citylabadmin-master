package com.muhammed.citylabadmin.ui.adapter.resvrvation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.data.model.general.SimpleResponse;
import com.muhammed.citylabadmin.data.model.reservation.Booking;
import com.muhammed.citylabadmin.data.model.reservation.Datum;
import com.muhammed.citylabadmin.data.model.user.User;
import com.muhammed.citylabadmin.di.RetrofitClint;
import com.muhammed.citylabadmin.helper.AllToken;
import com.muhammed.citylabadmin.helper.FileData;
import com.muhammed.citylabadmin.helper.MyPreference;
import com.muhammed.citylabadmin.service.RetrofitService;
import com.muhammed.citylabadmin.ui.adapter.result.ResultFileClickListener;
import com.muhammed.citylabadmin.ui.adapter.result.ResultImageAdapter;
import com.ortiz.touchview.TouchImageView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReservationAdapter  extends RecyclerView.Adapter<ReservationAdapter.ReservationHolder> {
    float scaletr=1.0f;
    ScaleGestureDetector saScaleGestureDetector;
    ImageView imaged;
 Context context;
    RetrofitService retrofitService;
     List<Datum> reserv = new ArrayList<>();
    public void addreserv(List<Datum> reserv ) {
        this.reserv = reserv;
        notifyDataSetChanged();
    }
    @Inject
    public  ReservationAdapter(Context context,RetrofitService retrofitService) {
     this.retrofitService=retrofitService;
        this.context=context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReservationAdapter.ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationAdapter.ReservationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationAdapter.ReservationHolder holder, int position) {
        if(reserv.get(position).getmName()!=null && reserv.get(position).getmPhoneNumber()!=null) {

            holder.name.setText(reserv.get(position).getmName());
            holder.phone.setText(reserv.get(position).getmPhoneNumber());
            holder.age.setText("الفرع : "+reserv.get(position).getmAge());
            holder.date.setText(reserv.get(position).getmReservationDate().split(" ")[0]);
            if (reserv.get(position).getmType() == 1) {
                holder.type.setText("حجز منزلي");
                if (reserv.get(position).getmBuildingNo()==null) {
                    holder.address.setText(reserv.get(position).getmAddress().toString());
                }
                if(reserv.get(position).getmBuildingNo()!=null)
                {    holder.address.setText(reserv.get(position).getmAddress().toString() + "\n" + reserv.get(position).getmBuildingNo().toString());
            }
            }
            if(reserv.get(position).getmFile().equals(""))
            {
                holder.image_test.setImageResource(R.drawable.defult);
            }
            else {
                Glide.with(context).load("http://" + reserv.get(position).getmFile())
                        .into(holder.image_test);
                holder.dowenload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "يتم بداء التحميل الان", Toast.LENGTH_SHORT).show();
                        @SuppressLint({"NewApi", "LocalSuppress"}) DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://" + reserv.get(position).getmFile()));
                        request.setTitle("resevation");
                        request.setDescription("تحميل الملف برجاء الانتظار .......");
                        String cookie = CookieManager.getInstance().getCookie("http://" + reserv.get(position).getmFile());
                        request.addRequestHeader("cookie", cookie);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,".png");
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
                        downloadManager.enqueue(request);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (reserv!=null)
        return reserv.size();
        else
            return 0;
    }

    public  class ReservationHolder extends RecyclerView.ViewHolder {
TouchImageView image_test;
            ImageView imageView_delete,dowenload;
            TextView name,age,phone,date,address,type;
ConstraintLayout linearLayout;
        public ReservationHolder(@NonNull View itemView) {
                super(itemView);
                linearLayout=itemView.findViewById(R.id.number_call);
                name=itemView.findViewById(R.id.name_id_user_reservation);
                image_test=itemView.findViewById(R.id.image_id_item_resevation);
                age=itemView.findViewById(R.id.age_id_user_reservation);
                phone=itemView.findViewById(R.id.phone_user_reservation);
                date=itemView.findViewById(R.id.day_id_user_reservation);
                address=itemView.findViewById(R.id.address_id_user_reservation);
                type=itemView.findViewById(R.id.type_id_user_reservation);
                dowenload=itemView.findViewById(R.id.downloadImage);
            imageView_delete=itemView.findViewById(R.id.remove_icon_reservation);
            image_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom_image_reservation);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    imaged =dialog.findViewById(R.id.image_show_reservation);

// Include dialog.xml file
                    dialog.show();      // Include dialog.xml file
                    Glide.with(context).load("http://"+reserv.get(getAdapterPosition()).getmFile())
                            .into(imaged);



                }
            });




            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("اتصال");
                    alertDialog.setMessage("هل تريد محادثه هذا الشخص");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" +   reserv.get(getAdapterPosition()).getmPhoneNumber()));

                                    if (Build.VERSION.SDK_INT > 23) {
                                        context.startActivity(intent);
                                    } else {

                                        if (ActivityCompat.checkSelfPermission(context,
                                                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            Toast.makeText(context, "Permission Not Granted ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                                            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS_STORAGE, 9);
                                           context.startActivity(intent);
                                        }
                                    }
                                    alertDialog.dismiss();
                                }
                            });
                    alertDialog.show();



                }
            });
            imageView_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    imageView_delete.setEnabled(false);
                    AllToken allToken=new AllToken(context);
                    allToken.SetnewToken();
                    RetrofitClint.getInstance().delete_reservation(MyPreference.getSharedString(MyPreference.SHARED_USER_TOKEN),
                            reserv.get(getAdapterPosition()).getmReservationId()).enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            if(response.isSuccessful())
                            {
                                reserv.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());

                            }
                        }

                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {


                        }
                    });
                    imageView_delete.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageView_delete.setEnabled(true);
                        }
                    }, 2000);

                }
            });
        }
    }

    private class Scallister extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaletr*=saScaleGestureDetector.getScaleFactor();
            imaged.setScaleX(scaletr);
            imaged.setScaleY(scaletr);
            return true;
        }

    }

}
