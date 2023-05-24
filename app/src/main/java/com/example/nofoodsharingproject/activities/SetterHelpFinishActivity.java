package com.example.nofoodsharingproject.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.ApplicationCore;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivitySetterFinishHelpBinding;
import com.example.nofoodsharingproject.models.Setter;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.json.JSONArray;
import org.json.JSONException;

import io.socket.client.Socket;


public class SetterHelpFinishActivity extends AppCompatActivity {
    private ActivitySetterFinishHelpBinding binding;
    private Socket socket;
    private DefineUser<Setter> defineUser;
    private String getterID;
    private String generateID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetterFinishHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getterID = getIntent().getStringExtra("getterID");
        generateID = getIntent().getStringExtra("gettingProductID");

        if (generateID != null) binding.setterFinishCode.setText(generateID);

        defineUser = new DefineUser<>(this);

        binding.setterFinishGotovk.setOnClickListener(View -> vkLoad());
        binding.setterFinishReturn.setOnClickListener(View -> {
            Intent intent = new Intent(SetterHelpFinishActivity.this, SetterActivity.class);
            startActivity(intent);
            finish();
        });

        binding.setterFinishOpenChat.setOnClickListener(View -> createChat());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
            socket.off("getCreatedChat");
        }
    }

    private void vkLoad() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.vk_link)));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), R.string.unvisinle_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void createChat() {
        ApplicationCore app = (ApplicationCore) getApplication();
        socket = app.getSocket();
        socket.connect();
        try {
            JSONArray arr = new JSONArray(new String[]{defineUser.getTypeUser().first, getterID});
            socket.emit("create_chat", arr);
        } catch (JSONException err) {
            err.printStackTrace();
        }

        socket.on("getCreatedChat", args -> {
            Intent intent = new Intent(SetterHelpFinishActivity.this, ChatsListActivity.class);
            startActivity(intent);
        });
    }
}
