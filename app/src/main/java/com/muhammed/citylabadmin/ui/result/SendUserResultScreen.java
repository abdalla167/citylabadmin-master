package com.muhammed.citylabadmin.ui.result;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.muhammed.citylabadmin.R;
import com.muhammed.citylabadmin.base.BaseFragment;
import com.muhammed.citylabadmin.databinding.FragmentSendUserResultScreenBinding;
import com.muhammed.citylabadmin.helper.AllToken;
import com.muhammed.citylabadmin.helper.FileData;
import com.muhammed.citylabadmin.helper.ImageResizer;
import com.muhammed.citylabadmin.helper.LoadingDialog;
import com.muhammed.citylabadmin.helper.NetworkState;
import com.muhammed.citylabadmin.helper.ResizJavaImage;
import com.muhammed.citylabadmin.ui.adapter.result.ResultFileClickListener;
import com.muhammed.citylabadmin.ui.adapter.result.ResultImageAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MultipartBody;


@AndroidEntryPoint
public class SendUserResultScreen extends BaseFragment
        implements PopupMenu.OnMenuItemClickListener, ResultFileClickListener {

    private FragmentSendUserResultScreenBinding binding;
    private ResultViewModel viewModel;

    private List<Image> images ;
    InputStream inputStream;
    ByteArrayOutputStream bytes;


    ResultImageAdapter adapter;
    RecyclerView.LayoutManager layoutManager;


    List<FileData> files = new ArrayList<>();
    String note = "";

    boolean pdfSelected = false;
    boolean imageSelected = false;

    String phone = "";
    String name = "";

    private void initRecycler() {
        adapter = new ResultImageAdapter(this);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        binding.resultImagesRecycler.setAdapter(adapter);
        binding.resultImagesRecycler.setLayoutManager(layoutManager);
    }


    public SendUserResultScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            phone = bundle.getString("phone");
            name = bundle.getString("name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_user_result_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentSendUserResultScreenBinding.bind(view);

        binding.toolbar.toolbarTitle.setText(getString(R.string.send_results));
        binding.userPhoneResult.setText(phone);
        binding.userNameResult.setText(name);


        viewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        initRecycler();

        binding.uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpMenu(v);

            }
        });
        observe();

        binding.uploadPdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!pdfSelected) {
                    if (checkStoragePermission(MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_PDF)) {
                        pdfIntent();
                    }
                } else {
                    files.clear();
                    binding.ln1.setVisibility(View.VISIBLE);
                    binding.uploadPdfBtn.setBackground(ContextCompat.getDrawable(requireContext(),
                            R.drawable.upload_btn_shape));
                    binding.uploadPdfBtn.setText(getResources().getString(R.string.download));
                    pdfSelected = false;
                }
            }
        });

        binding.sendResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendResult();
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
                cameraIntent();

                return true;
            case R.id.gallery_item:
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
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE_PDF:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pdfIntent();
                    return;
                }

                showToast("?????? ???????????? ?????????????? ?????????????? ?????????? ??????????????");

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
           images = ImagePicker.getImages(data);


            if (requestCode == REQUEST_GALLERY_CODE)
                onSelectFromGalleryResult(images);
            else if (requestCode == REQUEST_CAMERA_CODE)
                onCaptureImageResult(images.get(0));
            else
                onPdfSelected(data);


        }
    }

    private void onPdfSelected(Intent data) {
        try {
            inputStream = requireContext().getContentResolver().openInputStream(data.getData());
            byte[] b = getBytes(inputStream);
            String pdfBase64 = Base64.encodeToString(b, Base64.DEFAULT);
            files.add(new FileData(null, pdfBase64));

            binding.ln1.setVisibility(View.GONE);
            binding.uploadPdfBtn.setBackground(ContextCompat.getDrawable(requireContext(),
                    R.drawable.remove_btn_background));
            binding.uploadPdfBtn.setText("??????");
            pdfSelected = true;


        } catch (IOException e) {
            e.printStackTrace();
            showToast("?????? ????????");
        }


    }

    private void onSelectFromGalleryResult(List<Image> data) {
        Bitmap bm;
        if (data != null) {
            try {

            //    bm=ResizJavaImage.decodeFile(data.getData().getPath());
                for (Image image : data) {
                    bm = ResizJavaImage.decodeFile(image.getPath());
                    if (bytes == null)
                        bytes = new ByteArrayOutputStream();
                    bytes.reset();

                    bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    String sImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT).trim();

                    Log.d("TAG", "onSelectFromGalleryResult: " + bm.getWidth() + " " + bm.getHeight());
                    files.add(new FileData(bm, sImage));
                    adapter.addImage(files);
                }
                binding.ln2.setVisibility(View.GONE);
                binding.uploadImageBtn.setText(getResources().getString(R.string.add));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
    private void onCaptureImageResult(Image data) {
        if (data != null) {
            Bitmap thumbnail = ResizJavaImage.decodeFile(data.getPath());
            if (bytes == null)
                bytes = new ByteArrayOutputStream();
            bytes.reset();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);

            String sImage = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT).trim();

            //add image to adapter

            files.add(new FileData(thumbnail, sImage));
            adapter.addImage(files);

            binding.ln2.setVisibility(View.GONE);
            binding.uploadImageBtn.setText(getResources().getString(R.string.add));


        }

    }

    private void observe() {
        AllToken allToken=new AllToken(this.getContext());
        allToken.SetnewToken();
        viewModel.sendResultLiveData.observe(getViewLifecycleOwner(), new Observer<NetworkState>() {
            @Override
            public void onChanged(NetworkState networkState) {
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

    private void restAllWidgets() {
        files.clear();
        adapter.addImage(files);
        binding.resultNote.setText("");
        binding.userNameResult.setText("");
        binding.userPhoneResult.setText("");
        binding.ln2.setVisibility(View.VISIBLE);
        binding.ln1.setVisibility(View.VISIBLE);
        binding.uploadImageBtn.setText(getResources().getString(R.string.download));
        binding.uploadPdfBtn.setBackground(ContextCompat.getDrawable(requireContext(),
                R.drawable.upload_btn_shape));
        binding.uploadPdfBtn.setText(getResources().getString(R.string.download));
        pdfSelected = false;
    }

    @Override
    public void removeFile(int pos) {
        files.remove(pos);
        adapter.addImage(files);

        if (files.size() <= 0) {
            binding.ln2.setVisibility(View.VISIBLE);
            binding.uploadImageBtn.setText(getResources().getString(R.string.download));
        }

    }

    private void sendResult() {
        String phone = binding.userPhoneResult.getText().toString();
        if (phone.isEmpty()) {
            Toast.makeText(requireContext(), "???????? ????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (files.size() == 0) {
            Toast.makeText(requireContext(), "???????? ??????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!binding.resultNote.getText().toString().isEmpty()) {
            note = binding.resultNote.getText().toString();
        }

        List<String> base64 = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            base64.add(files.get(i).getBase64());
            Log.d("TAG", "sendResult: "+files.get(i).getBase64().length());
        }
        viewModel.sendResult(base64, phone, note);


    }


    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

}