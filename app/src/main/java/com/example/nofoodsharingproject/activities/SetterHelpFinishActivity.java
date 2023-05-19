package com.example.nofoodsharingproject.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivitySetterFinishHelpBinding;


public class SetterHelpFinishActivity extends AppCompatActivity {
    private ActivitySetterFinishHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterFinishHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setterFinishGotovk.setOnClickListener(View -> vkLoad());
        binding.setterFinishReturn.setOnClickListener(View -> {
            Intent intent = new Intent(SetterHelpFinishActivity.this, SetterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void vkLoad() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vk_link)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
        }
    }
}
