package com.example.quickchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quickchat.R;
import com.example.quickchat.adapter.UserAdapter;
import com.example.quickchat.databinding.ActivityHomeBinding;
import com.example.quickchat.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private RecyclerView recyclerViewUser;
    private UserAdapter adapter;
    private FirebaseDatabase database;
    private List<Users> usersList;
    private ActivityHomeBinding binding;
    ImageView imageViewLogOut;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        initRecycler();
        imageViewLogOut = findViewById(R.id.image_logOut);
        layout = findViewById(R.id.gotochat);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,GroupActivity.class));
            }
        });


        DatabaseReference databaseReference = database.getReference().child("users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    usersList.add(users);
                    adapter.setUsersList(usersList);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imageViewLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.diolog_layout);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                dialog.show();
                TextView textViewNo = dialog.findViewById(R.id.btnNo);
                TextView textViewYes = dialog.findViewById(R.id.btnYes);
                textViewNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                textViewYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.signOut();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                });
            }
        });

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
        }

    }

    private void initRecycler() {
        recyclerViewUser = findViewById(R.id.userRecycler);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(this));
        usersList = new ArrayList<>();
        adapter = new UserAdapter(this);
        recyclerViewUser.setAdapter(adapter);


    }
}