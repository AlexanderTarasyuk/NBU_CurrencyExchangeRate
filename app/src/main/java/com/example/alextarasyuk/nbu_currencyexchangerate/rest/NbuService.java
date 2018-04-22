package com.example.alextarasyuk.nbu_currencyexchangerate.rest;

import com.example.alextarasyuk.nbu_currencyexchangerate.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NbuService {

    private static NbuApiService sNbuService;

    public static NbuApiService getApi() {
        if (sNbuService == null) {

            Gson gson = new GsonBuilder()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            sNbuService = retrofit.create(NbuApiService.class);
        }
        return sNbuService;

    }
}
