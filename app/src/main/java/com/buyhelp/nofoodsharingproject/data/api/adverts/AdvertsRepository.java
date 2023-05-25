package com.buyhelp.nofoodsharingproject.data.api.adverts;

import android.content.Context;

import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseActiveAdverts;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.RequestTakingProduct;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.buyhelp.nofoodsharingproject.models.Advertisement;

import retrofit2.Call;

public class AdvertsRepository {

    public static Call<ResponseActiveAdverts> getListAdverts(Context ctx, String market) {
        return AdvertsApiService.getInstance(ctx).getListAdvertisements(market);
    }

    public Call<Advertisement> createAdvert(Context ctx, Advertisement advert) {
        return AdvertsApiService.getInstance(ctx).createAdvert(advert);
    }

    public Call<RequestDoneAdvert> makeDoneAdvert(Context ctx, RequestDoneAdvert req) {
        return AdvertsApiService.getInstance(ctx).makeDoneAdvert(req);
    }

    public Call<Advertisement> getOwnAdvert(Context ctx, String authorID) {
        return AdvertsApiService.create(ctx).getOwnAdvert(authorID);
    }

    public Call<ResponseDeleteAdvert> deleteAdvert(Context ctx, String advertID) {
        return AdvertsApiService.getInstance(ctx).deleteAdvert(advertID);
    }

    public Call<Advertisement> getAdvertByID(Context ctx, String advertID) {
        return AdvertsApiService.getInstance(ctx).getAdvertByID(advertID);
    }

    public Call<Advertisement> takingProducts(Context ctx, String authorID) {
        return AdvertsApiService.getInstance(ctx).takingProducts(new RequestTakingProduct(authorID));
    }

    public Call<ResponseHistoryAdverts> findSetterAdvertisements(Context ctx, String userID) {
        return AdvertsApiService.getInstance(ctx).findSetterAdvertisements(userID);
    }

    public Call<Advertisement> getRandomAdvertByMarket(Context ctx, String market) {
        return AdvertsApiService.getInstance(ctx).getRandomAdvertByMarket(market);
    }
}
