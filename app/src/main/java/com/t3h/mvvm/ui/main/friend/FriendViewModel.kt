package com.t3h.mvvm.ui.main.friend

import com.t3h.mvvm.model.response.FriendResponse

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.t3h.mvvm.common.CommonApp
import com.t3h.mvvm.common.SharedPreferenceCommon
import com.t3h.mvvm.model.RetrofitFactor
import com.t3h.mvvm.model.ServiceAppCallApi
import com.t3h.mvvm.model.response.ErrorResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FriendViewModel : ViewModel {
    private val service: ServiceAppCallApi
    val friendsModel: MutableLiveData<MutableList<FriendResponse>>
    val errorResponse: MutableLiveData<ErrorResponse>
    val isLoading: MutableLiveData<Boolean>

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        friendsModel = MutableLiveData()
        errorResponse = MutableLiveData()
        isLoading = MutableLiveData()
    }

    fun getFriends() {
        isLoading.value = true

        service.getFriend(
            "accepted"
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    friendsModel.value = it
                    isLoading.value = false
                },
                {
                    if (it is HttpException) {
                        //lay data loi tra ve
                        val contentError = (it as HttpException).response().errorBody()?.string()
                        val error =
                            Gson().fromJson<ErrorResponse>(contentError, ErrorResponse::class.java)
                        errorResponse.value = error
                    }
                    isLoading.value = false
                })
    }
}