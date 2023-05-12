package com.example.nofoodsharingproject.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivitySetterFinishHelpBinding;


public class SetterHelpFinish_Activity extends AppCompatActivity {

    private ActivitySetterFinishHelpBinding binding;
    private Button openVkButton;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterFinishHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        openVkButton = binding.setterFinishGotovk;
        returnButton = binding.setterFinishReturn;

        openVkButton.setOnClickListener(View -> vkLoad());
        returnButton.setOnClickListener(View -> {
            Intent intent = new Intent(SetterHelpFinish_Activity.this, Setter_Activity.class);
            startActivity(intent);
            finish();
        });
    }

    private void vkLoad() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/app7550574"));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
        }
    }
}
