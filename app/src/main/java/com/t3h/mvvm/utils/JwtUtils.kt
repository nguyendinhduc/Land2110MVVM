package com.t3h.mvvm.utils

import com.auth0.android.jwt.JWT

object JwtUtils {
    fun getAttribute(token: String, key: String): String? {
        return JWT(token).getClaim(key).asString()
    }
}