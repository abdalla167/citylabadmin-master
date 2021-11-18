package com.muhammed.citylabadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.muhammed.citylabadmin.data.model.medical.MedicalInformationModel;

public class MedicalInformation extends AppCompatActivity {
ImageView imageView;
Button button;
EditText hideline,content;
    Uri imageUri;
    ProgressBar progressBar;
    StorageReference mStorageRef ;
    DatabaseReference mDatabaseref;
    FirebaseDatabase database;
    private static final int PICK_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        button=findViewById(R.id.upload_information);
        imageView=findViewById(R.id.add_information_image);
        hideline=findViewById(R.id.hid_lin_infor);
        content=findViewById(R.id.content_medical_information);
        progressBar=findViewById(R.id.progressbiggroupid);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        mDatabaseref = database.getReference();
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    });
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(hideline.getText().toString().equals("")||content.getText().toString().equals("")||imageUri.equals(""))
        {
            Toast.makeText(MedicalInformation.this, "الرجاء ملئ البيانات", Toast.LENGTH_SHORT).show();
        }
        else
            {
                MedicalInformationModel medicalInformationModel=new MedicalInformationModel();
                medicalInformationModel.setContent(content.getText().toString());
                medicalInformationModel.setHide(hideline.getText().toString());
                uploadGroupdata(medicalInformationModel,imageUri,progressBar,getApplication(),mStorageRef,mDatabaseref);
            }
    }
});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    public  void uploadGroupdata (MedicalInformationModel modle, Uri imageUri, ProgressBar mProgressBar, Context activity, StorageReference mStorageRef, DatabaseReference mDatabaseref )
    {
        // Sign in success, update UI with the signed-in user's information
        final StorageReference photoRef  = mStorageRef.child("medicalinformation").child(imageUri.toString());
        photoRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Download file From Firebase Storage

                        photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadPhotoUrl) {
                                //Now play with downloadPhotoUrl
                                //Store data into Firebase Realtime Database
                                Thread spalsh = new Thread() {
                                    public void run() {
                                        try {
                                            sleep(3000);
                                            mProgressBar.setProgress(0);

                                        } catch (Exception e) {
                                        }
                                    }
                                };

                                spalsh.start();
                                modle.setImage(downloadPhotoUrl.toString());
                                //   modle.setId();
                                mDatabaseref.child("medical").push().setValue(modle);
                                Intent i = new Intent(activity,MainActivity.class);
                                activity.startActivity(i);
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                mProgressBar.setProgress((int) progress);

            }
        });


    }
    public void gf(){}
}