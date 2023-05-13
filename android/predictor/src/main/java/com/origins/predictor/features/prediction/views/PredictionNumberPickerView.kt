package com.origins.predictor.features.prediction.views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.netcosports.kotlin.extensions.dpToPx


internal class PredictionNumberPickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, true)
        clipToPadding = false
        overScrollMode = OVER_SCROLL_NEVER

        setFadingEdgeLength(110.dpToPx(context).toInt())
    }
}