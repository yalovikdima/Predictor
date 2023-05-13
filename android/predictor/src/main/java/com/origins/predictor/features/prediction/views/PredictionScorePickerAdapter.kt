package com.origins.predictor.features.prediction.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.origins.predictor.R
import com.origins.predictor.features.prediction.ui.RangeUi
import com.origins.resources.entity.extensions.setTextStyleKMM

internal class PredictionScorePickerAdapter(
    private val isHomeTeam: Boolean,
    private var isShare: Boolean = false,
    private val itemClickListener: (Int, Boolean) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<PredictionScorePickerAdapter.NumberViewHolder>() {

    var selectedPosition: Int = 0

    var numbers = emptyList<Int>()
    var range: RangeUi? = null
        set(value) {
            field = value
            value?.let { numbers = (it.minValue..it.maxValue).toList() }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        return NumberViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.predictor_list_item_number_picker, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        val item = numbers[position]
        holder.text.text = item.toString()
        if (selectedPosition == position) {
            range?.selectedStyle?.let { holder.text.setTextStyleKMM(it) }
        } else {
            range?.unSelectedStyle?.let { holder.text.setTextStyleKMM(it) }
        }
        if (isShare) {
            if (position > selectedPosition) {
                holder.text.alpha = 1 - ((position - selectedPosition) * 0.17f)
            }
        }

        holder.itemView.setOnClickListener {
            itemClickListener(position, isHomeTeam)
        }
    }

    override fun getItemCount(): Int {
        return numbers.size
    }

    class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.number)
    }
}

