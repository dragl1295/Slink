package com.example.slink.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.slink.R;
import com.example.slink.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
private ActivitySettingsBinding binding;
private FirebaseFirestore firestore;
private FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_settings);

       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
       firestore = FirebaseFirestore.getInstance();

       if(firebaseUser!=null){
           getInfo();
       }
       initClickAction();

    }

    private void initClickAction() {
        binding.inProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,ProfileActivity.class));
            }
        });
    }

    private void getInfo() {
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
           String userName = Objects.requireNonNull(documentSnapshot.get("userName")).toString();
                String imageProfile = documentSnapshot.getString("imageProfile");
           binding.tvUsername.setText(userName);
                Glide.with(SettingsActivity.this).load(imageProfile).into(binding.imageProfile);
            }
        });
    }
}