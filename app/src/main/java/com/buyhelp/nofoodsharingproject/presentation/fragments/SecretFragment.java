/**
 * Класс {@code SecretFragment} - фрагмент, необходимый для демонстрации приложения
 * В production не нужен, от него можно будет избавиться
 * @author Кулагин Александр
 */

package com.buyhelp.nofoodsharingproject.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.buyhelp.nofoodsharingproject.domain.helpers.DefineUser;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;

public class SecretFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final EditText input = new EditText(requireContext());

        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        DefineUser defineUser = app.getHelpersComponent().getDefineUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Подключить", (dialog, id) -> defineUser.initBaseRetrofitPath(input.getText().toString())).setNegativeButton("Отмена", (dialog, id) -> defineUser.setDefaultBasePathForRetrofit());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        return builder.create();
    }
}