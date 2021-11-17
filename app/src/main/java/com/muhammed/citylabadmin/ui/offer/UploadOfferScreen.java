package com.muhammed.citylabadmin.ui.offer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.SplashScreen;
import com.muhammed.citylabadmin.base.BaseFragment;
import com.muhammed.citylabadmin.data.model.offer.Datum;
import com.muhammed.citylabadmin.data.model.general.SimpleResponse;
import com.muhammed.citylabadmin.databinding.FragmentUploadOfferScreenBinding;
import com.muhammed.citylabadmin.di.RetrofitClint;
import com.muhammed.citylabadmin.helper.AllToken;
import com.muhammed.citylabadmin.helper.LoadingDialog;
import com.muhammed.citylabadmin.helper.MyPreference;
import com.muhammed.citylabadmin.helper.NetworkState;
import com.muhammed.citylabadmin.helper.ResizJavaImage;
import com.muhammed.citylabadmin.helper.Utile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@AndroidEntryPoint
public class UploadOfferScreen extends BaseFragment implements PopupMenu.OnMenuItemClickListener {

    private OfferViewModel viewModel;
    private FragmentUploadOfferScreenBinding binding;

    private String imageBase64;
    public Datum datum=new Datum();
    InputStream inputStream;
    ByteArrayOutputStream bytes;
    private List<Image> images;
    String startDateOfferUpdate;
    String endDateOfferUpdate;
    final Calendar myCalendar = Calendar.getInstance();
    private Date startOfferDate;
    private Date endOfferDate;

    DatePickerDialog.OnDateSetListener startDateSetListener;
    DatePickerDialog.OnDateSetListener endDateSetListener;



    private SimpleDateFormat getDateFromString(String date) {

        String myFormat = "MM/dd/yy"; //In which you need put here
        return new SimpleDateFormat(myFormat, Locale.US);
    }

    String sOffDate = "";
    String eOffDate = "";



    private void initDateDialog() {
        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                binding.startOfferDate.setText(( dayOfMonth) + "/" + month + "/" + year);
                 if (dayOfMonth>1)
                     dayOfMonth=dayOfMonth-1;
                    sOffDate =( dayOfMonth) + "/" + month + "/" + year;

            }
        };
        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                eOffDate = dayOfMonth + "/" + month + "/" + year;
                binding.endOfferDate.setText(eOffDate);

            }
        };
    }


    public UploadOfferScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            String personJsonString = args.getString("object_offer");
            datum= Utile.getGsonParser().fromJson(personJsonString, Datum.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload_offer_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentUploadOfferScreenBinding.bind(view);
        binding.toolbar.toolbarTitle.setText(getString(R.string.send_offers));

        viewModel = new ViewModelProvider(this).get(OfferViewModel.class);
        initDateDialog();
        if(SplashScreen.stat==1) {
            binding.offerTitle.setText(datum.getTitle());
            binding.offerNote.setText(datum.getDescription());
            binding.startOfferDate.setText(datum.getStartTime().toString().split(" ")[0]);
            binding.endOfferDate.setText(datum.getEndTime().split(" ")[0]);
            binding.newOfferPrice.setText(datum.getCurrentPrice().toString());
            binding.oldOfferPrice.setText(datum.getPreviousPrice().toString());

            Glide.with(this)
                    .asBitmap()
                    .load("http://"+datum.getFiles().get(0))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.offerImage.setImageBitmap(resource);


                            if (bytes == null)
                                bytes = new ByteArrayOutputStream();
                            bytes.reset();

                            resource.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                            imageBase64 = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT).trim();

                            binding.offerImage.setImageBitmap(resource);


                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });


            binding.removeIcon.setVisibility(View.VISIBLE);
        }

        DateFormat dffrom = new SimpleDateFormat("M/dd/yyyy");
        DateFormat dfto = new SimpleDateFormat("yyyy-MM-dd");
        Date today = null;
        try {
            today = dffrom.parse("7/1/2011");
        } catch (ParseException exception) {
            exception.printStackTrace();
        }
        String s = dfto.format(today);

        Log.d("dddddd", "onViewCreated: " + s);


        binding.startDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                Locale locale = getResources().getConfiguration().locale.ENGLISH;
                Locale.setDefault(locale);
                DatePickerDialog dialog = new DatePickerDialog(
                        requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        binding.endDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                Locale locale = getResources().getConfiguration().locale.ENGLISH;
                Locale.setDefault(locale);
                DatePickerDialog dialog = new DatePickerDialog(
                        requireContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        endDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        binding.uploadOfferImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpMenu(v);

            }
        });
        binding.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.offerImage.setImageResource(R.drawable.ic_camera);
                binding.removeIcon.setVisibility(View.GONE);
                imageBase64=null;
            }
        });
        binding.uploadOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bytes == null) {
                    try {
                        if (inputStream != null)
                            uploadOfferData(getBytes(inputStream), SplashScreen.stat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else uploadOfferData(bytes.toByteArray(),SplashScreen.stat);
            }
        });

        observe();

    }

    public void restAllWidgets() {
        binding.startOfferDate.setText(getResources().getString(R.string.start_date));
        binding.endOfferDate.setText(getResources().getString(R.string.end_date));
        binding.oldOfferPrice.setText("");
        binding.newOfferPrice.setText("");
        binding.offerImage.setImageResource(R.drawable.ic_camera);
        binding.offerNote.setText("");
        binding.offerTitle.setText("");
        binding.removeIcon.setVisibility(View.GONE);
        imageBase64 = null;
        sOffDate = "";
        eOffDate = "";

    }

    public void observe() {
        viewModel.addOfferLiveData.observe(getViewLifecycleOwner(), new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
                Log.d("dddddd", "onChanged: " + networkState.message);
                switch (networkState.status) {
                    case SUCCESS:
                        LoadingDialog.hideDialog();
                        restAllWidgets();
                        Toast.makeText(requireContext(), "" + networkState.data.toString(),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case FAILED:
                        LoadingDialog.hideDialog();
                        Toast.makeText(requireContext(), "" + networkState.message.toString(),
                                Toast.LENGTH_SHORT).show();
                        break;

                    default:
                            LoadingDialog.showDialog(requireActivity());
                        break;


                }

            }
        });
    }

    private void popUpMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.file_type_items);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.camera_item:
                if (checkStoragePermission(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CAMERA))
                    cameraIntent();
                return true;
            case R.id.gallery_item:
                if (checkStoragePermission(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_GALLERY))
                    galleryIntent();
                return true;
        }


        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                    return;
                }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                    return;
                }

                showToast("يجب السماح بالوصول للملفات لتمام العملية");

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            images  = ImagePicker.getImages(data);
            if (requestCode == REQUEST_GALLERY_CODE)
                onSelectFromGalleryResult(images.get(0));
            else if (requestCode == REQUEST_CAMERA_CODE)
                onCaptureImageResult(images.get(0));

        }
    }


    private void onSelectFromGalleryResult(Image data) {
        Bitmap bm;
        if (data != null) {
            try {
                bm = ResizJavaImage.decodeFile(data.getPath());
                if (bytes == null)
                    bytes = new ByteArrayOutputStream();
                bytes.reset();

                bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                imageBase64 = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT).trim();

                binding.offerImage.setImageBitmap(bm);
                binding.removeIcon.setVisibility(View.VISIBLE);
                //  inputStream = requireContext().getContentResolver().openInputStream(data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void onCaptureImageResult(Image data) {
        if (data != null) {
            Bitmap thumbnail = ResizJavaImage.decodeFile(data.getPath());
            bytes = new ByteArrayOutputStream();
            if (bytes == null)
                bytes = new ByteArrayOutputStream();
            bytes.reset();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            imageBase64 = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT).trim();


            binding.offerImage.setImageBitmap(thumbnail);
            binding.removeIcon.setVisibility(View.VISIBLE);


        }

    }

    private void uploadImage(byte[] imageBytes) {
        if (imageBytes == null)
            showToast("يجب اختيار صورة");
        else {
            RequestBody requestFile = RequestBody.create(imageBytes, MediaType.parse("image/jpeg"));
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg",
                    requestFile);
        }
        //upload image viewModel
    }


    private void uploadOfferData(byte[] imageBytes,int stat) {



            String title = Objects.requireNonNull(binding.offerTitle.getText()).toString().trim();
            String startDate = sOffDate;
            String endDate = eOffDate;
            String startPrice = Objects.requireNonNull(binding.oldOfferPrice.getText()).toString().trim();
            String endPrice = Objects.requireNonNull(binding.newOfferPrice.getText()).toString().trim();
            String desc = Objects.requireNonNull(binding.offerNote.getText()).toString().trim();
            ///////////////////////////////

            if(stat==0) {

                if (title.isEmpty() || startDate.isEmpty() || endDate.isEmpty() ||
                        startPrice.isEmpty() || endPrice.isEmpty() || desc.isEmpty()) {
                    showToast("ادخل البيانات كاملة");
                    return;
                }
            }

            if (imageBase64 == null) {
                showToast("يجب اختيار صورة");
                if (Double.parseDouble(startPrice) > Double.parseDouble(endPrice)) {
                    showToast("خطاء في الاسعار");
                }
            } else {

                if(stat==1)
                {
                    binding.progressupload.setVisibility(View.VISIBLE);
                    datum.setTitle( Objects.requireNonNull(binding.offerTitle.getText()).toString().trim());
                    if(!sOffDate.equals(""))
                    {

                        datum.setStartTime(sOffDate);
                    }
                    if (!eOffDate.equals(""))
                    {
                        datum.setEndTime(eOffDate);
                    }
                    datum.setPreviousPrice(Long.valueOf(Objects.requireNonNull(binding.oldOfferPrice.getText()).toString().trim()));
                    datum.setCurrentPrice(Long.valueOf(Objects.requireNonNull(binding.newOfferPrice.getText()).toString().trim()));
                    datum.setDescription(Objects.requireNonNull(binding.offerNote.getText()).toString().trim());
                    List<String>file=new ArrayList<>();
                    file.add(imageBase64);
                    datum.setFiles(file);

                    AllToken allToken=new AllToken(this.getContext());
                    allToken.SetnewToken();

                    RetrofitClint.getInstance().
                            updateOffer(MyPreference.getSharedString(MyPreference.SHARED_USER_TOKEN),Integer.parseInt(String.valueOf(datum.getOfferId())),
                                    imageBase64,datum.getTitle(),datum.getDescription(),datum.getStartTime().split(" ")[0].toString(),datum.getEndTime().split(" ")[0],Double.parseDouble(String.valueOf(datum.getPreviousPrice())) ,Double.parseDouble(String.valueOf(datum.getCurrentPrice()))).
                            enqueue(new Callback<SimpleResponse>() {
                        @Override
                        public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                            Log.d("TAG", "onResponse "+response.body().getMessage());
                            binding.progressupload.setVisibility(View.GONE);

                        }
                        @Override
                        public void onFailure(Call<SimpleResponse> call, Throwable t) {
                            Log.d("TAG", "onResponse+up: "+t.getMessage());
                            binding.progressupload.setVisibility(View.GONE);
                        }
                    });
                }
                else {
                    SplashScreen.stat=0;
                    AllToken allToken=new AllToken(this.getContext());
                    allToken.SetnewToken();
                    viewModel.addOffer(imageBase64, title, desc, startDate, endDate,
                            Double.parseDouble(startPrice), Double.parseDouble(endPrice));
                }
            }


    }


}