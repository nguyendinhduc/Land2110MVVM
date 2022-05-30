package com.t3h.mvvm.model

import com.t3h.mvvm.model.request.LoginRequest
import com.t3h.mvvm.model.response.FriendResponse
import com.t3h.mvvm.model.response.LoginResponse
import com.t3h.mvvm.model.response.MessageChatResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface ServiceAppCallApi {
    @POST("/users/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<LoginResponse>

    @GET("/api/friends")
    fun getFriend(
        @Query("status") status: String
    ): Observable<MutableList<FriendResponse>>

    @GET("/api/messages")
    fun getMessage(
        @Query("friendId") friendId:Int
    ):Observable<MutableList<MessageChatResponse>>

    @Multipart
    @POST("/image")
    fun postImage(
        @Part imageFile: MultipartBody.Part
    ): Observable<String>
}