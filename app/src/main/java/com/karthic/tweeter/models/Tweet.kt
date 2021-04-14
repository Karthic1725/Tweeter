package com.karthic.tweeter.models

import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

data class Tweet(

    @SerializedName("TweetId")
    var tweetId: String = UUID.randomUUID().toString(),

    @SerializedName("Message")
    var message: String? = null,

    @SerializedName("TweetedBy")
    var tweetedBy: User? = null,

    @ServerTimestamp
    @SerializedName("TweetTime")
    var tweetTime: Date = Date(),

    @ServerTimestamp
    @SerializedName("UpdatedTime")
    var updatedTime: Date = Date(),

    @SerializedName("LikedBy")
    var likedBy: ArrayList<String> = ArrayList(), //TODO

    @SerializedName("ReTweetedBy")
    var reTweetedBy: ArrayList<String> = ArrayList(), //TODO

    @SerializedName("CommentedBy")
    var commentedBy: ArrayList<String> = ArrayList() //TODO

): Serializable