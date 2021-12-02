package com.example.quickchat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickchat.R;
import com.example.quickchat.model.Messages;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    String id;
    List<Messages> messagesArrayList;
    public static final int MY_MESSAGE = 1;
    public static final int OTHER_MESSAGE = 2;

    public MessageAdapter(String id, List<Messages> messagesArrayList) {
        this.id = id;
        this.messagesArrayList = messagesArrayList;
    }

    public List<Messages> getMessagesArrayList() {
        return messagesArrayList;
    }

    public void setMessagesArrayList(List<Messages> messagesArrayList) {
        this.messagesArrayList = messagesArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item_list, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item_list, parent, false);

        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Messages messages =messagesArrayList.get(position);
        holder.textView.setText(messages.getMessage());
        Picasso.get().load("https://static.vecteezy.com/system/resources/previews/002/387/693/non_2x/user-profile-icon-free-vector.jpg").into(holder.imageView);
    }

    @Override
    public int getItemViewType(int position) {
       if (messagesArrayList.get(position).getId().equals(id)){
           return MY_MESSAGE;
       }else {
           return OTHER_MESSAGE;
       }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_pro);
            textView = itemView.findViewById(R.id.text_dg);
        }
    }
}
