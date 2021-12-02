package com.example.quickchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
EditText editTextEmail;
EditText editTextPassword;
    private FirebaseAuth auth;
    TextView buttonSinIn;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.login_email);
        editTextPassword = findViewById(R.id.login_password);
        buttonSinIn = findViewById(R.id.btSingIn);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        }

        buttonSinIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Write all stroke", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    editTextEmail.setError("Invalid email");
                    Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    editTextPassword.setError("Invalid password");
                    Toast.makeText(LoginActivity.this, "Please enter valid password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Error in login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    public void onClickSingUp(View view) {
    startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }
}