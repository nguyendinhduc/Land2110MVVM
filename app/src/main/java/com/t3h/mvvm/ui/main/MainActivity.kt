package com.t3h.mvvm.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.t3h.mvvm.R
import com.t3h.mvvm.ui.main.friend.FriendFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.content, FriendFragment(), FriendFragment::class.java.name)
            .commit()
    }
}