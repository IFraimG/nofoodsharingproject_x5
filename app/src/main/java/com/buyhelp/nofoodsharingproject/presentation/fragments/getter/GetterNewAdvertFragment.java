package com.buyhelp.nofoodsharingproject.presentation.fragments.getter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.presentation.activities.GetterActivity;
import com.buyhelp.nofoodsharingproject.data.api.adverts.AdvertsRepository;
import com.buyhelp.nofoodsharingproject.databinding.FragmentGetterCreateNewAdvertismentBinding;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;
import com.buyhelp.nofoodsharingproject.data.models.Getter;
import com.buyhelp.nofoodsharingproject.domain.utils.DefineUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetterNewAdvertFragment extends Fragment {
    private final String[] productItems = new String[]{"Хлеб", "Картофель", "Мороженая рыба", "Сливочное масло",
            "Подсолнечное масло", "Яйца куриные", "Молоко", "Чай", "Кофе", "Соль", "Сахар",
            "Мука", "Лук", "Макаронные изделия", "Пшено", "Шлифованный рис", "Гречневая крупа",
            "Белокочанная капуста", "Морковь", "Яблоки", "Свинина", "Баранина", "Курица"};
    private final List<String> userProductItems = new ArrayList<String>();
    private FragmentGetterCreateNewAdvertismentBinding binding;
    private ArrayAdapter<String> arrayAdapterChoose;
    private ArrayAdapter<String> arrayAdapterChoosenItems;
    private DefineUser<Getter> defineUser;
    private AdvertsRepository advertsRepository;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentGetterCreateNewAdvertismentBinding.inflate(getLayoutInflater());
        defineUser = new DefineUser<>(requireActivity());
        advertsRepository = new AdvertsRepository();

        arrayAdapterChoose = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_name, this.productItems);
        arrayAdapterChoosenItems = new ArrayAdapter<>(requireContext(), R.layout.item_getter_product_done_name, this.userProductItems);

        binding.productChoice.setAdapter(arrayAdapterChoose);
        binding.productChoosenItems.setAdapter(arrayAdapterChoosenItems);

        binding.productChoice.setOnItemClickListener((parent, view, position, id) -> chooseItem(position));

        binding.productChoosenItems.setOnItemClickListener((parent, view, position, id) -> {
            userProductItems.remove(position);
            arrayAdapterChoosenItems.notifyDataSetChanged();

            Toast.makeText(requireContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
        });

        binding.buttonBack.setOnClickListener(v ->  Navigation.findNavController(v).navigate(R.id.action_getterNewAdvertFragment_to_getterAdvrsF));
        binding.readyToCreate.setOnClickListener(View -> pushData());

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

    private void pushData() {
        if (userProductItems.size() == 0)
            Toast.makeText(requireContext(), R.string.add_to_list_product, Toast.LENGTH_SHORT).show();
        else if (binding.getterAdvertInputTitle.getText().toString().length() == 0) {
            Toast.makeText(requireContext(), R.string.edit_name, Toast.LENGTH_SHORT).show();
        } else if (userProductItems.size() > 3) {
            Toast.makeText(requireContext(), R.string.many_products, Toast.LENGTH_SHORT).show();
        } else {
            Getter result = defineUser.defineGetter();
            Advertisement advertisement = new Advertisement(binding.getterAdvertInputTitle.getText().toString(), result.getX5_Id(), result.getLogin());
            advertisement.setGettingProductID(Advertisement.generateID());
            if (userProductItems.size() > 0) advertisement.setListProductsCustom(userProductItems);

            binding.readyToCreate.setEnabled(false);
            advertsRepository.createAdvert(requireContext(), advertisement).enqueue(new Callback<Advertisement>() {
                @Override
                public void onResponse(@NotNull Call<Advertisement> call, @NotNull Response<Advertisement> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(requireContext(), R.string.problems, Toast.LENGTH_SHORT).show();
                        binding.readyToCreate.setEnabled(true);
                    } else {
                        Toast.makeText(requireContext(),
                                R.string.advert_sucesfully_create, Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).navigate(R.id.action_getterNewAdvertFragment_to_getterAdvrsF);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<Advertisement> call, @NotNull Throwable t) {
                    binding.readyToCreate.setEnabled(true);
                    Toast.makeText(requireContext(),
                            R.string.smth_not_good, Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }
    }

    private void chooseItem(int position) {
        String result = productItems[position];
        if (userProductItems.size() > 3) {
            Toast.makeText(requireContext(), R.string.lot_of_product, Toast.LENGTH_SHORT).show();
        } else if (!userProductItems.contains(result)) {
            userProductItems.add(result);
            arrayAdapterChoosenItems.notifyDataSetChanged();
            Toast.makeText(requireContext(), R.string.added, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), R.string.this_product_added, Toast.LENGTH_SHORT).show();
        }
    }
}
