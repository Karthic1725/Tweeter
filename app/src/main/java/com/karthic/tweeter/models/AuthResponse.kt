package com.karthic.tweeter.models

import com.google.firebase.auth.AuthResult
import com.karthic.tweeter.interfaces.IAuthResponse

sealed class AuthResponse: IAuthResponse {
    data class Failed(val errorMessage: String) : AuthResponse()
    data class Success(val authResult: AuthResult?) : AuthResponse()
}
