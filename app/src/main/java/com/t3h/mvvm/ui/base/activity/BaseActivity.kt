package com.t3h.mvvm.ui.base.activity

import androidx.appcompat.app.AppCompatActivity
import com.t3h.mvvm.ui.base.fragment.BaseFragment

open class BaseActivity : AppCompatActivity(){

    fun onBackPressRoot(){
        super.onBackPressed()
    }

    fun getCurrentBaseFragment():BaseFragment?{
        for (fragment in supportFragmentManager.fragments) {
            if (fragment != null && fragment.isVisible && fragment is BaseFragment){
                return fragment as BaseFragment
            }
        }
        return null
    }

    open override fun onBackPressed() {
        val fr = getCurrentBaseFragment()
        if (fr != null ){
            fr.onBackPress()
            return
        }
        onBackPressRoot()
    }
}