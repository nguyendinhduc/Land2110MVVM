package com.t3h.mvvm.ui.main.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.t3h.mvvm.common.CommonApp
import com.t3h.mvvm.databinding.ItemMessageImageReceiverBinding
import com.t3h.mvvm.databinding.ItemMessageImageSenderBinding
import com.t3h.mvvm.databinding.ItemMessageTextReceiverBinding
import com.t3h.mvvm.databinding.ItemMessageTextSenderBinding
import com.t3h.mvvm.model.response.MessageChatResponse
import com.t3h.mvvm.utils.CommonUtils

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    companion object {
        val IMAGE_SENDER = 1
        val IMAGE_RECEIVER = 2
        val TEXT_SENDER = 3
        val TEXT_RECEIVER = 4
    }

    private val inter: IChat
    private val friendAvatar: String

    constructor(inter: IChat, friendAvatar: String) {
        this.inter = inter
        this.friendAvatar = friendAvatar
    }

    override fun getItemViewType(position: Int): Int {
        val item = this.inter.getItem(position)
        if (item.type.equals("image")) {
            if (item.senderId == CommonApp.userId) {
                return IMAGE_SENDER
            } else {
                return IMAGE_RECEIVER
            }
        } else {
            if (item.senderId == CommonApp.userId) {
                return TEXT_SENDER
            } else {
                return TEXT_RECEIVER
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == IMAGE_SENDER) {
            return ItemMessageImageSenderHolder(
                ItemMessageImageSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        if (viewType == IMAGE_RECEIVER) {
            val holder = ItemMessageImageReceiverHolder(
                ItemMessageImageReceiverBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            CommonUtils.loadNormalImageLink(holder.binding.ivAvatar, friendAvatar)
            return holder
        }
        if (viewType == TEXT_SENDER) {
            return ItemMessageTextSenderHolder(
                ItemMessageTextSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        val holder = ItemMessageTextReceiverHolder(
            ItemMessageTextReceiverBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        CommonUtils.loadNormalImageLink(holder.binding.ivAvatar, friendAvatar)
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = this.inter.getItem(position)
        var avatar: ImageView? = null
        when (getItemViewType(position)) {
            IMAGE_SENDER -> {
                (holder as ItemMessageImageSenderHolder).binding.data = item
            }
            IMAGE_RECEIVER -> {
                (holder as ItemMessageImageReceiverHolder).binding.data = item
                avatar = (holder as ItemMessageImageReceiverHolder).binding.ivAvatar
            }
            TEXT_SENDER -> {
                (holder as ItemMessageTextSenderHolder).binding.data = item
            }
            else -> {
                (holder as ItemMessageTextReceiverHolder).binding.data = item
                avatar = holder.binding.ivAvatar
            }
        }



        if (avatar != null) {
            if (position == this.inter.getCount() - 1) {
                avatar.visibility = View.VISIBLE
            } else {
                if (item.senderId == this.inter.getItem(position + 1).senderId) {
                    avatar.visibility = View.INVISIBLE
                } else {
                    avatar.visibility = View.VISIBLE
                }
            }
        }
    }


    override fun getItemCount() = this.inter.getCount()

    interface IChat {
        fun getCount(): Int
        fun getItem(position: Int): MessageChatResponse
    }

    class ItemMessageTextSenderHolder(val binding: ItemMessageTextSenderBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ItemMessageTextReceiverHolder(val binding: ItemMessageTextReceiverBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ItemMessageImageSenderHolder(val binding: ItemMessageImageSenderBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ItemMessageImageReceiverHolder(val binding: ItemMessageImageReceiverBinding) :
        RecyclerView.ViewHolder(binding.root)
}

