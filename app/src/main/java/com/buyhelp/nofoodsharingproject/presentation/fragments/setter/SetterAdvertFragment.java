package com.buyhelp.nofoodsharingproject.presentation.fragments.setter;

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
import com.buyhelp.nofoodsharingproject.data.api.getter.GetterRepository;
import com.buyhelp.nofoodsharingproject.data.api.notifications.NotificationRepository;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.SetterActivity;
import com.buyhelp.nofoodsharingproject.databinding.FragmentSetterAdvertBinding;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Setter;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.factories.setters.SetterAdvertFactory;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.setter.SetterAdvertViewModel;
import com.buyhelp.nofoodsharingproject.presentation.views.CustomAppCompatButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import io.socket.client.Socket;

public class SetterAdvertFragment extends Fragment {
    private FragmentSetterAdvertBinding binding;
    private Socket socket;
    private ArrayAdapter<String> productsAdapter;
    private DefineUser<Setter> defineUser;
    private SetterAdvertViewModel viewModel;
    private AdvertsRepository advertsRepository;
    private NotificationRepository notificationRepository;
    private GetterRepository getterRepository;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
        notificationRepository = app.getAppComponent().getNotificationRepository();
        getterRepository = app.getAppComponent().getGetterRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSetterAdvertBinding.inflate(inflater);

        this.defineUser = new DefineUser<>(requireActivity());

        viewModel = new ViewModelProvider(requireActivity(),
                new SetterAdvertFactory(requireActivity().getApplication(), advertsRepository, notificationRepository, getterRepository))
                .get(SetterAdvertViewModel.class);

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        socket = app.getSocket();
        socket.connect();

        binding.setterAdvertBack.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_setterAdvertFragment_to_setterAdvrsF));
        binding.setterAdvertAccept.setOnClickListener(View -> makeHelp());
        binding.setterAdvertCreateChat.setOnClickListener(this::createChat);
        binding.setterAdvertReport.setOnClickListener(View -> openReportModal());

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

        viewModel.getAdvert(advertID).observe(requireActivity(), advertisement -> {
            if (advertisement != null) {
                productsAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_name, advertisement.getListProducts());
                binding.setterAdvertProductItemListProducts.setAdapter(productsAdapter);

                binding.setterAdvertAuthor.setText(advertisement.getAuthorName());
                binding.setterAdvertTitle.setText(advertisement.getTitle());
                binding.setterAdvertDate.setText(advertisement.getDateOfCreated());

                binding.setterAdvertAccept.setEnabled(!advertisement.isDone());
                binding.setterAdvertCreateChat.setEnabled(true);
            } else
                Navigation.findNavController(requireView()).navigate(R.id.action_setterAdvertFragment_to_setterAdvrsF);
        });
    }

    private void makeHelp() {
        binding.setterAdvertAccept.setEnabled(false);
        String generateID = Advertisement.generateID();
        viewModel.makeHelp(defineUser).observe(requireActivity(), advertisement -> {
            Bundle args = new Bundle();
            args.putString("getterID", advertisement.getAuthorID());
            args.putString("gettingProductID", generateID);
            Navigation.findNavController(requireView()).navigate(R.id.action_setterAdvertFragment_to_setterHelpFinishFragment, args);
        });
    }

    private void createChat(View v) {
        try {
            JSONArray arr = new JSONArray(new String[]{defineUser.getTypeUser().first, viewModel.getAdvert().getAuthorID()});
            socket.emit("create_chat", arr);
            Navigation.findNavController(requireView()).navigate(R.id.action_setterAdvertFragment_to_chatsListFragment);
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

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
