package com.origins.predictor.base.coreui.fragments

interface PredictorBackPressObserver {

    /**
     * @return Returns true if fragment handled back press.
     */
    fun onBackPressed(): Boolean
}