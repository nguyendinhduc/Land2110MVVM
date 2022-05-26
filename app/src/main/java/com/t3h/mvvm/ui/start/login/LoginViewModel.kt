package com.t3h.mvvm.ui.start.login

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.t3h.mvvm.model.RetrofitFactor
import com.t3h.mvvm.model.ServiceAppCallApi
import com.t3h.mvvm.model.request.LoginRequest
import com.t3h.mvvm.model.response.ErrorResponse
import com.t3h.mvvm.model.response.LoginResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//View = Activity/Fragment
//ViewModel: ViewModel
//model: Retrofit/Database
class LoginViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val loginData : MutableLiveData<LoginResponse>
    val errorResponse : MutableLiveData<ErrorResponse>
    val isLoading: ObservableBoolean

    constructor() {
        service = RetrofitFactor.createRetrofit()
        loginData = MutableLiveData()
        errorResponse = MutableLiveData()
        isLoading = ObservableBoolean()
    }

    fun login(username: String, password: String) {
        isLoading.set(true)

        service.login(
            LoginRequest(username, password)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loginData.value = it
                    isLoading.set(false)
                },
                {
                    if (it is HttpException){
                        //lay data loi tra ve
                        val contentError = (it as HttpException).response().errorBody()?.string()
                        val error = Gson().fromJson<ErrorResponse>(contentError, ErrorResponse::class.java)
                        errorResponse.value = error
                    }
                    isLoading.set(false)
                })
    }
}