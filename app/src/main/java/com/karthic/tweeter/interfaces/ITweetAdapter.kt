package com.karthic.tweeter.interfaces


interface ITweetAdapter {
    fun onLikeClicked(tweetId: String)
    fun onActionMenuClicked(tweetId: String, message: String)
    fun onReTweetClicked(tweetId: String)
    fun onCommentClicked(tweetId: String)
}