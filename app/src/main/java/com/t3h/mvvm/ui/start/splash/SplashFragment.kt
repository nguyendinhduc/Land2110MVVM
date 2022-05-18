package com.t3h.mvvm.ui.start.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.t3h.mvvm.databinding.FragmentSplashBinding
import com.t3h.mvvm.ui.base.fragment.BaseFragment
import com.t3h.mvvm.ui.start.StartActivity

class SplashFragment : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSplashBinding.inflate(inflater, container, false)
        Handler().postDelayed({
            (requireActivity() as StartActivity).openLoginFragment()
        }, 3000)
        return binding.root

    }
}