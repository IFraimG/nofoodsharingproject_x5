package com.buyhelp.nofoodsharingproject.presentation.fragments.setter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterAdvertBinding;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Notification;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.utils.DefineUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import io.socket.client.Socket;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetterAdvertFragment extends Fragment {
    private FragmentSetterAdvertBinding binding;
    private Advertisement advertisement;
    private String fcmToken;
    private Socket socket;
    private ArrayAdapter<String> productsAdapter;
    private DefineUser<Setter> defineUser;
    private GetterRepository getterRepository;
    private NotificationRepository notificationRepository;
    private AdvertsRepository advertsRepository;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAdvertBinding.inflate(inflater);

        this.defineUser = new DefineUser<>(requireActivity());
        advertsRepository = new AdvertsRepository();
        getterRepository = new GetterRepository();
        notificationRepository = new NotificationRepository();

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        socket = app.getSocket();
        socket.connect();

        binding.setterAdvertBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterAdvertFragment_to_setterAdvrsF));
        binding.setterAdvertAccept.setOnClickListener(View -> makeHelp());
        binding.setterAdvertCreateChat.setOnClickListener(this::createChat);

        getAdvertisement(getArguments().getString("advertID", ""));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof SetterActivity) {
            ((SetterActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof SetterActivity) {
            ((SetterActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnect();

        binding = null;
    }

    private void disconnect() {
        if (socket != null) {
            socket.disconnect();
            socket.off("create_chat");
        }
    }

    private void getAdvertisement(String advertID) {
        binding.setterAdvertCreateChat.setEnabled(false);
        binding.setterAdvertAccept.setEnabled(false);
        advertsRepository.getAdvertByID(requireContext(), advertID).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                if (!response.isSuccessful()) Navigation.findNavController(requireView()).navigate(R.id.action_setterAdvertFragment_to_setterAdvrsF);
                else {
                    advertisement = response.body();
                    productsAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_name, advertisement.getListProducts());
                    binding.setterAdvertProductItemListProducts.setAdapter(productsAdapter);
                    binding.setterAdvertAuthor.setText(advertisement.getAuthorName());
                    binding.setterAdvertTitle.setText(advertisement.getTitle());
                    binding.setterAdvertDate.setText(advertisement.getDateOfCreated());
                    if (advertisement.isDone()) binding.setterAdvertAccept.setEnabled(false);
                    else binding.setterAdvertAccept.setEnabled(true);
                    binding.setterAdvertCreateChat.setEnabled(true);
                }
            }

            @Override
            public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void makeHelp() {
        binding.setterAdvertAccept.setEnabled(false);
        String generateID = Advertisement.generateID();
        advertsRepository.makeDoneAdvert(requireContext(), new RequestDoneAdvert(advertisement.getAuthorID(), defineUser.getTypeUser().first, generateID)).enqueue(new Callback<RequestDoneAdvert>() {
            @Override
            public void onResponse(@NotNull Call<RequestDoneAdvert> call, @NotNull Response<RequestDoneAdvert> response) {
                if (response.isSuccessful()) {
                    saveMessageForUser();
                    Bundle args = new Bundle();
                    args.putString("getterID", advertisement.getAuthorID());
                    args.putString("gettingProductID", generateID);
                    Navigation.findNavController(requireView()).navigate(R.id.action_setterAdvertFragment_to_setterHelpFinishFragment, args);
                } else {
                    binding.setterAdvertAccept.setEnabled(true);
                    Toast.makeText(requireContext(), R.string.smth_not_good_try_again, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RequestDoneAdvert> call, @NotNull Throwable t) {
                binding.setterAdvertAccept.setEnabled(true);
                t.printStackTrace();
            }
        });
    }

    private void getFCMTokenByUserID() {
        getterRepository.getFCMtoken(requireContext(), advertisement.getAuthorID()).enqueue(new Callback<ResponseFCMToken>() {
            @Override
            public void onResponse(@NotNull Call<ResponseFCMToken> call, @NotNull Response<ResponseFCMToken> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fcmToken = response.body().getFcmToken();
                    sendNotification();
                } else Toast.makeText(requireContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseFCMToken> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void sendNotification() {
            notificationRepository.requestNotifyDonateCall(fcmToken, getString(R.string.success_advert), getString(R.string.success_advert_body)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), R.string.sucses_notyfy_send, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(requireContext(), R.string.smth_problrs, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(requireContext(), R.string.smth_problrs, Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
    }

    private void saveMessageForUser() {
        Notification notification = new Notification(getString(R.string.success_advert), getString(R.string.success_advert_body), advertisement.getAuthorID());
        notification.setFromUserID(defineUser.getTypeUser().first);
        notification.setListItems(advertisement.getListProducts());
        notification.setTypeOfUser("getter");
        notificationRepository.createNotification(requireContext(), notification).enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(@NotNull Call<Notification> call, @NotNull Response<Notification> response) {
                if (!response.isSuccessful()) Toast.makeText(requireContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                else getFCMTokenByUserID();
            }

            @Override
            public void onFailure(@NotNull Call<Notification> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void createChat(View v) {
        try {
            JSONArray arr = new JSONArray(new String[]{defineUser.getTypeUser().first, advertisement.getAuthorID()});
            socket.emit("create_chat", arr);
            Navigation.findNavController(requireView()).navigate(R.id.action_setterAdvertFragment_to_chatsListFragment);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}
