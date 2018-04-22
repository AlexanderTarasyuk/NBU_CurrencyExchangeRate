package com.example.alextarasyuk.nbu_currencyexchangerate.rest;

import com.example.alextarasyuk.nbu_currencyexchangerate.model.CurrencyModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NbuApiService {


    @GET("statdirectory/exchange")
    Call<List<CurrencyModel>> getExchangeRate();

    @GET
    Observable <List<CurrencyModel>> getCurrency(
            @Url String url
    );


}
