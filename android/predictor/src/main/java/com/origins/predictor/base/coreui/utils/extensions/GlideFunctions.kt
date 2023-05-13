package com.origins.predictor.base.coreui.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


internal fun loadCropImg(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(url)
        .apply(RequestOptions().centerCrop())
        .into(imageView)
}

internal fun loadCenterImg(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(url)
        .apply(RequestOptions().centerInside())
        .into(imageView)
}

internal fun loadRoundImg(imageView: ImageView, url: String?) {
    Glide.with(imageView.context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .into(imageView)
}

internal fun loadRoundImg(imageView: ImageView, imgResId: Int?) {
    Glide.with(imageView.context)
        .load(imgResId)
        .apply(RequestOptions.circleCropTransform())
        .into(imageView)
}

internal fun loadBitmap(url: String, size: Int, @DrawableRes placeholder: Int? = null, context: Context): Bitmap {
    return Glide.with(context)
        .asBitmap()
        .load(url)
        .apply(RequestOptions().centerCrop().apply {
            if (placeholder != null) {
                placeholder(placeholder)
            }
        })
        .submit(size, size)
        .get()
}