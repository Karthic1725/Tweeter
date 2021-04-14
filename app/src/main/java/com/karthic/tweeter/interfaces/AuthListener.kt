package com.karthic.tweeter.interfaces

interface AuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}