package com.neeva.neevabrowser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This class responsibility is to save state of the current browser tab if it
 * can go forward. This single variable class was created to demonstrate the ViewModel
 * functionality during this project
 */
class BrowsHistoryViewModel : ViewModel() {

    val canGoForward = MutableLiveData<Boolean>()

    fun setCanGoForward(state: Boolean) {
        canGoForward.value = state
    }
}