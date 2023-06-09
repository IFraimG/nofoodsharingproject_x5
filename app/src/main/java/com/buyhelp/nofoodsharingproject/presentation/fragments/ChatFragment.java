/**
 * Класс {@code ChatFragment} - фрагмент страницы чата с сообщениями
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.buyhelp.nofoodsharingproject.databinding.FragmentChatBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.GiverActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.NeedyActivity;
import com.buyhelp.nofoodsharingproject.presentation.adapters.MessagesAdapter;
import com.buyhelp.nofoodsharingproject.data.models.Message;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatFragment extends Fragment {
    private FragmentChatBinding binding;
    private WeakReference<FragmentChatBinding> mBinding;
    private Socket mSocket;
    private String chatID = "";
    private DefineUser defineUser;
    private MessagesAdapter messagesAdapter;
    private final List<Message> messages = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.connect();

        defineUser = app.getHelpersComponent().getDefineUser();

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        mBinding = new WeakReference<>(binding);

        if (getArguments() != null) chatID = getArguments().getString("chatID");

        messagesAdapter = new MessagesAdapter(requireContext(), defineUser.getUser().getX5_Id());
        messagesAdapter.updateMessages(messages);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.messagesList.setLayoutManager(layoutManager);
        binding.messagesList.setAdapter(messagesAdapter);

        binding.messagesSend.setOnClickListener(View -> sendMessage());
        binding.chatReturn.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

        mSocket.emit("get_messages", chatID);
        mSocket.on("set_messages", setMessages);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(false);
        } else if (getActivity() instanceof NeedyActivity) {
            ((NeedyActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(true);
        } else if (getActivity() instanceof NeedyActivity) {
            ((NeedyActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off("set_messages");
            mSocket.off("get_messages");
        }
    }

    /**
     * Этот метод получает список сообщений
     */
    private final Emitter.Listener setMessages = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(() -> {
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

    /**
     * Этот метод отправляет сообщение в чат
     */
    private void sendMessage() {
        String body = binding.messagesInput.getText().toString();
        if (body.length() == 0) Snackbar.make(requireContext(), requireView(), getString(R.string.no_input_message), Snackbar.LENGTH_SHORT).show();
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
