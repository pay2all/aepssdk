package com.pay2all.aeps.aepsnew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AEPSViewModelFactory (val mBaseURL:String): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AEPSViewModel(mBaseURL) as T
    }
}