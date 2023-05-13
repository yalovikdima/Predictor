package com.origins.predictor.features.prediction.views.utils

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import com.netcosports.kotlin.extensions.dpToPx
import com.origins.predictor.features.prediction.ui.TermsUi
import com.origins.predictor.features.prediction.views.PredictionTermsView

internal fun ViewGroup.addView(marginTop: Float, view: View) {
    val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    params.topMargin = context.dpToPx(marginTop).toInt()
    params.gravity = Gravity.CENTER_HORIZONTAL
    view.layoutParams = params
    this.addView(view, this.childCount)
}

internal fun ViewGroup.addTermsView(termsUi: TermsUi) {
    val terms = PredictionTermsView(this.context)
    terms.apply {
        setTerms(termsUi)
        val params =
            FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.apply {
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 0, 0, context.dpToPx(6).toInt())
        }
        layoutParams = params
    }
    this.addView(terms)
}

internal fun ViewGroup.addButtonView(marginTop: Float, view: View, paddingBottom: Float = 0f) {
    if (view is AppCompatTextView) {
        val params = LinearLayout.LayoutParams(context.dpToPx(215f).toInt(), context.dpToPx(45f).toInt())
        params.apply {
            gravity = Gravity.CENTER
            topMargin = context.dpToPx(marginTop).toInt()
            setPadding(0, 0, 0, context.dpToPx(paddingBottom).toInt())
        }
        view.layoutParams = params
        view.apply {
            gravity = Gravity.CENTER
        }
        this.addView(view, this.childCount)
    } else {
        addView(marginTop, view)
    }
}







