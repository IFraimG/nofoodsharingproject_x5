package com.example.nofoodsharingproject.data.api.adverts;

import android.content.Context;

import com.example.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseActiveAdverts;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.example.nofoodsharingproject.data.api.adverts.dto.RequestTakingProduct;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.example.nofoodsharingproject.models.Advertisement;

import retrofit2.Call;

public class AdvertsRepository {

    public static Call<ResponseActiveAdverts> getListAdverts(Context ctx, String market) {

        return AdvertsApiService.getInstance(ctx).getListAdvertisements(market);
    }

    public static Call<Advertisement> createAdvert(Context ctx, Advertisement advert) {
        return AdvertsApiService.getInstance(ctx).createAdvert(advert);
    }

    public static Call<RequestDoneAdvert> makeDoneAdvert(Context ctx, RequestDoneAdvert req) {
        return AdvertsApiService.getInstance(ctx).makeDoneAdvert(req);
    }

    public static Call<Advertisement> getOwnAdvert(Context ctx, String authorID) {
        return AdvertsApiService.create(ctx).getOwnAdvert(authorID);
    }

    public static Call<ResponseDeleteAdvert> deleteAdvert(Context ctx, String advertID) {
        return AdvertsApiService.getInstance(ctx).deleteAdvert(advertID);
    }

    public static Call<Advertisement> getAdvertByID(Context ctx, String advertID) {
        return AdvertsApiService.getInstance(ctx).getAdvertByID(advertID);
    }

    public static Call<Advertisement> takingProducts(Context ctx, String authorID) {
        return AdvertsApiService.getInstance(ctx).takingProducts(new RequestTakingProduct(authorID));
    }

    public static Call<ResponseHistoryAdverts> findSetterAdvertisements(Context ctx, String userID) {
        return AdvertsApiService.getInstance(ctx).findSetterAdvertisements(userID);
    }

    public static Call<Advertisement> getRandomAdvertByMarket(Context ctx, String market) {
        return AdvertsApiService.getInstance(ctx).getRandomAdvertByMarket(market);
    }
}
