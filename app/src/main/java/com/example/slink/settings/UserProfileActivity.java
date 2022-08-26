package com.example.slink.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.slink.R;
import com.example.slink.databinding.ActivityUserProfileBinding;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_user_profile);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
       String receiverID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("userProfile");

        if(receiverID!=null) {
            binding.toolbar.setTitle(userName);
            if (userProfile.equals("")) {
                binding.imageProfile.setImageResource(R.drawable.avatar);
            } else {
                Glide.with(this).load(userProfile).into(binding.imageProfile);
            }
        }
    }
}