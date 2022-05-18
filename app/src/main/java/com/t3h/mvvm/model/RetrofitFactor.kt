package com.t3h.mvvm.model

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactor {
    fun createRetrofit(): ServiceAppCallApi {
        return Retrofit.Builder().baseUrl("https://land2110e.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ServiceAppCallApi::class.java)
    }
}