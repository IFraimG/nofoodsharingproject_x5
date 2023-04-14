package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nofoodsharingproject.R;

public class SetterAdvertAC extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setter_advert);

        String advertID = getIntent().getStringExtra("advertID");
//        AdvertsApiService.getInstance().getListAdvertisements().enqueue(new Callback<List<Advertisement>>() {
//            @Override
//            public void onResponse(Call<List<Advertisement>> call, Response<List<Advertisement>> response) {
//                String result = response.body().toString();
//            }
//
//            @Override
//            public void onFailure(Call<List<Advertisement>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

    public void backHome(View view) {
//        Intent intent = new Intent();
    }
}
