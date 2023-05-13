package com.origins.predictor.features.prediction.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.origins.predictor.base.coreui.utils.extensions.loadCenterImg
import com.origins.predictor.databinding.PredictorGiftViewBinding
import com.origins.predictor.features.prediction.ui.GiftUi
import com.origins.resources.entity.extensions.setLabelKMM

internal class PredictionGiftView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val binding: PredictorGiftViewBinding =
        PredictorGiftViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setGift(gift: GiftUi?) {
        if (gift != null) {
            gift.title?.let { binding.title.setLabelKMM(it) }
            loadCenterImg(binding.image, gift.imageUrl)
            if (!gift.redirectUrl.isNullOrEmpty()) {
                this.setOnClickListener {
                    try {
                        Uri.parse(gift.redirectUrl)
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
        } else {
            isVisible = false
        }
    }
}