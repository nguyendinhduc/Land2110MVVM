package com.t3h.mvvm.model

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor

import okhttp3.Request


object RetrofitFactor {
    fun createRetrofit(): ServiceAppCallApi {
        return Retrofit.Builder()
//            .baseUrl("https://land2110e.herokuapp.com")
            .baseUrl("http://192.168.1.3:1900")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(ServiceAppCallApi::class.java)
    }

    fun createRetrofitToken(token:String): ServiceAppCallApi {
        val httpClient  = OkHttpClient.Builder()
        httpClient.networkInterceptors().add(Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            requestBuilder.header("Authorization", "Bearer "+token)
            chain.proceed(requestBuilder.build())
        })
        return Retrofit.Builder()
//            .baseUrl("https://land2110e.herokuapp.com")
            .baseUrl("http://192.168.1.3:1900")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build().create(ServiceAppCallApi::class.java)
    }
}