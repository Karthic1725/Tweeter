package com.karthic.tweeter.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.karthic.tweeter.models.AuthResponse
import com.karthic.tweeter.models.Tweet
import com.karthic.tweeter.models.User
import com.karthic.tweeter.utils.DATA_TWEETS
import com.karthic.tweeter.utils.DATA_USERS
import com.karthic.tweeter.utils.TWEET_MESSAGE
import com.karthic.tweeter.utils.UPDATED_TWEET_TIME
import kotlinx.coroutines.tasks.await
import java.util.*

class FirebaseSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val tweetCollections by lazy {
        firebaseDB.collection(DATA_TWEETS)
    }

    suspend fun logInWithEmail(email: String, password: String): AuthResponse {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
                ?.let {loggedInAuthResult->
                    loggedInAuthResult.user?.uid?.let { userId ->
                        getAllUsers(userId)?.let {
                            loggedInAuthResult
                        }
                    }
                }
            AuthResponse.Success(authResult = authResult)
        } catch (ex: Exception) {
            AuthResponse.Failed(errorMessage = ex.message?:"Login Failed")
        }
    }

    suspend fun registerUser(email: String, password: String): AuthResponse {
        return try {
            val authResult = firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .await()

            AuthResponse.Success(authResult)
        }
        catch (ex:Exception) {
            AuthResponse.Failed(ex.message?:"Registration Failed")
        }
    }

    private suspend fun getAllUsers(documentId: String): DocumentSnapshot? {
        return try{
            return firebaseDB
                .collection(DATA_USERS)
                .document(documentId)
                .get()
                .await()
        } catch (e : Exception){
            null
        }
    }

    suspend fun createUserOnSignUp(user: User) : Boolean {
        return try {
            firebaseDB
                .collection(DATA_USERS)
                .document(user.id)
                .set(user)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserById(userId: String): Task<DocumentSnapshot> {
        return firebaseDB
            .collection(DATA_USERS)
            .document(userId)
            .get()
    }

    fun getTweet(tweetId: String): Task<DocumentSnapshot> {
        return firebaseDB
                .collection(DATA_TWEETS)
                .document(tweetId)
                .get()
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    private fun getTweetModel(description: String, user: User?) : Tweet {
        return Tweet(
                message = description,
                tweetedBy = user,
                tweetTime = Date(),
                likedBy = arrayListOf(),
                commentedBy = arrayListOf(),
                reTweetedBy = arrayListOf()
        )
    }

    suspend fun postTweet(description: String) : Boolean {
        return try {
            firebaseAuth
                .currentUser
                ?.uid
                ?.let { currentUser ->

                    val user = getUserById(currentUser).await().toObject(User::class.java)
                    val tweet = getTweetModel(description, user)

                    tweetCollections
                        .document(tweet.tweetId)
                        .set(tweet)
                        .await()

                    true

                }?:false

        } catch (e : Exception) {
            false
        }
    }

    suspend fun updateTweet(tweetId: String, description: String) : Boolean {
        return try {
            tweetCollections
                    .document(tweetId)
                    .update(mapOf(TWEET_MESSAGE to description, UPDATED_TWEET_TIME to Date()))
                    .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteTweet(tweetId: String) : Boolean {
        return try {
            tweetCollections
                    .document(tweetId)
                    .delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }

}