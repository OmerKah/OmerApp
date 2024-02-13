package com.example.omerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Button LoginBtn;
    private EditText Email;
    private EditText Pass;
    private TextView Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        LoginBtn = findViewById(R.id.btnLogin);
        Email = findViewById(R.id.etEmail);
        Pass = findViewById(R.id.etPass);
        LoginBtn.setOnClickListener(this);
        Register = findViewById(R.id.registerTv);
        Register.setOnClickListener(this);


    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MainActivity", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this,HomePage.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MainActivity", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END sign_in_with_email]
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if( mAuth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this,HomePage.class);
            startActivity(intent);
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == LoginBtn.getId()) {
            signIn(Email.getText().toString(), Pass.getText().toString());
        }
        if (v.getId() == Register.getId()) {
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }

    }
}