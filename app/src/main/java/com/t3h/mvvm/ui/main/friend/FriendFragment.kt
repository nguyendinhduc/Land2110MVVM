package com.t3h.mvvm.ui.main.friend

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.t3h.mvvm.common.CommonApp
import com.t3h.mvvm.common.SharedPreferenceCommon
import com.t3h.mvvm.databinding.FragmentFriendBinding
import com.t3h.mvvm.model.response.FriendResponse
import com.t3h.mvvm.ui.base.fragment.BaseFragment
import com.t3h.mvvm.ui.main.MainActivity
import com.t3h.mvvm.ui.start.StartActivity
import com.t3h.mvvm.utils.CommonUtils

class FriendFragment : BaseFragment(), FriendAdapter.IFriendAdapter {
    private lateinit var binding: FragmentFriendBinding
    private lateinit var viewModel: FriendViewModel
    private val friendResponses = mutableListOf<FriendResponse>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)
        viewModel = FriendViewModel()
        binding.data = viewModel

        binding.rcFriend.adapter = FriendAdapter(this)
        binding.rcFriend.layoutManager = LinearLayoutManager(requireContext())
        register()

        viewModel.getFriends()
        initViews()
        return binding.root
    }

    private fun initViews(){
        binding.refresh.setOnRefreshListener {
            viewModel.getFriends()
        }
        CommonUtils.loadNormalImageLink(binding.ivAvatar, CommonApp.avatar)
    }



    private fun register(){
        viewModel.friendsModel.observe(getViewLifecycleOwner(), androidx.lifecycle.Observer {
            friendResponses.clear()
            friendResponses.addAll(it)
            binding.rcFriend.adapter?.notifyDataSetChanged()
        })

        viewModel.isLoading.observe(getViewLifecycleOwner(), androidx.lifecycle.Observer {
            binding.refresh.isRefreshing = it
        })

        viewModel.errorResponse.observe(getViewLifecycleOwner(), Observer {
            if (it.status == 401){
                SharedPreferenceCommon.saveUserToken(requireContext(), "")

                val intent = Intent()
                intent.setClass(requireContext(), StartActivity::class.java)
                intent.putExtra("TYPE","OPEN_LOGIN")
                startActivity(intent)

                Toast.makeText(requireContext(),"Token expired", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }else {
                Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onClickItem(position: Int) {
        (requireActivity() as MainActivity).openChatFragment(
            getFriend(position).friendId,
            getFriend(position).avatar,
            getFriend(position).firstName+" "+getFriend(position).lastName,
            this
        )
    }

    //    override fun getCount(): Int {
//        return this.friendResponses.size
//    }

    override fun getCount() = this.friendResponses.size

    override fun getFriend(position: Int): FriendResponse {
        return this.friendResponses[position]
    }
}