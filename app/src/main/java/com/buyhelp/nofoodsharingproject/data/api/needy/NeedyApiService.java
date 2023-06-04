package com.buyhelp.nofoodsharingproject.data.api.needy;

import com.buyhelp.nofoodsharingproject.data.api.RetrofitService;


public class NeedyApiService {
    private final NeedyAPI needyAPI;

    public NeedyApiService(RetrofitService retrofitService) {
        needyAPI = retrofitService.getInstance().create(NeedyAPI.class);
    }

    public NeedyAPI getNeedyAPI() {
        return needyAPI;
    }
}
