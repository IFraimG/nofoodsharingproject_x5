package com.buyhelp.nofoodsharingproject.data.api.getter;

import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.data.models.Getter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GetterAPI {
    @PUT("/getters/edit_profile")
    Call<Getter> editProfile(@Body RequestGetterEditProfile requestGetterEditProfile);

    @GET("/getters/get_token/{authorID}")
    Call<ResponseFCMToken> getFCMtoken(@Path("authorID") String authorID);

    @PUT("/getters/change_token")
    Call<ResponseBody> changeToken(@Body RequestChangeToken requestChangeToken);
}
