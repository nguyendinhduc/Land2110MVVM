package com.t3h.mvvm.ui.base.fragment

import androidx.fragment.app.Fragment
import com.t3h.mvvm.ui.base.activity.BaseActivity

open abstract class BaseFragment : Fragment(){

    open fun onBackPress(){
        (requireActivity() as BaseActivity).onBackPressRoot()
    }
}