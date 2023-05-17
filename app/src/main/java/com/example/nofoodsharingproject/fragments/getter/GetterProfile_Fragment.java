package com.example.nofoodsharingproject.fragments.getter;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.activities.MainAuth_Activity;
import com.example.nofoodsharingproject.data.api.getter.GetterRepository;
import com.example.nofoodsharingproject.databinding.FragmentGetterProfileBinding;
import com.example.nofoodsharingproject.models.ShortDataUser;
import com.example.nofoodsharingproject.utils.DefineUser;
import com.example.nofoodsharingproject.utils.ValidateUser;
import com.example.nofoodsharingproject.models.Getter;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetterProfile_Fragment extends Fragment {
    private FragmentGetterProfileBinding binding;
    private EditText editLogin;
    private EditText editPhone;
    private EditText editPassword;
    private EditText oldPassword;
    private ImageButton buttonLogout;
    private Button btnSave;
    private TextView login;
    private TextView phone;
    private ShortDataUser user;
    private DefineUser<Getter> defineUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defineUser = new DefineUser<Getter>(requireActivity());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGetterProfileBinding.inflate(inflater);

        buttonLogout = binding.getterProfileLogout;
        editLogin = binding.getterProfileEditLogin;
        editPhone = binding.getterProfileEditPhone;
        editPassword = binding.getterProfileEditPassword;
        btnSave = binding.getterProfileSave;
        login = binding.getterProfileLogin;
        phone = binding.getterProfilePhone;
        oldPassword = binding.getterProfileEditOldPassword;

        user = defineUser.getUser();

        login.setText(user.getLogin());
        phone.setText(user.getPhone());

        buttonLogout.setOnClickListener(View -> logout());
        btnSave.setOnClickListener(View -> editProfile());


        return binding.getRoot();
    }


    private void editProfile() {
        String newLogin = editLogin.getText().toString();
        String newPhone = editPhone.getText().toString();
        String newPassword = editPassword.getText().toString();
        String oldPasswordText = oldPassword.getText().toString();

        if (!ValidateUser.validatePhone(newPhone)) {
            Toast.makeText(getContext(), R.string.uncorrect_number_phone, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validateLogin(newLogin)) {
            Toast.makeText(getContext(), R.string.uncorrect_name, Toast.LENGTH_LONG).show();
        } else if (!ValidateUser.validatePassword(newPassword)) {
            Toast.makeText(getContext(), R.string.uncorrect_password, Toast.LENGTH_LONG).show();
        } else {
            btnSave.setEnabled(false);
            GetterRepository.editProfile(user.getX5_Id(), newLogin, newPhone, newPassword, oldPasswordText).enqueue(new Callback<Getter>() {
                @Override
                public void onResponse(@NotNull Call<Getter> call, @NotNull Response<Getter> response) {
                    if (response.code() == 400) Toast.makeText(getContext(), R.string.your_password_uncorrect, Toast.LENGTH_SHORT).show();
                    if (response.code() == 201) {
                        Toast.makeText(getContext(), R.string.sucses, Toast.LENGTH_SHORT).show();
                        login.setText(response.body().getLogin());
                        phone.setText(response.body().getPhone());

                        defineUser.editProfileInfo(response.body().getLogin(), response.body().getPhone());

                        editLogin.setText("");
                        editPassword.setText("");
                        editPhone.setText("");
                        oldPassword.setText("");
                    }
                    btnSave.setEnabled(true);
                }

                @Override
                public void onFailure(@NotNull Call<Getter> call, @NotNull Throwable t) {
                    t.printStackTrace();
                    btnSave.setEnabled(true);
                    Toast.makeText(getContext(), R.string.smth_wrong, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void logout() {
        defineUser.clearData();

        Intent intent = new Intent(requireActivity().getApplicationContext(), MainAuth_Activity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}