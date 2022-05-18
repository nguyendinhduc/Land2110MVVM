package com.t3h.mvvm.model

import com.t3h.mvvm.model.request.LoginRequest
import com.t3h.mvvm.model.response.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceAppCallApi {
    @POST("/users/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<LoginResponse>
}