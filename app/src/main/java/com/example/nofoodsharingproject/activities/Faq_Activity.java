package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nofoodsharingproject.adapters.FaqAdapter;
import com.example.nofoodsharingproject.databinding.ActivityFaqBinding;
import com.example.nofoodsharingproject.models.Faq;

import java.util.Arrays;

public class Faq_Activity extends AppCompatActivity {
    private ActivityFaqBinding binding;
    private RecyclerView setterRecycler;
    private RecyclerView getterRecycler;
    private ImageView returnButton;

    private Faq[] getterQuesitons = new Faq[]{
            new Faq("В течении какого времени можно забрать продукты?", "За 5 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 10 часов"),
    };
    private Faq[] setterQuesitons = new Faq[]{
            new Faq("В течении какого времени можно забрать продукты?", "За 9 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов")
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setterRecycler = binding.faqSetterList;
        getterRecycler = binding.faqGetterList;
        returnButton = binding.faqReturn;

        returnButton.setOnClickListener(View -> finish());

        FaqAdapter faqAdapterSetter = new FaqAdapter(getApplicationContext());
        FaqAdapter faqAdapterGetter = new FaqAdapter(getApplicationContext());

        faqAdapterSetter.loadFaq(Arrays.asList(setterQuesitons));
        faqAdapterGetter.loadFaq(Arrays.asList(getterQuesitons));

        setterRecycler.setAdapter(faqAdapterSetter);
        getterRecycler.setAdapter(faqAdapterGetter);
    }
}
