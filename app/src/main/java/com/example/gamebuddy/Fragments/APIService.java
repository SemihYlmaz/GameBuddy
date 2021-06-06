package com.example.gamebuddy.Fragments;

import com.example.gamebuddy.Notifications.MyResponse;
import com.example.gamebuddy.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService
{
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAKFMY0O4:APA91bGuaItS2tc7HgTV6d_rFI4m7aJFCIOLsImvKZagq79hWXKglL3qTUoHGhQG7mvspXF-oUkS8e-fYoMhAmJL2iGuBuT6ObsexIEClcdjQPz67LLP7D_VCX_-rLDbSRHG-TSZGDVx"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
