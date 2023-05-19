package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.ApplicationCore;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivityChatBinding;
import com.example.nofoodsharingproject.models.Message;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ImageView sendMessageButton;
    private EditText messageInput;
    private Socket mSocket;
    private String chatID;
    private DefineUser defineUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());

        chatID = getIntent().getStringExtra("chatID");
        defineUser = new DefineUser<>(this);

        ApplicationCore app = (ApplicationCore) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();

        sendMessageButton = binding.messagesSend;
        messageInput = binding.messagesInput;

        sendMessageButton.setOnClickListener(View -> sendMessage());

        getMessages();

        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) mSocket.disconnect();
    }

    private void getMessages() {
        mSocket.emit("get_messages", chatID);

        mSocket.on("set_messages", args -> {

        });
    }
    private void sendMessage() {
        String body = messageInput.getText().toString();
        if (body.length() == 0) Toast.makeText(this, getString(R.string.no_input_message), Toast.LENGTH_SHORT).show();
        else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("body", body);
                jsonObject.put("chatID", chatID);
                jsonObject.put("authorID", defineUser.getUser().getX5_Id());

                mSocket.emit("save_message", jsonObject);

                messageInput.setText("");
            } catch (JSONException err) {
                err.printStackTrace();
            }
        }
    }
}
