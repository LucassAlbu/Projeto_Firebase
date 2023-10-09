package com.projetoFirebase.util

interface DefaultListenerResponse<T, P> {
    fun onSuccess(data: T)
    fun onError(error: P)
}