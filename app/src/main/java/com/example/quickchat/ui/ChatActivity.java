package com.example.quickchat.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.R;
import com.example.quickchat.adapter.Dialogadapter;
import com.example.quickchat.model.MyDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String getName, getImage, getIUD, my_ID;
    private CircleImageView profileImage;
    private TextView otherName;


    private FirebaseAuth auth;
    private FirebaseDatabase database;

    public static String myImage;
    public static String otherImage;
    CardView btSend;
    EditText editTextMessage;

    String my_Room, other_Room;
    RecyclerView recyclerView;
    ArrayList<MyDialog> dialogArrayList;
    Dialogadapter dialogadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        btSend = findViewById(R.id.btnSend);
        editTextMessage = findViewById(R.id.ettextMessage);
        Intent intent = getIntent();
        getName = intent.getStringExtra("name");
        getImage = intent.getStringExtra("image");
        getIUD = intent.getStringExtra("uid");
        profileImage = findViewById(R.id.profilImageViewInChat);
        otherName = findViewById(R.id.chatName);


        otherName.setText(getName);
        Picasso.get().load(getImage).into(profileImage);
        my_ID = auth.getUid();

        my_Room = my_ID + getIUD;
        other_Room = getIUD + my_ID;

        recyclerView = findViewById(R.id.recyclerChat);
        dialogArrayList = new ArrayList<>();
        dialogadapter = new Dialogadapter(ChatActivity.this, dialogArrayList);
        recyclerView.setAdapter(dialogadapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chat_reference = database.getReference().child("chats").child(my_Room).child("dialogs");
        chat_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialogArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MyDialog dialog = dataSnapshot.getValue(MyDialog.class);
                    dialogArrayList.add(dialog);
                }
                dialogadapter.setMyDialogArrayList(dialogArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot !=null){
         myImage = snapshot.child("imageUri").getValue().toString();
                otherImage = getImage;
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (message.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "write!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                editTextMessage.setText(" ");
                Date date = new Date();
                MyDialog dialog = new MyDialog(message, my_ID, date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(my_Room).child("dialogs")
                        .push().setValue(dialog).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(other_Room).child("dialogs")
                                .push().setValue(dialog).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });

    }
}