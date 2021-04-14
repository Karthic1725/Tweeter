package com.karthic.tweeter.data

import com.google.firebase.firestore.CollectionReference


class TweetRepository(private val firebase: FirebaseSource) {

    suspend fun postTweet(description: String) : Boolean = firebase.postTweet(description)

    suspend fun deleteTweet(tweetId: String) : Boolean = firebase.deleteTweet(tweetId)

    suspend fun updateTweet(tweetId: String, description: String) : Boolean = firebase.updateTweet(tweetId, description)

    fun getTweetCollection() : CollectionReference = firebase.tweetCollections

    fun logout() = firebase.logout()
}