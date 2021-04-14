package com.karthic.tweeter.data

import com.karthic.tweeter.models.User


class AuthRepository(private val firebase: FirebaseSource) {

    suspend fun login(email: String, password: String) = firebase.logInWithEmail(email, password)

    suspend fun register(email: String, password: String) = firebase.registerUser(email, password)

    suspend fun createUser(user: User) : Boolean = firebase.createUserOnSignUp(user)

    fun currentUser() = firebase.currentUser()

}
