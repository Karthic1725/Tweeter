package com.karthic.tweeter.viewmodels

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.karthic.tweeter.data.AuthRepository
import com.karthic.tweeter.interfaces.AuthListener
import com.karthic.tweeter.models.AuthResponse
import com.karthic.tweeter.models.User
import com.karthic.tweeter.ui.LoginActivity
import com.karthic.tweeter.ui.SignUpActivity
import com.karthic.tweeter.utils.PHOTO_URL
import com.karthic.tweeter.utils.startSignUpActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AuthViewModel(private val authRepo: AuthRepository): ViewModel() {

    val user by lazy {
        authRepo.currentUser()
    }

    //auth listener
    var authListener: AuthListener? = null

    fun showLoginProcess(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                authListener?.onStarted()
            }

            val authResult = login(email, password)

            withContext(Dispatchers.Main) {
                when(authResult) {
                    is AuthResponse.Success -> authListener?.onSuccess()
                    is AuthResponse.Failed -> authListener?.onFailure(authResult.errorMessage)
                }
            }
        }
    }

    fun showSignUpProcess(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                authListener?.onStarted()
            }

            val validationMessage = signUp(email, password)

            withContext(Dispatchers.Main) {
                when(validationMessage.isNullOrBlank()) {
                    true -> authListener?.onSuccess()
                    false -> authListener?.onFailure(validationMessage)
                }
            }
        }
    }

    suspend fun login(email: String,
                      password: String): AuthResponse {
        return authRepo.login(email, password)
    }

    private suspend fun signUp(email: String, password: String) : String? {

        return when(val authResponse = authRepo.register(email, password)) {
            is AuthResponse.Success -> {
                authResponse.authResult?.user?.let {fireBaseUser ->
                    User(username = fireBaseUser.displayName,
                            id = fireBaseUser.uid,
                            created = Date(),
                            emailId = email,
                            photoUrl = PHOTO_URL,
                            description = "").also {
                        authRepo.createUser(it)
                    }
                }
                null
            }
            is AuthResponse.Failed -> authResponse.errorMessage
        }
    }

    fun goToSignUp(view: View) {
        view.context.startSignUpActivity()
    }

    fun goToLogin(view: View) {
        Intent(view.context, LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }

}