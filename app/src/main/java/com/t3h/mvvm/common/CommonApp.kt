package com.t3h.mvvm.common

import android.app.Application
import android.content.Context

class CommonApp : Application() {
    companion object {
        private var context: Context? = null
        var userId = -1
        var avatar = ""
        fun getContextApp(): Context {
            return context!!
        }
    }



    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}