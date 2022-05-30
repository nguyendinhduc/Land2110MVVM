package com.t3h.mvvm.ui.main.chat

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.t3h.mvvm.R
import com.t3h.mvvm.common.CommonApp
import com.t3h.mvvm.databinding.FragmentChatBinding
import com.t3h.mvvm.model.response.MessageChatResponse
import com.t3h.mvvm.socket.SocketManager
import com.t3h.mvvm.ui.base.fragment.BaseFragment
import com.t3h.mvvm.utils.CommonUtils
import android.content.Intent
import android.net.Uri
import android.util.Log

import androidx.core.app.ActivityCompat.startActivityForResult
import android.provider.MediaStore
import com.t3h.mvvm.utils.PathUtil


class ChatFragment : BaseFragment(), ChatAdapter.IChat, View.OnClickListener {
    private lateinit var binding: FragmentChatBinding
    private val messages = mutableListOf<MessageChatResponse>()
    private var friendId: Int = 0
    private var friendAvatar = ""
    private var fullName = ""
    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        initsData()
        initsView()
        register()
        initsCallApi()
        return binding.root
    }

    private fun initsData() {
        friendId = requireArguments().getInt("FRIEND_ID")
        friendAvatar = requireArguments().getString("FRIEND_AVATAR", "")
        fullName = requireArguments().getString("FRIEND_FULLNAME", "")

        viewModel = ChatViewModel()
    }

    private fun initsView() {
        val llManager = LinearLayoutManager(requireContext())
        llManager.stackFromEnd = true
        binding.rcChat.layoutManager = llManager
        binding.rcChat.adapter = ChatAdapter(this, friendAvatar)

        binding.btnBack.setOnClickListener {
            onBackPress()
        }
        CommonUtils.loadNormalImageLink(binding.ivAvatar, friendAvatar)
        binding.tvFullname.setText(fullName)

        binding.refresh.setOnRefreshListener {
            viewModel.getMessage(friendId)
        }

        binding.btnSend.setOnClickListener(this)
        binding.btnImage.setOnClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun register() {
        this.viewModel.getMessagesModel().observe(viewLifecycleOwner, {
            messages.clear()
            messages.addAll(it)
            binding.rcChat.adapter?.notifyDataSetChanged()
        })

        viewModel.isLoading.observe(getViewLifecycleOwner(), {
            binding.refresh.isRefreshing = it
        })

        SocketManager.getInstance().messageSentModel.observe(getViewLifecycleOwner(), this::sentMessage)
        SocketManager.getInstance().messageReceivedModel.observe(getViewLifecycleOwner(), this::receiveMessage)
    }

    private fun initsCallApi() {
        viewModel.getMessage(friendId)
    }

    override fun getCount() = messages.size

    override fun getItem(position: Int): MessageChatResponse {
        return messages[position]
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_send -> {
                sendText()
            }
            R.id.btn_image -> {
                openGalley()
            }
        }
    }

    private fun sendText() {
        val content = binding.edtContent.text.toString()
        if ("".equals(content)) {
            Toast.makeText(requireContext(), "Please type message", Toast.LENGTH_SHORT).show()
            return
        }
        val message = MessageChatResponse()
        message.content = content
        message.senderId = CommonApp.userId
        message.receiverId = friendId
        message.type = "text"
        SocketManager.getInstance().sendMessage(message)
        binding.edtContent.setText( "")
    }

    private fun openGalley(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            val pathFile = PathUtil.getPath(requireContext(),
                data!!.data!!
            )
            viewModel.sendImage(pathFile)

        }
    }

    fun getRealPathFromURI(uri: Uri): String {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    private fun sentMessage(message: MessageChatResponse?) {
        if (message != null && message.receiverId.equals(friendId) && message.senderId.equals(CommonApp.userId)) {
            messages.add(message)
            if (getCount() >1){
                binding.rcChat.adapter?.notifyItemChanged(getCount() - 2)
            }
            binding.rcChat.adapter?.notifyItemInserted(getCount() - 1)
            binding.rcChat.smoothScrollToPosition(getCount() - 1)
        }
    }
    private fun receiveMessage(message: MessageChatResponse?) {
        if (message != null && message.receiverId.equals(CommonApp.userId) && message.senderId.equals(friendId)) {
            messages.add(message)
            if (getCount() >1){
                binding.rcChat.adapter?.notifyItemChanged(getCount() - 2)
            }
            binding.rcChat.adapter?.notifyItemInserted(getCount() - 1)
            binding.rcChat.smoothScrollToPosition(getCount() - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}