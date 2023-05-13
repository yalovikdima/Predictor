package com.origins.predictor.features.prediction.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.View.OnClickListener
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.origins.predictor.features.prediction.ui.TermsUi
import com.origins.resources.entity.extensions.setTextStyleKMM

internal class PredictionTermsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        gravity = Gravity.CENTER_HORIZONTAL
    }

    fun setTerms(terms: TermsUi) {
        val isVisible: Boolean
        val listener: OnClickListener?
        if (terms == null) {
            isVisible = false
            listener = null
        } else {
            text = SpannableString(terms.text.text.getString(context)).apply {
                setSpan(UnderlineSpan(), 0, this.length, 0)
            }
            setTextStyleKMM(terms.text.style)
            isVisible = terms != null
            listener = OnClickListener {
                try {
                    Uri.parse(terms.url)
                } catch (ignore: Exception) {
                    null
                }?.let { uri ->
                    ContextCompat.startActivity(
                        context,
                        Intent.createChooser(Intent(Intent.ACTION_VIEW, uri), null),
                        null
                    )
                }
            }
        }
        this.isVisible = isVisible
        this.setOnClickListener(listener)
    }
}