package com.example.nofoodsharingproject.data.api.adverts;

import com.example.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseActiveAdverts;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.example.nofoodsharingproject.data.api.adverts.dto.RequestTakingProduct;
import com.example.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.example.nofoodsharingproject.models.Advertisement;

import retrofit2.Call;

public class AdvertsRepository {

    public static Call<ResponseActiveAdverts> getListAdverts(String market) {

        return AdvertsApiService.getInstance().getListAdvertisements(market);
    }

    public static Call<Advertisement> createAdvert(Advertisement advert) {
        return AdvertsApiService.getInstance().createAdvert(advert);
    }

    public static Call<RequestDoneAdvert> makeDoneAdvert(RequestDoneAdvert req) {
        return AdvertsApiService.getInstance().makeDoneAdvert(req);
    }

    public static Call<Advertisement> getOwnAdvert(String authorID) {
        return AdvertsApiService.create().getOwnAdvert(authorID);
    }

    public static Call<ResponseDeleteAdvert> deleteAdvert(String advertID) {
        return AdvertsApiService.getInstance().deleteAdvert(advertID);
    }

    public static Call<Advertisement> getAdvertByID(String advertID) {
        return AdvertsApiService.getInstance().getAdvertByID(advertID);
    }

    public static Call<Advertisement> takingProducts(String authorID) {
        return AdvertsApiService.getInstance().takingProducts(new RequestTakingProduct(authorID));
    }

    public static Call<ResponseHistoryAdverts> findSetterAdvertisements(String userID) {
        return AdvertsApiService.getInstance().findSetterAdvertisements(userID);
    }

    public static Call<Advertisement> getRandomAdvertByMarket(String market) {
        return AdvertsApiService.getInstance().getRandomAdvertByMarket(market);
    }
}
