package com.karthic.tweeter.viewmodels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.CollectionReference
import com.karthic.tweeter.data.TweetRepository
import com.karthic.tweeter.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TweetViewModel constructor(private val tweetRepository: TweetRepository): ViewModel() {

    var tweetMessage: MutableLiveData<String>? = null

    fun getTweetCollections(): CollectionReference {
        return tweetRepository.getTweetCollection()
    }

    suspend fun deleteTweet(tweetId: String) {
        tweetRepository.deleteTweet(tweetId)
    }

    fun postTweet(tweetId: String?, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tweetId?.let { tweetId ->
                tweetRepository.updateTweet(tweetId, message)
            }?: tweetRepository.postTweet(message)
        }
    }

    fun composeTweet(view: View) {
        view.context.startTweetActivity()
    }

    fun signOut(view: View) {
        tweetRepository.logout()
        view.context.startLoginActivity()
    }

}