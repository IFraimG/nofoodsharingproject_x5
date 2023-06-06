/**
 * Класс {@code ChatListFragment} - фрагмент страницы списка чатов
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.databinding.FragmentListChatsBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.NeedyActivity;
import com.buyhelp.nofoodsharingproject.presentation.activities.GiverActivity;
import com.buyhelp.nofoodsharingproject.data.models.Chat;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
public class ChatsListFragment extends Fragment {
    private WeakReference<FragmentListChatsBinding> mBinding;
    private DefineUser defineUser;
    private ArrayAdapter<String> arrayAdapter;
    private List<Chat> chats;
    private List<String> shortChats = new ArrayList<>();
    private Socket mSocket;

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
        FragmentListChatsBinding binding = FragmentListChatsBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        getChatsList();

        arrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_chat, shortChats);
        binding.chatList.setAdapter(arrayAdapter);
        binding.chatList.setOnItemClickListener((parent, view, position, id) -> {
            Chat chat = chats.get(position);

            Bundle args = new Bundle();
            args.putString("chatID", chat.getChatID());
            Navigation.findNavController(view).navigate(R.id.action_chatsListFragment_to_chatFragment, args);
        });

        binding.chatsReturn.setOnClickListener(v -> Navigation.findNavController(v).popBackStack());

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
            mSocket.off("get_chats");
            mSocket.off("send_user_id_to_get_chat");
        }
    }

    /**
     * Этот метод получает список чатов
     */
    private void getChatsList() {
        Pair<String, Boolean> userData = defineUser.getTypeUser();
        String type = userData.second ? "needy" : "giver";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", type);
            jsonObject.put("userID", userData.first);
            mSocket.emit("send_user_id_to_get_chat", jsonObject);
        } catch (JSONException err) {
            err.printStackTrace();
        }

        mSocket.on("get_chats", args -> new Handler(Looper.getMainLooper()).post(() -> {
            JSONObject data = (JSONObject) args[0];
            chatsUpdate(data);
        }));
    }

    /**
     * Этот метод отображает список чатов
     */
    private void chatsUpdate(JSONObject data) {
        try {
            JSONArray chatsJSON = data.getJSONArray("result");

            chats = new ArrayList<>(chatsJSON.length());
            shortChats = new ArrayList<>(chatsJSON.length());

            for (int i = 0; i < chatsJSON.length(); i++) {
                JSONObject chat = chatsJSON.getJSONObject(i);
                Chat normalChat = new Chat();

                normalChat.setChatID(chat.getString("_id"));
                normalChat.setDateCreated(chat.getString("dateCreated"));
                normalChat.setTitle(chat.getString("title"));

                shortChats.add(chat.getString("title"));

                JSONArray users = chat.getJSONArray("users");
                String[] normalUsers = new String[users.length()];
                for (int j = 0; j < users.length(); j++) normalUsers[j] = users.getString(j);
                normalChat.setUsers(normalUsers);

                chats.add(normalChat);
            }

            arrayAdapter.clear();
            arrayAdapter.addAll(shortChats);
            arrayAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
