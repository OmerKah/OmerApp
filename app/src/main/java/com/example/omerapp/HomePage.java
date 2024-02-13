package com.example.omerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity implements View.OnClickListener {
    private ImageView pfp;
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private Button btnReport;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        pfp = findViewById(R.id.profilePic);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        btnReport = findViewById(R.id.btnReport);
        btnReport.setOnClickListener(this);


        database.child("users").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                Glide.with(HomePage.this)
                        .load(currentUser.getImage())
                        .circleCrop()
                        .into(pfp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnReport.getId())
        {
            Intent intent = new Intent(HomePage.this, EnvHazardActivity1.class);
            intent.putExtra("currentUser",currentUser);
            startActivity(intent);
        }
    }
}