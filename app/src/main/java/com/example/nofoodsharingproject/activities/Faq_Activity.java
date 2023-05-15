package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

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

    private Faq[] getterQuesitons = new Faq[]{
            new Faq("В течении какого времени можно забрать продукты?", "За 5 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 10 часов"),
    };
    private Faq[] setterQuesitons = new Faq[]{
            new Faq("В течении какого времени можно забрать продукты?", "За 9 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
            new Faq("В течении какого времени можно забрать продукты?", "За 6 часов"),
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFaqBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setterRecycler = binding.faqSetterList;
        FaqAdapter faqAdapter = new FaqAdapter(getApplicationContext());
        faqAdapter.loadFaq(Arrays.asList(setterQuesitons));

        setterRecycler.setAdapter(faqAdapter);
    }
}
