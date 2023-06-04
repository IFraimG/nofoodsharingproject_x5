package com.buyhelp.nofoodsharingproject.data.api.giver;

import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Giver;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GiverAPI {
    @PUT("/giver/edit_profile")
    Call<Giver> editProfile(@Body RequestNeedyEditProfile requestNeedyEditProfile);

    @GET("/giver/get_token/{authorID}")
    Call<ResponseFCMToken> getFCMtoken(@Path("authorID") String authorID);

    @PUT("/giver/change_token")
    Call<ResponseBody> changeToken(@Body RequestChangeToken requestChangeToken);
}
