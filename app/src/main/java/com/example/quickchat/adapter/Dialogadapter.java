package com.example.quickchat.adapter;

import static com.example.quickchat.ui.ChatActivity.myImage;
import static com.example.quickchat.ui.ChatActivity.otherImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickchat.R;
import com.example.quickchat.model.MyDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dialogadapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MyDialog> myDialogArrayList;
    int ITEM_MY = 1;
    int ITEM_OTHER = 2;

    public Dialogadapter(Context context, ArrayList<MyDialog> myDialogArrayList) {
        this.context = context;
        this.myDialogArrayList = myDialogArrayList;
    }

    public ArrayList<MyDialog> getMyDialogArrayList() {
        return myDialogArrayList;
    }

    public void setMyDialogArrayList(ArrayList<MyDialog> myDialogArrayList) {
        this.myDialogArrayList = myDialogArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_MY) {
            View view = LayoutInflater.from(context).inflate(R.layout.my_item_list, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.other_item_list, parent, false);
            return new OtherViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyDialog myDialog = myDialogArrayList.get(position);

        if (holder.getClass() == MyViewHolder.class) {
            MyViewHolder holder1 = (MyViewHolder) holder;
            holder1.textView.setText(myDialog.getMessage());
            Picasso.get().load(myImage).into(holder1.imageView);
        } else {
            OtherViewHolder holder1 = (OtherViewHolder) holder;
            holder1.textView.setText(myDialog.getMessage());
            Picasso.get().load(otherImage).into(holder1.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return myDialogArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MyDialog myDialog = myDialogArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(myDialog.getMuID())) {
            return ITEM_MY;
        } else {
            return ITEM_OTHER;
        }
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

    class OtherViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView;

        public OtherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_pro);
            textView = itemView.findViewById(R.id.text_dg);
        }
    }
}
