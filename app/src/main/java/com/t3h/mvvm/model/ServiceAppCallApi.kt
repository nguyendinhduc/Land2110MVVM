package com.t3h.mvvm.model

import com.t3h.mvvm.model.request.LoginRequest
import com.t3h.mvvm.model.response.FriendResponse
import com.t3h.mvvm.model.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceAppCallApi {
    @POST("/users/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<LoginResponse>

    @GET("/api/friends")
    fun getFriend(
        @Query("status") status: String
    ): Observable<MutableList<FriendResponse>>
}