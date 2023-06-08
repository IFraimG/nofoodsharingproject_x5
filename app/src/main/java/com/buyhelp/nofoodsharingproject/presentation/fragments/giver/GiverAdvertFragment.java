/**
 * Класс {@code GiverAdvertFragment} - фрагмент страницы объявления у отдающего пользователя
 * Отдающий зайдет на эту страницу, когда будет стоять на кассе
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments.giver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.data.api.needy.NeedyRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGiverAdvertBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.GiverActivity;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.factories.giver.GiverAdvertFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.giver.GiverAdvertViewModel;
import com.buyhelp.nofoodsharingproject.presentation.views.CustomAppCompatButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;

import io.socket.client.Socket;

public class GiverAdvertFragment extends Fragment {
    private FragmentGiverAdvertBinding binding;
    private WeakReference<FragmentGiverAdvertBinding> mBinding;
    private Socket socket;
    private ArrayAdapter<String> productsAdapter;
    private DefineUser defineUser;
    private GiverAdvertViewModel viewModel;
    private AdvertsRepository advertsRepository;
    private NotificationRepository notificationRepository;
    private NeedyRepository needyRepository;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
        notificationRepository = app.getAppComponent().getNotificationRepository();
        needyRepository = app.getAppComponent().getNeedyRepository();
        this.defineUser = app.getHelpersComponent().getDefineUser();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGiverAdvertBinding.inflate(inflater);
        mBinding = new WeakReference<>(binding);

        viewModel = new ViewModelProvider(requireActivity(),
                new GiverAdvertFactory(requireActivity().getApplication(), advertsRepository, notificationRepository, needyRepository))
                .get(GiverAdvertViewModel.class);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        socket = app.getSocket();
        socket.connect();

        binding.giverAdvertBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_giverAdvertFragment_to_giverAdvrsF));
        binding.giverAdvertAccept.setOnClickListener(this::makeHelp);
        binding.giverAdvertCreateChat.setOnClickListener(this::createChat);
        binding.giverAdvertReport.setOnClickListener(View -> openReportModal());

        if (getArguments() != null) getAdvertisement(getArguments().getString("advertID", ""));

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof GiverActivity) {
            ((GiverActivity) requireActivity()).setBottomNavigationVisibility(true);
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
        disconnect();
    }

    private void disconnect() {
        if (socket != null) {
            socket.disconnect();
            socket.off("create_chat");
        }
    }

    /**
     * Метод для получения объявления
     * @param advertID необходим для поиска объявления
     */
    private void getAdvertisement(String advertID) {
        binding.giverAdvertCreateChat.setEnabled(false);
        binding.giverAdvertAccept.setEnabled(false);

        viewModel.getAdvert(advertID).observe(requireActivity(), advertisement -> {
            if (advertisement != null) {
                productsAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_needy_product_name, advertisement.getListProducts());
                binding.giverAdvertProductItemListProducts.setAdapter(productsAdapter);

                binding.giverAdvertAuthor.setText(advertisement.getAuthorName());
                binding.giverAdvertTitle.setText(advertisement.getTitle());
                binding.giverAdvertDate.setText(advertisement.getDateOfCreated());

                binding.giverAdvertAccept.setEnabled(!advertisement.isDone());
                binding.giverAdvertCreateChat.setEnabled(true);
            } else
                Navigation.findNavController(requireView()).navigate(R.id.action_giverAdvertFragment_to_giverAdvrsF);
        });
    }

    /**
     * Метод, который отмечает, что пользователь совершил покупку
     */
    private void makeHelp(View v) {
        binding.giverAdvertAccept.setEnabled(false);
        String generateID = Advertisement.generateID();
        viewModel.makeHelp(defineUser, generateID).observe(requireActivity(), advertisement -> {
            Bundle args = new Bundle();
            args.putString("needyID", advertisement.getAuthorID());
            args.putString("gettingProductID", generateID);
            Navigation.findNavController(v).navigate(R.id.action_giverAdvertFragment_to_giverHelpFinishFragment, args);
        });
    }


    private void createChat(View v) {
        try {
            JSONArray arr = new JSONArray(new String[]{defineUser.getTypeUser().first, viewModel.getAdvert().getAuthorID()});
            socket.emit("create_chat", arr);
            Navigation.findNavController(requireView()).navigate(R.id.action_giverAdvertFragment_to_chatsListFragment);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    /**
     * Метод необходим на случай, если пользователь захочет пожаловаться на объявление
     */
    private void openReportModal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = getLayoutInflater();
        android.view.View customView = inflater.inflate(R.layout.modal_report, null);
        builder.setView(customView);

        CustomAppCompatButton reportUser = customView.findViewById(R.id.report_advert);

        reportUser.setOnClickListener(View -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + R.string.mail));
            startActivity(intent);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
