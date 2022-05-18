package com.t3h.mvvm.model

import android.util.Log
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFactory
import com.theostanton.rxwebsocket.RxWebSocket
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ChatSocketManager {
    private lateinit var webSocket : WebSocket
    fun init(){
        webSocket = WebSocketFactory().createSocket("ws://192.168.1.3/endpoint")

        webSocket.addListener(object: WebSocketAdapter(){
            override fun onTextMessage(websocket: WebSocket?, text: String?) {

            }
        })
        Observable.create<Boolean> {
            webSocket.connect()
            Log.d("ChatSocketManager", "init: "+ webSocket.isOpen.toString())
            it.onNext(true)
            it.onComplete()
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()


    }
    fun sendMessage(){

    }

}