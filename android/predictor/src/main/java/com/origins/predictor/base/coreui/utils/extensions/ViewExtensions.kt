package com.origins.predictor.base.coreui.utils.extensions

import android.graphics.Rect
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.netcosports.kotlin.extensions.getAttrColor
import com.origins.predictor.LabelWithIconKMM
import com.origins.predictor.R
import com.origins.resources.entity.extensions.setLabelKMM


internal fun TextView.setLabelKMM(label: LabelWithIconKMM) {
    setLabelKMM(label.label)
    setDrawableEnd(label.icon?.imageResId)
}

internal fun TextView.setDrawableEnd(@DrawableRes resId: Int?) {
    setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        if (resId != null) ContextCompat.getDrawable(this.context, resId) else null,
        null
    )
}

internal fun SwipeRefreshLayout.initColor() {
    context.getAttrColor(R.attr.colorSecondary).let {
        setColorSchemeColors(it, it, it)
    }
}

internal fun View.doOnApplyWindowInsets(block: (View, WindowInsetsCompat, Rect) -> WindowInsetsCompat) {
    val initialPadding = recordInitialPaddingForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        block(v, insets, initialPadding)
    }
    requestApplyInsetsWhenAttached()
}


private fun recordInitialPaddingForView(view: View): Rect {
    return Rect(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
}

internal fun View.addSystemBarBottomPadding() {
    doOnApplyWindowInsets { _, insets, rect ->
        val systemWindowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        updatePadding(bottom = rect.bottom + systemWindowInsets.bottom)
        insets.consumeSystemWindowInsetsBottom()
    }
}

internal fun View.requestApplyInsetsWhenAttached() {
    if (ViewCompat.isAttachedToWindow(this)) {
        // We're already attached, just request as normal
        ViewCompat.requestApplyInsets(this)
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(v)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

internal fun WindowInsetsCompat.consumeSystemWindowInsetsBottom(): WindowInsetsCompat {
    return setSystemWindowInsets(bottom = 0)
}

internal fun WindowInsetsCompat.setSystemWindowInsets(
    left: Int = systemWindowInsetLeft,
    top: Int = systemWindowInsetTop,
    right: Int = systemWindowInsetRight,
    bottom: Int = systemWindowInsetBottom
): WindowInsetsCompat {
    return WindowInsetsCompat.Builder(this).setSystemWindowInsets(
        Insets.of(
            left,
            top,
            right,
            bottom
        )
    ).build()
}


