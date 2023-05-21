package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.nofoodsharingproject.ApplicationCore;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.adapters.MessagesAdapter;
import com.example.nofoodsharingproject.databinding.ActivityChatBinding;
import com.example.nofoodsharingproject.models.Message;
import com.example.nofoodsharingproject.utils.DefineUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private Socket mSocket;
    private String chatID;
    private DefineUser defineUser;
    private MessagesAdapter messagesAdapter;
    private LinearLayoutManager layoutManager;
    private final List<Message> messages = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());

        chatID = getIntent().getStringExtra("chatID");
        defineUser = new DefineUser<>(this);

        messagesAdapter = new MessagesAdapter(getApplicationContext(), defineUser.getUser().getX5_Id());
        messagesAdapter.updateMessages(messages);
        layoutManager = new LinearLayoutManager(this);
        binding.messagesList.setLayoutManager(layoutManager);
        binding.messagesList.setAdapter(messagesAdapter);

        ApplicationCore app = (ApplicationCore) getApplication();
        mSocket = app.getSocket();
        mSocket.connect();

        binding.messagesSend.setOnClickListener(View -> sendMessage());
        binding.chatReturn.setOnClickListener(v -> {
            finish();
        });

        mSocket.emit("get_messages", chatID);
        mSocket.on("set_messages", setMessages);

        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off("set_messages");
            mSocket.off("get_messages");
        }
    }
    private final Emitter.Listener setMessages = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(() -> {
                JSONObject data = (JSONObject) args[0];
                List<Message> newMessages = new ArrayList<>();
                try {
                    JSONArray chatsJSON = data.getJSONArray("result");
                    for (int i = messages.size(); i < chatsJSON.length(); i++) {
                        JSONObject jsonMsg = chatsJSON.getJSONObject(i);

                        Message message = new Message();

                        message.setChatID(jsonMsg.getString("chatID"));
                        message.setBody(jsonMsg.getString("body"));
                        message.setAuthorID(jsonMsg.getString("authorID"));
                        message.setDateCreated(jsonMsg.getString("dateCreated"));
                        message.setMessageID(jsonMsg.getString("_id"));

                        newMessages.add(message);
                    }

                    messages.addAll(newMessages);
                    messagesAdapter.updateMessages(newMessages);
                    binding.messagesList.scrollToPosition(messages.size() - 1);
                } catch (JSONException err) {
                    throw new RuntimeException(err);
                }
            });
        }
    };

    private void sendMessage() {
        String body = binding.messagesInput.getText().toString();
        if (body.length() == 0) Toast.makeText(this, getString(R.string.no_input_message), Toast.LENGTH_SHORT).show();
        else {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("body", body);
                jsonObject.put("chatID", chatID);
                jsonObject.put("authorID", defineUser.getUser().getX5_Id());

                Message message = new Message();
                message.setBody(body);
                message.setChatID(chatID);
                message.setAuthorID(defineUser.getUser().getX5_Id());
                message.createDate();
                binding.messagesInput.setText("");

                mSocket.emit("save_message", jsonObject);
                mSocket.emit("get_messages", chatID);
                mSocket.on("set_messages", setMessages);

                messages.add(message);
                messagesAdapter.updateMessage(message);

                binding.messagesList.scrollToPosition(messages.size() - 1);
            } catch (JSONException err) {
                err.printStackTrace();
            }
        }
    }
}
