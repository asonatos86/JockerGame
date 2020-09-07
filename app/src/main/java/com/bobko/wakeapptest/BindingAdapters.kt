package com.bobko.wakeapptest

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    Glide.with(imageView)
        .load(resource)
        .into(imageView)
}