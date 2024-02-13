package com.example.omerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText Email;
    private EditText Pass;
    private EditText Phone;
    private EditText Name;
    private Button Register;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private String currentPhotoPath = "";
    private ImageView ivImage;
    private ImageButton imgBtn;
    Bitmap bitmap;
    private StorageReference storageRef;
    private UploadTask uploadTask;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Email = findViewById(R.id.etEmail);
        Pass = findViewById(R.id.etPass);
        Phone = findViewById(R.id.etPhone);
        Name = findViewById(R.id.etName);
        Register = findViewById(R.id.regBtn);
        Register.setOnClickListener(this);
        ivImage = findViewById(R.id.ivImage);
        imgBtn = findViewById(R.id.imgbtnPfp);
        imgBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        storageRef = FirebaseStorage.getInstance().getReference();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == Register.getId()) {
            createAccount(Email.getText().toString(),Pass.getText().toString());

        }
        if(v.getId() == imgBtn.getId())
        {
            dispatchTakePictureIntent();
        }

    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User userinfo;
                            if(downloadUri == null)
                            {
                                userinfo = new User(user.getUid(),Name.getText().toString(),Email.getText().toString(),Phone.getText().toString(),"");
                            }
                            else {
                                userinfo = new User(user.getUid(),Name.getText().toString(),Email.getText().toString(),Phone.getText().toString(),downloadUri.toString());
                            }

                            database.child("users").child(userinfo.getId()).setValue(userinfo);
                            Intent intent = new Intent(Register.this,HomePage.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END create_user_with_email]
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            /*Bundle extras = data.getExtras();
            Bitmap imagebitmap = (Bitmap)extras.get("data");
            Bitmap scaledbitmap =Bitmap.createScaledBitmap(imagebitmap,ivImage.getWidth(),ivImage.getHeight(),false);
            ivImage.setImageBitmap(scaledbitmap);*/
            setPic();
            uploadFullImage();
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
    private void setPic() {
        // Get the dimensions of the View
        int targetW = ivImage.getWidth();
        int targetH = ivImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        ivImage.setImageBitmap(bitmap);

    }
    private void uploadFullImage()
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
                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }
}