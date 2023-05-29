package com.buyhelp.nofoodsharingproject.data.api.notifications;

import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.RequestSetRead;
import com.buyhelp.nofoodsharingproject.data.api.notifications.dto.ResponseNotificationsList;
import com.buyhelp.nofoodsharingproject.data.models.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InnerNotificationAPI {
    @GET("/notifications/get_notifications")
    Call<ResponseNotificationsList> getNotifications(@Query("userID") String userID, @Query("typeOfUser") String typeOfUser);

    @GET("/notifications/get_notification_one/{notificationID}")
    Call<Notification> getNotificationOne(@Path("notificationID") String notificationID);

    @POST("/notifications/create_notification")
    Call<Notification> createNotification(@Body Notification notification);

    @PUT("/notifications/set_read")
    Call<Notification> setRead(@Body RequestSetRead requestSetRead);
}
