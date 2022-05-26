package com.t3h.mvvm.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.t3h.mvvm.R
import java.text.SimpleDateFormat

object CommonUtils {
    @JvmStatic
    val FORMAT_TIME = SimpleDateFormat("DD/MM")

    @JvmStatic
    @BindingAdapter(value= arrayOf("loadNormalImageLink"))
    fun loadNormalImageLink(img: ImageView, link: String?) {
        if (link == null) {
            img.setImageResource(R.drawable.ao_dai)
            return
        }
        Glide.with(img.context)
            .load(link)
            .error(R.drawable.ao_dai)
            .placeholder(R.drawable.ao_dai)
            .into(img)
    }

    @JvmStatic
    @BindingAdapter(value= arrayOf("loadNormalImageResource"))
    fun loadNormalImageResource(img: ImageView, resource: Int?) {
        if (resource == null) {
            img.setImageResource(R.drawable.ao_dai)
            return
        }
        Glide.with(img.context)
            .load(resource)
            .error(R.drawable.ao_dai)
            .placeholder(R.drawable.ao_dai)
            .into(img)
    }
}