package com.t3h.mvvm.common

import android.content.Context

object SharedPreferenceCommon {

    fun saveUserToken(context: Context, token: String) {
        val share = context.getSharedPreferences("user.xml", Context.MODE_PRIVATE)
        val edt = share.edit()
        edt.putString("TOKEN", token)
        edt.apply()
    }

    fun readUserToken(context: Context): String {
        val token = context.getSharedPreferences("user.xml", Context.MODE_PRIVATE)
            .getString("TOKEN", "")
        if (token == null) {
            return ""
        }
        return token
    }
}