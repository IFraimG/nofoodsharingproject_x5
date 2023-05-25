package com.buyhelp.nofoodsharingproject.data.api.setter;

import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestChangeToken;
import com.buyhelp.nofoodsharingproject.data.api.getter.dto.RequestGetterEditProfile;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseFCMToken;
import com.buyhelp.nofoodsharingproject.models.Setter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SetterAPI {
    @PUT("/setters/edit_profile")
    Call<Setter> editProfile(@Body RequestGetterEditProfile requestGetterEditProfile);

    @GET("/setters/get_token/{authorID}")
    Call<ResponseFCMToken> getFCMtoken(@Path("authorID") String authorID);

    @PUT("/setters/change_token")
    Call<ResponseBody> changeToken(@Body RequestChangeToken requestChangeToken);
}
