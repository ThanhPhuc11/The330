package com.nagaja.the330.model

interface UploadListener {
    fun onStart()
    fun onSuccess(url: String)
    fun onFail(t: Throwable?)
}