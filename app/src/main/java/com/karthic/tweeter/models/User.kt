package com.karthic.tweeter.models

import com.google.firebase.firestore.ServerTimestamp
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


data class User(
        @SerializedName("Id")
        var id: String = "",

        @SerializedName("Email")
        val emailId : String? = null,

        @SerializedName("UserName")
        val username: String? = null,

        @SerializedName("Description")
        val description: String? = null,

        @SerializedName("Website")
        val website: String? = null,

        @SerializedName("PhotoUrl")
        val photoUrl: String? = null,

        @ServerTimestamp
        @SerializedName("Created")
        val created: Date = Date()
):Serializable