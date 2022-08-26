package com.example.slink.chat;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.example.slink.MainActivity;
import com.example.slink.R;
import com.example.slink.adapter.ChatsAdapter;
import com.example.slink.common.FirebaseService;
import com.example.slink.databinding.ActivityChatBinding;
import com.example.slink.model.Chats;
;
import com.example.slink.settings.UserProfileActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

//    private ActivityChatBinding binding;
//    private String receiverID, senderID, userName;
//    private String receiverRoom, senderRoom;
//    private ChatsAdapter adapder;
//    private List<Chats> list = new ArrayList<>();
//    private FirebaseAuth auth;
//    private FirebaseDatabase database;
//    private Boolean isActionBar = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
//
//        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//
//        Intent intent = getIntent();
//        userName = intent.getStringExtra("userName");
//        receiverID = intent.getStringExtra("userID");
//        senderID = auth.getUid();
//       String userProfile = intent.getStringExtra("userProfile");
//
//           binding.tvUsername.setText(userName);
//        if(userProfile.equals("")){
//            binding.imageProfile.setImageResource(R.drawable.avatar);
//        }else {
//            Glide.with(this).load(userProfile).into(binding.imageProfile);
//        }
//
//        binding.btnBack.setOnClickListener(v -> finish());
//        binding.imageProfile.setOnClickListener(view -> startActivity(new Intent(ChatActivity.this, UserProfileActivity.class)
//        .putExtra("userID", receiverID)
//        .putExtra("userProfile",userProfile)
//        .putExtra("userName", userName)));
//
//        binding.edMessage.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (TextUtils.isEmpty(binding.edMessage.getText().toString())) {
//                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
//
//                } else {
//                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        list = new ArrayList<>();
//
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapder = new ChatsAdapter(list, this);
//        binding.recyclerView.setAdapter(adapder);
//
//        senderRoom = senderID + receiverID;
//        receiverRoom = receiverID + senderID;
//
//
//        database.getReference().child("chats")
//                .child(senderRoom).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                for(DataSnapshot snapshot1: snapshot.getChildren()){
//                    Chats chats = snapshot1.getValue(Chats.class);
//                    list.add(chats);
//                }
//                adapder.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        binding.btnSend.setOnClickListener(v -> {
//           String message  = binding.edMessage.getText().toString();
//           final Chats chats = new Chats(message, senderID);
//           chats.setTimestamp(new Date().getTime());
//
//            Date date = Calendar.getInstance().getTime();
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd-mm-yyyy");
//            String today = format.format(date);
//
//            Calendar currentDateTime = Calendar.getInstance();
//            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm");
//            String currentTime = df.format(currentDateTime.getTime());
//            chats.setDateTime(currentTime);
//
//            binding.edMessage.setText("");
//            database.getReference().child("chats")
//                    .child(senderRoom)
//                    .push()
//                    .setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//                    database.getReference().child("chats")
//                            .child(receiverRoom)
//                            .push()
//                            .setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//
//                        }
//                    });
//                }
//            });
//            database.getReference("chatlist").child(senderID).child(receiverID)
//                    .child("chatid")
//                    .setValue(receiverID);
//            database.getReference("chatlist").child(receiverID).child(senderID)
//                    .child("chatid")
//                    .setValue(senderID);
//
//        });
//
//        binding.btnFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (isActionBar) {
//                    binding.layoutAction.setVisibility(View.GONE);
//                    isActionBar = false;
//                }else {
//                    binding.layoutAction.setVisibility(View.VISIBLE);
//                    isActionBar = true;
//                }
//            }
//        });
//
//    }

private static final String TAG = "ChatsActivity";
    private static final int REQUEST_CORD_PERMISSION = 332;
    private ActivityChatBinding binding;
    private String receiverID;
    private ChatsAdapter adapder;
    private List<Chats>list = new ArrayList<>();
    private String userProfile,userName;
    private boolean isActionShown = false;
    private ChatService chatService;
    private int IMAGE_GALLERY_REQUEST = 111;
    private Uri imageUri;

    //Audio
    private MediaRecorder mediaRecorder;
    private String audio_path;
    private String sTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat);

        initialize();
        initBtnClick();
        readChats();

        FirebaseDatabase.getInstance().getReference().child("presence").child(receiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String status = snapshot.getValue(String.class);
                    if(!status.isEmpty()){
                        binding.tvStatus.setText(status);
//                        binding.tvStatus.setText(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("presence").child(currentId).setValue("Online");
    }

    private void initialize(){

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        receiverID = intent.getStringExtra("userID");
        userProfile = intent.getStringExtra("userProfile");

        chatService = new ChatService(this,receiverID);

        if (receiverID!=null){
            Log.d(TAG, "onCreate: receiverID "+receiverID);
            binding.tvUsername.setText(userName);
            if (userProfile != null) {
                if (userProfile.equals("")){
                    binding.imageProfile.setImageResource(R.drawable.avatar);  // set  default image when profile user is null
                } else {
                    Glide.with(this).load(userProfile).into( binding.imageProfile);
                }
            }
        }
        final Handler handler = new Handler();

        binding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    binding.btnSend.setVisibility(View.INVISIBLE);
                    binding.recordButton.setVisibility(View.VISIBLE);
                } else {
                    binding.btnSend.setVisibility(View.VISIBLE);
                    binding.recordButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                FirebaseDatabase.getInstance().getReference().child("presence").child(FirebaseAuth.getInstance().getUid()).setValue("typing...");
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(userStoppedTyping, 1000);

            }
            Runnable userStoppedTyping = new Runnable() {
                @Override
                public void run() {
                    FirebaseDatabase.getInstance().getReference().child("presence").child(FirebaseAuth.getInstance().getUid()).setValue("Online");
                }
            };
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);

        binding.recordButton.setRecordView(binding.recordView);
        binding.recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {


                if (!checkPermissionFromDevice()) {
                    binding.btnEmoji.setVisibility(View.INVISIBLE);
                    binding.btnFile.setVisibility(View.INVISIBLE);
                    binding.btnCamera.setVisibility(View.INVISIBLE);
                    binding.edMessage.setVisibility(View.INVISIBLE);

                    startRecord();
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator != null) {
                        vibrator.vibrate(100);
                    }

                } else {
                    requestPermission();
                }

            }

            @Override
            public void onCancel() {
                try {
                    mediaRecorder.reset();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish(long recordTime) {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);

                //Stop Recording..
                try {
                    sTime = getHumanTimeText(recordTime);
                    stopRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLessThanSecond() {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);
            }
        });
        binding.recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                binding.btnEmoji.setVisibility(View.VISIBLE);
                binding.btnFile.setVisibility(View.VISIBLE);
                binding.btnCamera.setVisibility(View.VISIBLE);
                binding.edMessage.setVisibility(View.VISIBLE);
            }
        });

    }

    @SuppressLint("DefaultLocale")
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d",
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    private void readChats() {
        chatService.readChatData(new OnReadChatCallBacks() {
            @Override
            public void onReadSuccess(List<Chats> list) {
                //adapder.setList(list);
                Log.d(TAG, "onReadSuccess: List "+list.size());
                binding.recyclerView.setAdapter(new ChatsAdapter(list,ChatActivity.this));
            }

            @Override
            public void onReadFailed() {
                Log.d(TAG, "onReadFailed: ");
            }
        });
    }

    private void initBtnClick(){
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.edMessage.getText().toString())){
                    chatService.sendTextMsg(binding.edMessage.getText().toString());
                    binding.edMessage.setText("");
                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, UserProfileActivity.class)
                        .putExtra("userID",receiverID)
                        .putExtra("userProfile",userProfile)
                        .putExtra("userName",userName));
            }
        });

        binding.btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActionShown){
                    binding.layoutActions.setVisibility(View.GONE);
                    isActionShown = false;
                } else {
                    binding.layoutActions.setVisibility(View.VISIBLE);
                    isActionShown = true;
                }

            }
        });

        binding.btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), IMAGE_GALLERY_REQUEST);

    }

    private boolean checkPermissionFromDevice() {
        int write_external_strorage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return write_external_strorage_result == PackageManager.PERMISSION_DENIED || record_audio_result == PackageManager.PERMISSION_DENIED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_CORD_PERMISSION);
    }

    private void startRecord(){
        setUpMediaRecorder();

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            //  Toast.makeText(InChatActivity.this, "Recording...", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ChatActivity.this, "Recording Error , Please restart your app ", Toast.LENGTH_LONG).show();
        }

    }

    private void stopRecord(){
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;

                chatService.sendVoice(audio_path);

            }else {
                Toast.makeText(getApplicationContext(), "Null" , Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Stop Recording Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setUpMediaRecorder() {
        String path_save = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + UUID.randomUUID().toString() + "audio_record.m4a";
        audio_path = path_save;

        mediaRecorder = new MediaRecorder();
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(path_save);
        } catch (Exception e) {
            Log.d(TAG, "setUpMediaRecord: " + e.getMessage());
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){

            imageUri = data.getData();

            //uploadToFirebase();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                reviewImage(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
    private void reviewImage(Bitmap bitmap){
        new DialogReviewSendImage(ChatActivity.this,bitmap).show(new DialogReviewSendImage.OnCallBack() {
            @Override
            public void onButtonSendClick() {
                // to Upload Image to firebase storage to get url image...
                if (imageUri!=null){
                    final ProgressDialog progressDialog = new ProgressDialog(ChatActivity.this);
                    progressDialog.setMessage("Sending image...");
                    progressDialog.show();

                    //hide action buttonss
                    binding.layoutActions.setVisibility(View.GONE);
                    isActionShown = false;

                    new FirebaseService(ChatActivity.this).uploadImageToFireBaseStorage(imageUri, new FirebaseService.OnCallBack() {
                        @Override
                        public void onUploadSuccess(String imageUrl) {
                            // to send chat image//
                            chatService.sendImage(imageUrl);
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onUploadFailed(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

            }
        });
    }
}