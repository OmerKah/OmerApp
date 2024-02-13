package com.example.omerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EnvHazardActivity1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner spHazardType;
    private Spinner spUrgencyHazard;
    private EditText etTitle;
    private String hazType = "אחר";
    private String hazUrgency = "לא דחוף";
    private String img1, img2, img3;
    private int kdot = 0;
    private ImageView hazImg1,hazImg2,hazImg3;
    private String currentPhotoPath = "";
    Bitmap bitmap;
    private StorageReference storageRef;
    private UploadTask uploadTask;
    private Uri downloadUri;
    private String img1Url = "",img2Url = "",img3Url = "";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private DatabaseReference database;
    private User currentUser;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env_hazard1);

        currentUser = (User)getIntent().getSerializableExtra("currentUser");
        spHazardType = findViewById(R.id.spHazardType);
        spUrgencyHazard = findViewById(R.id.spUrgencyHazard);
        hazImg1 = findViewById(R.id.hazImg1);
        hazImg2 = findViewById(R.id.hazImg2);
        hazImg3 = findViewById(R.id.hazImg3);
        etTitle = findViewById(R.id.etTitle);
        hazImg1.setOnClickListener(this);
        hazImg2.setOnClickListener(this);
        hazImg3.setOnClickListener(this);
        saveBtn = findViewById(R.id.btnSendReport);
        saveBtn.setOnClickListener(this);
        storageRef = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance().getReference();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.hazards_array,
                android.R.layout.simple_spinner_item
        );
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this,
                R.array.urgency_hazards,
                android.R.layout.simple_spinner_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spHazardType.setAdapter(adapter);
        spHazardType.setOnItemSelectedListener(this);
        spUrgencyHazard.setAdapter(adapter2);
        spUrgencyHazard.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spHazardType) {
            hazType = parent.getItemAtPosition(position).toString();
        } else {
            hazUrgency = parent.getItemAtPosition(position).toString();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == hazImg1.getId()) {
            dispatchTakePictureIntent();


        } else if (v.getId() == hazImg2.getId()) {
            dispatchTakePictureIntent();


        } else if (v.getId() == hazImg3.getId()) {
            dispatchTakePictureIntent();
        }

        if(v.getId() == saveBtn.getId())
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            EnvHazard hazard = new EnvHazard(currentUser.getId()+System.currentTimeMillis(),etTitle.getText().toString(),hazType,hazUrgency,currentDateandTime,0.0,0.0,currentUser.getId(),currentUser.getName(),currentUser.getPhone(),img1Url,img2Url,img3Url,0);
            database.child("users").child(currentUser.getId()).child("hazards").child(hazard.getId()).setValue(hazard);
            database.child("hazards").child(hazard.getId()).setValue(hazard);
            Intent intent = new Intent(EnvHazardActivity1.this,HomePage.class);
            startActivity(intent);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.omerapp",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic(ImageButton imgBtn) {
        // Get the dimensions of the View
        int targetW = imgBtn.getWidth();
        int targetH = imgBtn.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imgBtn.setImageBitmap(bitmap);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imagebitmap = (Bitmap)extras.get("data");
            Bitmap scaledbitmap =Bitmap.createScaledBitmap(imagebitmap,ivImage.getWidth(),ivImage.getHeight(),false);
            ivImage.setImageBitmap(scaledbitmap);*/
            switch (kdot) {
                case 0:
                    kdot = 1;
                    hazImg1.setClickable(false);
                    hazImg2.setVisibility(View.VISIBLE);
                    uploadFullImage(1);
                    break;
                case 1:
                    hazImg2.setClickable(false);
                    kdot = 2;
                    hazImg3.setVisibility(View.VISIBLE);
                    uploadFullImage(2);
                    break;
                case 2:
                    hazImg3.setClickable(false);
                    uploadFullImage(3);
                    break;
            }


        }
    }
    private void uploadFullImage(int imgNum)
    {
        Uri file = Uri.fromFile(new File(currentPhotoPath));
        final StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        uploadTask = riversRef.putFile(file);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    downloadUri = task.getResult();
                    if(imgNum == 1)
                    {
                        img1Url = downloadUri.toString();
                        Glide.with(EnvHazardActivity1.this)
                                .load(img1Url)
                                .fitCenter()
                                .into(hazImg1);
                    }
                    else if(imgNum == 2)
                    {
                        img2Url = downloadUri.toString();
                        Glide.with(EnvHazardActivity1.this)
                                .load(img2Url)
                                .fitCenter()
                                .into(hazImg2);
                    }
                    else if (imgNum == 3){
                        img3Url = downloadUri.toString();
                        Glide.with(EnvHazardActivity1.this)
                                .load(img3Url)
                                .fitCenter()
                                .into(hazImg3);
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
}