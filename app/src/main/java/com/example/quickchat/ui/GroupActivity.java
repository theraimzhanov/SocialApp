package com.example.quickchat.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickchat.R;
import com.example.quickchat.adapter.MessageAdapter;
import com.example.quickchat.model.Messages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupActivity extends AppCompatActivity {
    private EditText metText;
    private CardView mbtSent;
    private DatabaseReference reference;
    private FirebaseDatabase database;

    private List<Messages> list;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        metText =  findViewById(R.id.ettextMessageGroup);
        mbtSent =  findViewById(R.id.btnGroupSend);
         recyclerView=  findViewById(R.id.recyclerChatGroup);
        list = new ArrayList<>();

        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       adapter = new MessageAdapter(id,list);
        recyclerView.setAdapter(adapter);

         database = FirebaseDatabase.getInstance();
        reference = database.getReference("message_group");

        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString();
                if (!message.isEmpty()) {
                    /**
                     * Firebase - Send message
                     */
                    reference.push().setValue(new Messages(message,id));
                }

                metText.setText("");
            }
        });
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot != null && snapshot.getValue() != null) {
                    try {

                        Messages messages = snapshot.getValue(Messages.class);

                       list.add(messages);
                        recyclerView.scrollToPosition(list.size() - 1);
                        adapter.notifyItemInserted(list.size() - 1);
                    } catch (Exception ex) {
                        Toast.makeText(GroupActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}