package com.origins.predictor

import com.netcosports.rooibos.Swift
import com.origins.resources.entity.ImageKMM
import com.origins.resources.entity.ui.Label

@Swift
data class LabelWithIconKMM(
    val label: Label,
    val icon: ImageKMM?
)