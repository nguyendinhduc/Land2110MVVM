package com.t3h.mvvm.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.t3h.mvvm.R
import com.t3h.mvvm.common.SharedPreferenceCommon
import com.t3h.mvvm.databinding.FragmentLoginBinding
import com.t3h.mvvm.ui.base.fragment.BaseFragment
import com.t3h.mvvm.ui.start.StartActivity

class LoginFragment : BaseFragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = LoginViewModel()
        binding.data = viewModel

        binding.btnLogin.setOnClickListener(this)
        registerLiveData()
        return binding.root
    }

    private fun registerLiveData() {
        //dang khi su kien khi thay doi du lieu trong loginData
        viewModel.loginData.observe(getViewLifecycleOwner(), Observer {
            //roi vao day: mo man hinh Hom
//                Toast.makeText(requireContext(),"Login success", Toast.LENGTH_SHORT).show()

            SharedPreferenceCommon.saveUserToken(requireContext(), it.token)

            (requireActivity() as StartActivity).openMain()

        })

        viewModel.errorResponse.observe(getViewLifecycleOwner(), Observer {
            //roi vao day: mo man hinh Hom
            Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login -> {
                viewModel.login(
                    binding.edtUsername.text.toString(),
                    binding.edtPassword.text.toString(),
                )
            }
        }
    }
}