package com.origins.predictor

import com.netcosports.rooibos.Swift
import com.origins.resources.entity.TextStyleKMM
import com.origins.resources.entity.ui.Label

@Swift
data class LabelWithHintKMM(
    val hint: Label,
    val labelTextStyle: TextStyleKMM?
)