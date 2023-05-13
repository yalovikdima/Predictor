package com.origins.predictor.base.coreui.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.origins.predictor.R
import com.origins.predictor.features.prediction.views.PredictionScorePickerAdapter

internal class PredictorNumberItemDecoration(
    private val context: Context,
    private val adapter: PredictionScorePickerAdapter,
    private val isGame: Boolean = true
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPadding =
            context.resources.getDimensionPixelSize(R.dimen.number_picker_item_padding)
        val sharingItemPadding =
            context.resources.getDimensionPixelSize(R.dimen.sharing_number_picker_item_padding)
        val numberPickerTextSize =
            context.resources.getDimensionPixelSize(R.dimen.number_picker_item_text_size)
        val height = if (parent.height != 0) {
            parent.height
        } else {
            context.resources.getDimensionPixelSize(R.dimen.share_number_picker_height)
        }
        when {
            parent.getChildAdapterPosition(view) == 0 -> {
                if (isGame) {
                    outRect.bottom = (height - numberPickerTextSize) / 2
                    outRect.top = itemPadding
                } else {
                    outRect.top = sharingItemPadding
                    outRect.bottom = sharingItemPadding
                }
            }
            parent.getChildAdapterPosition(view) == adapter.itemCount - 1 -> {
                val paddingTop = if (isGame) {
                    (height - numberPickerTextSize) / 2
                } else {
                    height - numberPickerTextSize - sharingItemPadding
                }
                outRect.top = paddingTop
                outRect.bottom = if (isGame) itemPadding else sharingItemPadding

            }
            else -> {
                outRect.top = if (isGame) itemPadding else sharingItemPadding
                outRect.bottom = if (isGame) itemPadding else sharingItemPadding
            }
        }
    }
}