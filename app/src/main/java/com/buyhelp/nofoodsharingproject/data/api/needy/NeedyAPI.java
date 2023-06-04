package com.buyhelp.nofoodsharingproject.data.api.needy;

import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.needy.dto.RequestNeedyEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Needy;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NeedyAPI {
    @PUT("/needy/edit_profile")
    Call<Needy> editProfile(@Body RequestNeedyEditProfile requestNeedyEditProfile);

    @GET("/needy/get_token/{authorID}")
    Call<ResponseFCMToken> getFCMtoken(@Path("authorID") String authorID);

    @PUT("/needy/change_token")
    Call<ResponseBody> changeToken(@Body RequestChangeToken requestChangeToken);
}
