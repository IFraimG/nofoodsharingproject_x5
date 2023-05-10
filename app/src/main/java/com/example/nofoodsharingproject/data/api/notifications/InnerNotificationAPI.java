package com.example.nofoodsharingproject.data.api.notifications;

import com.example.nofoodsharingproject.models.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface InnerNotificationAPI {
    @GET("/notifications/get_notifications/{userID}")
    Call<ResponseNotificationsList> getNotifications(@Path("userID") String userID);

    @GET("/notifications/get_notification_one/{notificationID}")
    Call<Notification> getNotificationOne(@Path("notificationID") String notificationID);

    @POST("/notifications/create_notification")
    Call<Notification> createNotification(@Body Notification notification);

    @PUT("/notifications/set_read")
    Call<Notification> setRead(@Body RequestSetRead requestSetRead);
}
