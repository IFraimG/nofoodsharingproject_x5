package com.buyhelp.nofoodsharingproject.presentation.fragments.getter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.GetterActivity;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterCreateNewAdvertismentBinding;
import com.buyhelp.nofoodsharingproject.presentation.view_models.GetterNewAdvertViewModel;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class GetterNewAdvertFragment extends Fragment {
    private FragmentGetterCreateNewAdvertismentBinding binding;
    private ArrayAdapter<String> arrayAdapterChoosenItems;
    private GetterNewAdvertViewModel viewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentGetterCreateNewAdvertismentBinding.inflate(getLayoutInflater());

        viewModel = new ViewModelProvider(requireActivity(),
                (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()))
                .get(GetterNewAdvertViewModel.class);

        ArrayAdapter<String> arrayAdapterChoose = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_name, viewModel.getProductItems());
        arrayAdapterChoosenItems = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_done_name, new ArrayList<>());

        binding.productChoice.setAdapter(arrayAdapterChoose);
        binding.productChoosenItems.setAdapter(arrayAdapterChoosenItems);

        binding.productChoice.setOnItemClickListener((parent, view, position, id) -> chooseItem(position));
        binding.productChoosenItems.setOnItemClickListener((parent, view, position, id) -> removeItem(position));

        binding.buttonBack.setOnClickListener(v ->  Navigation.findNavController(v).navigate(R.id.action_getterNewAdvertFragment_to_getterAdvrsF));
        binding.readyToCreate.setOnClickListener(View -> createAdvert());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof GetterActivity) {
            ((GetterActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof GetterActivity) {
            ((GetterActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    private void createAdvert() {
        if (viewModel.getUserItems().size() == 0)
            Toast.makeText(requireContext(), R.string.add_to_list_product, Toast.LENGTH_SHORT).show();
        else if (binding.getterAdvertInputTitle.getText().toString().length() == 0) {
            Toast.makeText(requireContext(), R.string.edit_name, Toast.LENGTH_SHORT).show();
        } else if (viewModel.getUserItems().size() > 3) {
            Toast.makeText(requireContext(), R.string.many_products, Toast.LENGTH_SHORT).show();
        } else {
            binding.readyToCreate.setEnabled(false);
            viewModel.createAdvert(binding.getterAdvertInputTitle.getText().toString()).observe(requireActivity(), advertisement -> {
                if (advertisement != null) {
                    Navigation.findNavController(requireView()).navigate(R.id.action_getterNewAdvertFragment_to_getterAdvrsF);
                } else binding.readyToCreate.setEnabled(true);
            });
        }
    }

    private void removeItem(int position) {
        viewModel.removeItem(position).observe(requireActivity(), strings -> {
            arrayAdapterChoosenItems.clear();
            arrayAdapterChoosenItems.addAll(strings);
            arrayAdapterChoosenItems.notifyDataSetChanged();

            Toast.makeText(requireContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
        });
    }

    private void chooseItem(int position) {
        viewModel.updateItem(position).observe(requireActivity(), userItems -> {
            if (userItems.size() != arrayAdapterChoosenItems.getCount()) {
                arrayAdapterChoosenItems.clear();
                arrayAdapterChoosenItems.addAll(userItems);
                arrayAdapterChoosenItems.notifyDataSetChanged();
            }
        });
    }
}
