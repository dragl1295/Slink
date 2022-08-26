package com.example.slink.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.slink.R;
import com.example.slink.chat.ChatActivity;
import com.example.slink.model.Chatlist;
import com.example.slink.model.user.Users;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.Holder> {
    private List<Users> list;
    private Context context;

    public ContactsAdapter(List<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.layout_contact_item, parent, false);
      return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
    Users user = list.get(position);
    holder.username.setText(user.getUserName());
    holder.desc.setText(user.getUserPhone());
        Glide.with(context).load(user.getImageProfile()).into(holder.imageProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ChatActivity.class)
                        .putExtra("userID", user.getUserID())
                        .putExtra("userName", user.getUserName())
                        .putExtra("userProfile", user.getImageProfile()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView username, desc;
        private ImageView imageProfile;
        public Holder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tv_username);
            desc = itemView.findViewById(R.id.tv_desc);
            imageProfile = itemView.findViewById(R.id.image_profile);
        }
    }
}
