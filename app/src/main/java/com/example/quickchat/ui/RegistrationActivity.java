package com.example.quickchat.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.quickchat.model.Users;
import com.example.quickchat.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    private FirebaseAuth auth;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Uri uriimage;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageUri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         progressDialog = new ProgressDialog(this);
         progressDialog.setMessage("Please Wait...");
         progressDialog.setCancelable(false);

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        binding.btSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = binding.regName.getText().toString().trim();
                String email = binding.regEmail.getText().toString().trim();
                String password = binding.regPassword.getText().toString().trim();
                String conf_password = binding.regConfirmPassword.getText().toString();
                String status = "Hey , I'm using QuickChat...";


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(conf_password)) {
                     progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Please enter all ", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    binding.regEmail.setError("Enter valid email");
                    Toast.makeText(RegistrationActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(conf_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Passwors does not equal", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password must bigger 5", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                DatabaseReference databaseReferencere = database.getReference().child("users").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("uplod").child(auth.getUid());

                                if (uriimage != null) {
                                    storageReference.putFile(uriimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri = uri.toString();
                                                        Users users = new Users(auth.getUid(), name, email, imageUri,status);
                                                        databaseReferencere.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                                                } else {
                                                                    Toast.makeText(RegistrationActivity.this, "Error in Creating a New User", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else if (uriimage ==null || imageUri.isEmpty()){
                                    String status = "Hey , I'm using QuickChat...";
                                    imageUri = "https://static.vecteezy.com/system/resources/previews/002/387/693/non_2x/user-profile-icon-free-vector.jpg";
                                    Users users = new Users(auth.getUid(), name, email, imageUri,status);
                                    databaseReferencere.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                                            } else {
                                                Toast.makeText(RegistrationActivity.this, "Error in Creating a New User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Something error:"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        binding.profilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (data != null) {
                uriimage = data.getData();
                binding.profilImageView.setImageURI(uriimage);
            }
        }
    }

    public void onClickSingIn(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }
}