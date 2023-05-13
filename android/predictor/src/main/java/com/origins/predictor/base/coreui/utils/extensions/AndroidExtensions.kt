package com.origins.predictor.base.coreui.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


internal fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.currentFocus?.let { view ->
        if (view.windowToken != null) {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
