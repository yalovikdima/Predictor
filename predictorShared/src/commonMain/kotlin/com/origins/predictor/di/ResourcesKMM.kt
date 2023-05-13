package com.origins.predictor.di

import com.origins.predictor.resources.ColorResourceProvider
import com.origins.predictor.resources.ImageResourceProvider
import com.origins.predictor.resources.StringResourceProvider
import com.origins.predictor.resources.TextStyleResourceProvider

internal data class ResourcesKMM(
    val sp: StringResourceProvider,
    val ip: ImageResourceProvider,
    val tp: TextStyleResourceProvider,
    val cp: ColorResourceProvider
)