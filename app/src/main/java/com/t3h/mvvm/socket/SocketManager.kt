package com.t3h.mvvm.socket

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.t3h.mvvm.common.CommonApp
import com.t3h.mvvm.model.RetrofitFactor
import com.t3h.mvvm.model.response.MessageChatResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketManager {
    companion object {
        val TAG = SocketManager::class.java.name
        private var instance: SocketManager? = null
        fun getInstance(): SocketManager {
            if (instance == null) {
                instance = SocketManager()
            }
            return instance!!
        }
    }

    private var socket: Socket? = null
    private val gson: Gson
    val messageReceivedModel = MutableLiveData<MessageChatResponse>()
    val messageSentModel = MutableLiveData<MessageChatResponse>()

    private constructor() {
        gson = RetrofitFactor.getGson()
    }

    fun disconnect() {
        if (socket != null) {
            socket!!.disconnect()
            socket = null
        }
    }

    fun connect() {
        try {
            socket = IO.socket("http://192.168.1.3:1901")
            socket!!.on(Socket.EVENT_CONNECT) { args ->
                socket!!.emit(
                    "connected", CommonApp.userId.toString()
                )
            }
            socket!!.on(
                Socket.EVENT_DISCONNECT
            ) { args -> Log.d(SocketManager.TAG, "EVENT_DISCONNECT: $args") }
            socket!!.on(
                Socket.EVENT_CONNECT_ERROR
            ) { args -> Log.d(SocketManager.TAG, "EVENT_CONNECT_ERROR: $args") }
            socket!!.on(
                "message"
            ) { args -> reciveMessage(args[0] as String) }
            socket!!.on(
                "sent"
            ) { args -> sentMessage(args[0] as String) }
            socket!!.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun sendMessage(msg: MessageChatResponse): Boolean {
        if (socket != null && socket!!.connected()) {
            socket!!.emit("message",
                RetrofitFactor.getGson().toJson(msg))
            return true
        }
        return false

    }

    fun sendMessage(toJson: String): Boolean {
        if (socket != null && socket!!.connected()) {
            socket!!.emit("message", toJson)
            return true
        }
        return false

    }

    @SuppressLint("CheckResult")
    fun reciveMessage(messageString: String) {
        val message = gson.fromJson(messageString, MessageChatResponse::class.java)
        messageReceivedModel.postValue(message)
        Handler(Looper.getMainLooper()).postDelayed({
            messageSentModel.postValue(null)
        }, 100)
    }

    @SuppressLint("CheckResult")
    fun sentMessage(messageString: String) {
        val message = gson.fromJson(messageString, MessageChatResponse::class.java)
        messageSentModel.postValue(message)
        Handler(Looper.getMainLooper()).postDelayed({
            messageSentModel.postValue(null)
        }, 100)

    }


}