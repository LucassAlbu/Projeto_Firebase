package com.projetoFirebase.util

import androidx.lifecycle.MutableLiveData

class CustomMutableLiveData<T> : MutableLiveData<T>() {

    var lastValue: T? = null

    override fun postValue(value: T) {
        lastValue = this.value
        super.postValue(value)
    }

    override fun setValue(value: T?) {
        lastValue = this.value
        super.setValue(value)
    }
}