package com.buyhelp.nofoodsharingproject.data.api.adverts;

import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.RequestDoneAdvert;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseActiveAdverts;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseDeleteAdvert;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.RequestTakingProduct;
import com.buyhelp.nofoodsharingproject.data.api.adverts.dto.ResponseHistoryAdverts;
import com.buyhelp.nofoodsharingproject.data.models.Advertisement;

import javax.inject.Inject;

import retrofit2.Call;

public class AdvertsRepository {
    private final AdvertsAPI advertsAPI;

    @Inject
    public AdvertsRepository(AdvertsApiService apiService) {
        advertsAPI = apiService.getAdvertsAPI();
    }

    public Call<ResponseActiveAdverts> getListAdverts(String market) {
        return advertsAPI.getListAdvertisements(market);
    }

    public Call<Advertisement> createAdvert(Advertisement advert) {
        return advertsAPI.createAdvert(advert);
    }

    public Call<RequestDoneAdvert> makeDoneAdvert(RequestDoneAdvert req) {
        return advertsAPI.makeDoneAdvert(req);
    }

    public Call<Advertisement> getOwnAdvert(String authorID) {
        return advertsAPI.getOwnAdvert(authorID);
    }

    public Call<ResponseDeleteAdvert> deleteAdvert(String advertID) {
        return advertsAPI.deleteAdvert(advertID);
    }

    public Call<Advertisement> getAdvertByID(String advertID) {
        return advertsAPI.getAdvertByID(advertID);
    }

    public Call<Advertisement> takingProducts(String authorID) {
        return advertsAPI.takingProducts(new RequestTakingProduct(authorID));
    }

    public Call<ResponseHistoryAdverts> findSetterAdvertisements(String userID) {
        return advertsAPI.findSetterAdvertisements(userID);
    }

    public Call<Advertisement> getRandomAdvertByMarket(String market) {
        return advertsAPI.getRandomAdvertByMarket(market);
    }
}
