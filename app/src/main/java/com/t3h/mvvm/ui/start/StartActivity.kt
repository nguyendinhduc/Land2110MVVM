package com.t3h.mvvm.ui.start

import android.content.Intent
import android.os.Bundle
import com.t3h.mvvm.R
import com.t3h.mvvm.ui.base.activity.BaseActivity
import com.t3h.mvvm.ui.main.MainActivity
import com.t3h.mvvm.ui.start.login.LoginFragment
import com.t3h.mvvm.ui.start.splash.SplashFragment

class StartActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        if (intent.getStringExtra("TYPE") != null && intent.getStringExtra("TYPE").equals("OPEN_LOGIN")){
            openLoginFragment()
        }else {
            supportFragmentManager.beginTransaction()
                .add(R.id.content, SplashFragment(), SplashFragment::class.java.name)
                .commit()
        }

    }

    fun openLoginFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.content, LoginFragment(), LoginFragment::class.java.name)
            .commit()
    }

    fun openMain(){
        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}