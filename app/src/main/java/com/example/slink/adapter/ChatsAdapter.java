package com.example.slink.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.slink.R;
import com.example.slink.chat.AudioService;
import com.example.slink.databinding.DeleteDialogBinding;
import com.example.slink.model.Chats;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;


public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
private List<Chats> list;
private Context context;
public static final int MSG_TYPE_LEFT = 0;
public static final int MSG_TYPE_RIGHT = 1;
private FirebaseUser firebaseUser;
private ImageButton tmpBtnPlay;
private AudioService audioService;


public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
        this.audioService = new AudioService(context);
        }

public void setList(List<Chats> list){
        this.list = list;
        notifyDataSetChanged();
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_LEFT) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        return new ViewHolder(view);
        }else {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        return new ViewHolder(view);
        }
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
        }

@Override
public int getItemCount() {
        return list.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView textMessage, textTime;
    private LinearLayout layoutText, layoutImage, layoutVoice;
    private ImageView imageMessage, feeling;
    private ImageButton btnPlay;
    private ViewHolder tmpHolder;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        textMessage = itemView.findViewById(R.id.tv_text_message);
        layoutImage = itemView.findViewById(R.id.layout_image);
        layoutText = itemView.findViewById(R.id.layout_text);
        imageMessage = itemView.findViewById(R.id.image_chat);
        layoutVoice = itemView.findViewById(R.id.layout_voice);
        btnPlay = itemView.findViewById(R.id.btn_play_chat);
        textTime = itemView.findViewById(R.id.tv_date);
        feeling = itemView.findViewById(R.id.feeling);
    }
    @SuppressLint("ClickableViewAccessibility")
    void bind(final Chats chats) {
        int reactions[] = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            if (pos < 0)
                return false;
            feeling.setImageResource(reactions[pos]);
           feeling.setVisibility(View.VISIBLE);
            chats.setFeeling(pos);
            FirebaseDatabase.getInstance().getReference()
                    .child("Chats")
                    .child(chats.getMessageID()).setValue(chats);
            return true;
        });

        if(chats.getFeeling() >= 0) {
            feeling.setImageResource(reactions[chats.getFeeling()]);
            feeling.setVisibility(View.VISIBLE);
        } else {
           feeling.setVisibility(View.GONE);
        }

         View.OnTouchListener OnTouchToDrag =new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popup.onTouch(v, event);
                return false;
            }
    };



//        layoutImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                    popup.onTouch(v, event);
//                return false;
//            }
//        });
//        layoutVoice.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                popup.onTouch(v, event);
//                return false;
//            }
//        });


         itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v ) {

               new AlertDialog.Builder(context)
                        .setTitle("Delete Message")
                        .setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase.getInstance().getReference()
                    .child("Chats")
                    .child(chats.getMessageID()).setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                   }
               }).show();

                itemView.setOnTouchListener(OnTouchToDrag);

                return false;
            }
        });




        switch (chats.getType()){
            case "TEXT" :
                layoutText.setVisibility(View.VISIBLE);
                layoutImage.setVisibility(View.GONE);
                layoutVoice.setVisibility(View.GONE);

                textMessage.setText(chats.getTextMessage());
                textTime.setText(chats.getDateTime());

                break;
            case "IMAGE" :
                layoutText.setVisibility(View.GONE);
                layoutImage.setVisibility(View.VISIBLE);
                layoutVoice.setVisibility(View.GONE);

                textTime.setText(chats.getDateTime());
                Glide.with(context).load(chats.getUrl()).into(imageMessage);
                break;

            case "VOICE" :
                layoutText.setVisibility(View.GONE);
                layoutImage.setVisibility(View.GONE);
                layoutVoice.setVisibility(View.VISIBLE);
                textTime.setText(chats.getDateTime());
                layoutVoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tmpBtnPlay!=null){
                            tmpBtnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                        }

                        btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
                        audioService.playAudioFromUrl(chats.getUrl(), new AudioService.OnPlayCallBack() {
                            @Override
                            public void onFinished() {
                                btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                            }
                        });

                        tmpBtnPlay = btnPlay;

                    }
                });

                break;
        }
    }
}

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else
        {
            return MSG_TYPE_LEFT;
        }
    }
}

