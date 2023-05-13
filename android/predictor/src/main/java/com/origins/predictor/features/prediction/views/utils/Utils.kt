package com.origins.predictor.features.prediction.views.utils

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.netcosports.kotlin.extensions.dpToPx
import com.netcosports.kotlin.extensions.getAttrDrawable
import com.origins.predictor.R

internal fun createButton(
    context: Context
): AppCompatTextView {
    val button = AppCompatTextView(context)
    val params: LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(215.dpToPx(context).toInt(), 40.dpToPx(context).toInt())

    button.layoutParams = params
    val padding = 25.dpToPx(context).toInt()
    button.setPadding(padding, 0, padding, 0)
    button.foreground = context.getAttrDrawable(R.attr.selectableItemBackground)

    return button
}

internal fun createIconResultView(context: Context): AppCompatImageView {
    val imageView = AppCompatImageView(context)
    val params = LinearLayout.LayoutParams(context.dpToPx(38f).toInt(), context.dpToPx(38f).toInt())
    params.gravity = Gravity.CENTER_HORIZONTAL
    imageView.layoutParams = params
    return imageView
}