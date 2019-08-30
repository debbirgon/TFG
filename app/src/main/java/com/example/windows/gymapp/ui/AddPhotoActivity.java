package com.example.windows.gymapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.windows.gymapp.R;
import com.example.windows.gymapp.data.StorageSP;
import com.example.windows.gymapp.model.Image;
import com.example.windows.gymapp.model.User;
import com.example.windows.gymapp.util.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AddPhotoActivity extends AppCompatActivity {

    private AppCompatImageView ic_back, iv_photo;
    private Button btn_choose, btn_add_photo;
    private Uri imageUri;
    private ProgressBar progressBar;
    private StorageReference imagesReference;
    private StorageTask storageTask;
    private DatabaseReference imagesDbReference;
    private Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        ic_back = findViewById(R.id.ic_back);
        iv_photo = findViewById(R.id.iv_photo);
        btn_choose = findViewById(R.id.btn_choose);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        progressBar = findViewById(R.id.progress_bar);

        image = new Image();
        User user = new StorageSP(this).getUser();

        imagesReference = FirebaseStorage.getInstance().getReference(Constants.IMAGES).child(user.getId());
        imagesDbReference = FirebaseDatabase.getInstance().getReference(Constants.IMAGES).child(user.getId());

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFile();
            }
        });

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storageTask!=null && storageTask.isInProgress()){
                    Toast.makeText(getApplicationContext(), getString(R.string.upload_in_progress), Toast.LENGTH_SHORT).show();
                }else {
                    uploadPhoto();
                }
            }
        });
    }

    private void uploadPhoto() {
        String name = String.valueOf(System.currentTimeMillis());
        image.setName(name);
        storageTask = imagesReference.child(name).putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                image.setUrl(urlTask.getResult().toString());

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },500);

                String key = imagesDbReference.push().getKey();
                imagesDbReference.child(key).setValue(image);
                Toast.makeText(getApplicationContext(), getString(R.string.upload_successful), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.onFaiulre), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            }
        });
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data!=null && data.getData()!=null){

            imageUri = data.getData();
            Picasso.get().load(imageUri).into(iv_photo);
            btn_add_photo.setVisibility(View.VISIBLE);
        }
    }
}
