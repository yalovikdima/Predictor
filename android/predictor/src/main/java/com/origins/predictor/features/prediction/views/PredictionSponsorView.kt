package com.origins.predictor.features.prediction.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.origins.predictor.base.coreui.utils.extensions.loadCenterImg
import com.origins.predictor.databinding.PredictorSponsorViewBinding
import com.origins.predictor.features.prediction.ui.SponsorUi
import com.origins.resources.entity.extensions.setLabelKMM

internal class PredictionSponsorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val binding: PredictorSponsorViewBinding = PredictorSponsorViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
    }

    fun setSponsor(sponsor: SponsorUi?) {
        if (sponsor != null) {
            binding.presentedBy.setLabelKMM(sponsor.presentedBy)
            loadCenterImg(binding.sponsorLogo, sponsor.imageUrl)
            if (sponsor.url != null) {
                this.setOnClickListener {
                    try {
                        Uri.parse(sponsor.url)
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