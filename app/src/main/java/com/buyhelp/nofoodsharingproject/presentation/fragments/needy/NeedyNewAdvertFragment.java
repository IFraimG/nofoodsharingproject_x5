package com.buyhelp.nofoodsharingproject.presentation.fragments.needy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentNeedyCreateNewAdvertismentBinding;
import com.buyhelp.nofoodsharingproject.presentation.ApplicationCore;
import com.buyhelp.nofoodsharingproject.presentation.activities.NeedyActivity;
import com.buyhelp.nofoodsharingproject.presentation.viewmodels.needy.NeedyNewAdvertViewModel;
import com.buyhelp.nofoodsharingproject.presentation.factories.needy.NeedyNewAdvertFactory;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class NeedyNewAdvertFragment extends Fragment {
    private FragmentNeedyCreateNewAdvertismentBinding binding;
    private WeakReference<FragmentNeedyCreateNewAdvertismentBinding> mBinding;
    private ArrayAdapter<String> arrayAdapterChoosenItems;
    private NeedyNewAdvertViewModel viewModel;
    private AdvertsRepository advertsRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationCore app = (ApplicationCore) requireActivity().getApplication();
        advertsRepository = app.getAppComponent().getAdvertsRepository();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentNeedyCreateNewAdvertismentBinding.inflate(getLayoutInflater());
        mBinding = new WeakReference<>(binding);

        viewModel = new ViewModelProvider(requireActivity(),
                new NeedyNewAdvertFactory(requireActivity().getApplication(), advertsRepository))
                .get(NeedyNewAdvertViewModel.class);

        ArrayAdapter<String> arrayAdapterChoose = new ArrayAdapter<>(requireContext(), R.layout.item_needy_product_name, viewModel.getProductItems());
        arrayAdapterChoosenItems = new ArrayAdapter<>(requireContext(), R.layout.item_needy_product_done_name, new ArrayList<>());

        binding.productChoice.setAdapter(arrayAdapterChoose);
        binding.productChoosenItems.setAdapter(arrayAdapterChoosenItems);

        binding.buttonBack.setOnClickListener(v ->  Navigation.findNavController(v).navigate(R.id.action_needyNewAdvertFragment_to_needyAdvrsF));

        binding.productChoice.setOnItemClickListener((parent, view, position, id) -> chooseItem(position));
        binding.productChoosenItems.setOnItemClickListener((parent, view, position, id) -> removeItem(position));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View createdView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(createdView, savedInstanceState);

        viewModel.isNavigate().observe(getViewLifecycleOwner(), isNavigate -> {
            if (isNavigate) {
                Navigation.findNavController(requireView()).navigate(R.id.action_needyNewAdvertFragment_to_needyAdvrsF);
                viewModel.cancelNavigate();
            }
        });

        binding.readyToCreate.setOnClickListener(View -> createAdvert());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof NeedyActivity) {
            ((NeedyActivity) requireActivity()).setBottomNavigationVisibility(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof NeedyActivity) {
            ((NeedyActivity) requireActivity()).setBottomNavigationVisibility(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding.clear();
    }


    private void createAdvert() {
        if (viewModel.getUserItems().size() == 0)
           Toast.makeText(requireContext(), getString(R.string.add_to_list_product), Toast.LENGTH_SHORT).show();
        else if (binding.needyAdvertInputTitle.getText().toString().length() == 0) {
            Toast.makeText(requireContext(), getString(R.string.edit_name), Toast.LENGTH_SHORT).show();
        } else if (viewModel.getUserItems().size() > 3) {
            Toast.makeText(requireContext(), getString(R.string.many_products), Toast.LENGTH_SHORT).show();
        } else {
            binding.readyToCreate.setEnabled(false);
            viewModel.createAdvert(binding.needyAdvertInputTitle.getText().toString()).observe(requireActivity(), advertisement -> {
                int code = viewModel.getStatusCode();
                if (code > 299) Toast.makeText(requireContext(), getString(R.string.problems), Toast.LENGTH_SHORT).show();

                if (advertisement != null) Toast.makeText(requireContext(), getString(R.string.advert_sucesfully_create), Toast.LENGTH_SHORT).show();
                else binding.readyToCreate.setEnabled(true);
            });
        }
    }

    private void removeItem(int position) {
        viewModel.removeItem(position).observe(requireActivity(), strings -> {
            arrayAdapterChoosenItems.clear();
            arrayAdapterChoosenItems.addAll(strings);
            arrayAdapterChoosenItems.notifyDataSetChanged();
        });
    }

    private void chooseItem(int position) {
        if (viewModel.getUserItems().size() > 3) {
            Toast.makeText(requireContext(), getString(R.string.lot_of_product), Toast.LENGTH_SHORT).show();
        } else if (!viewModel.isContainsItem(position)) {
            viewModel.updateItem(position).observe(requireActivity(), userItems -> {
                if (userItems.size() != arrayAdapterChoosenItems.getCount()) {
                    arrayAdapterChoosenItems.clear();
                    arrayAdapterChoosenItems.addAll(userItems);
                    arrayAdapterChoosenItems.notifyDataSetChanged();
                }
            });
        } else {
            Toast.makeText(requireContext(), getString(R.string.this_product_added), Toast.LENGTH_SHORT).show();
        }
    }
}
