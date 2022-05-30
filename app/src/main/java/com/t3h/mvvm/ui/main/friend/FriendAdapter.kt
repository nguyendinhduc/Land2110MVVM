package com.t3h.mvvm.ui.main.friend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.t3h.mvvm.databinding.ItemFriendBinding
import com.t3h.mvvm.model.response.FriendResponse

class FriendAdapter : RecyclerView.Adapter<FriendAdapter.FriendHolder>{
    private val inter: IFriendAdapter
    constructor(inter:IFriendAdapter){
        this.inter = inter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        return FriendHolder(
            ItemFriendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        holder.binding.data = inter.getFriend(position)
        holder.binding.tvUsername.setText(
            inter.getFriend(position).firstName+" "+
            inter.getFriend(position).lastName
        )
        holder.binding.llRoot.setOnClickListener {
            this.inter.onClickItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return this.inter.getCount()
    }

    interface IFriendAdapter{
        fun getCount():Int
        fun getFriend(position:Int): FriendResponse
        fun onClickItem(position:Int)
    }
    class FriendHolder(val binding:ItemFriendBinding) : RecyclerView.ViewHolder(binding.root)
}