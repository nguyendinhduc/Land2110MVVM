package com.t3h.mvvm.ui.main.chat

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.t3h.mvvm.common.CommonApp
import com.t3h.mvvm.common.SharedPreferenceCommon
import com.t3h.mvvm.model.RetrofitFactor
import com.t3h.mvvm.model.ServiceAppCallApi
import com.t3h.mvvm.model.response.ErrorResponse
import com.t3h.mvvm.model.response.MessageChatResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File
import okhttp3.RequestBody




class ChatViewModel : ViewModel {
    private val service: ServiceAppCallApi
    private val messagesModel: MutableLiveData<MutableList<MessageChatResponse>>
    val errorResponse: MutableLiveData<ErrorResponse>
    val imageResponse: MutableLiveData<String>
    val isLoading: MutableLiveData<Boolean>

    constructor() {
        service = RetrofitFactor.createRetrofitToken(
            SharedPreferenceCommon.readUserToken(CommonApp.getContextApp())
        )
        this.messagesModel = MutableLiveData()
        errorResponse = MutableLiveData()
        imageResponse = MutableLiveData()
        isLoading = MutableLiveData()
    }

    fun getMessagesModel(): MutableLiveData<MutableList<MessageChatResponse>> {
        return messagesModel
    }

    @SuppressLint("CheckResult")
    fun getMessage(friendId: Int) {
        service.getMessage(
            friendId
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    messagesModel.value = it
                    isLoading.value = false
                },
                {
                    if (it is HttpException) {
                        val contentError = (it as HttpException).response().errorBody()?.string()
                        val error =
                            Gson().fromJson<ErrorResponse>(contentError, ErrorResponse::class.java)
                        errorResponse.value = error
                    }
                    isLoading.value = false
                })
    }

    fun sendImage(path:String){
        val file = File(path)
        val requestFile =  RequestBody.create(
            "multipart/form-data".toMediaType(),
            file
        )
        val part = MultipartBody.Part.createFormData(
            "imageFile", file.getName(),
            requestFile)
        service.postImage(
            part
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    imageResponse.value=it
                    isLoading.value = false
                },
                {
                    if (it is HttpException) {
                        val contentError = (it as HttpException).response().errorBody()?.string()
                        val error =
                            Gson().fromJson<ErrorResponse>(contentError, ErrorResponse::class.java)
                        errorResponse.value = error
                    }
                    isLoading.value = false
                })
    }
}