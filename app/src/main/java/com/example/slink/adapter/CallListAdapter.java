package com.example.slink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.slink.R;
import com.example.slink.model.CallList;
import com.example.slink.model.Chatlist;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.Holder> {
    private List<CallList> list;
    private Context context;

    public CallListAdapter(List<CallList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context).inflate(R.layout.layout_call_list, parent, false);
      return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
    CallList callList = list.get(position);
    holder.tvName.setText(callList.getUserName());
    holder.tvDate.setText(callList.getDate());

    if(callList.getCallType().equals("missed")){
     holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_downward_24));
     holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_red_dark));
    }else if(callList.getCallType().equals("income")){
        holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_downward_24));
        holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
    }else{
        holder.arrow.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_upward_24));
        holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
    }

        Glide.with(context).load(callList.getUrlProfile()).into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDate;
        private CircularImageView profile;
        private ImageView arrow;
        public Holder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tv_date);
            tvName = itemView.findViewById(R.id.tv_name);
            profile = itemView.findViewById(R.id.image_profile);
            arrow = itemView.findViewById(R.id.image_arrow);
        }
    }
}
