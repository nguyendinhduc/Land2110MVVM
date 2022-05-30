package com.t3h.mvvm.ui.main

import android.os.Bundle
import com.t3h.mvvm.R
import com.t3h.mvvm.socket.SocketManager
import com.t3h.mvvm.ui.base.activity.BaseActivity
import com.t3h.mvvm.ui.base.fragment.BaseFragment
import com.t3h.mvvm.ui.main.chat.ChatFragment
import com.t3h.mvvm.ui.main.friend.FriendFragment

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.content, FriendFragment(), FriendFragment::class.java.name)
            .commit()
    }

    fun openChatFragment(friendId:Int, friendAvatar:String,
                         fullName:String,
                         fgHide:BaseFragment){
        val fg = ChatFragment()
        val arg = Bundle()
        arg.putInt("FRIEND_ID", friendId)
        arg.putString("FRIEND_AVATAR", friendAvatar)
        arg.putString("FRIEND_FULLNAME", fullName)
        fg.arguments = arg
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left, R.anim.open_to_right, R.anim.exit_to_right)
            .hide(fgHide)
            .add(R.id.content, fg, ChatFragment::class.java.name)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        SocketManager.getInstance().disconnect()
        super.onDestroy()
    }
}