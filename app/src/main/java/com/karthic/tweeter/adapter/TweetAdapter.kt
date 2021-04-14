package com.karthic.tweeter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.karthic.tweeter.R
import com.karthic.tweeter.interfaces.ITweetAdapter
import com.karthic.tweeter.models.Tweet
import com.karthic.tweeter.utils.TimeUtils.Companion.getTimeAgo
import kotlinx.android.synthetic.main.tweet_item.view.*

class TweetAdapter(options: FirestoreRecyclerOptions<Tweet>,
                   private val listener: ITweetAdapter) : FirestoreRecyclerAdapter<Tweet, TweetAdapter.TweetViewHolder>(options) {

    class TweetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postText: TextView = itemView.tweetTitle
        val userText: TextView = itemView.userName
        val tweetedTime: TextView = itemView.createdAt
        val userImage: ImageView = itemView.userImage

        val likeCount: TextView = itemView.likeCount
        val likeButton: ImageView = itemView.likeButton
        val commentCount: TextView = itemView.commentCount
        val commentButton: ImageView = itemView.commentButton

        val reTweetCount: TextView = itemView.reTweetCount
        val reTweetButton: ImageView = itemView.reTweetButton

        val contextMenuButton: ImageView = itemView.context_menu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        return TweetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.tweet_item, parent, false))
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int, model: Tweet) {

        holder.apply {
            postText.text = model.message
            userText.text = model.tweetedBy?.username.takeIf { !it.isNullOrBlank() }?:model.tweetedBy?.emailId?.substringBefore("@")
            tweetedTime.text = getTimeAgo(model.tweetTime)

            likeCount.text = model.likedBy.size.toString()
            reTweetCount.text = model.reTweetedBy.size.toString()
            commentCount.text = model.commentedBy.size.toString()

            contextMenuButton.setOnClickListener { listener.onActionMenuClicked(model.tweetId, model.message?:"") }
        }

        model.tweetedBy?.photoUrl?.let { photoUrl ->
            Glide.with(holder.userImage.context).load(photoUrl).circleCrop()
                .into(holder.userImage)
        }

        val auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser!!.uid

        val isLiked = model.likedBy.contains(currentUserId)
        val isReTweeted = model.reTweetedBy.contains(currentUserId)
        val isCommented = model.commentedBy.contains(currentUserId)

        //TODO: Extend it to replicate the original behavior.

        if (isReTweeted) {
            holder.reTweetButton.setImageDrawable(ContextCompat.getDrawable(holder.reTweetButton.context, R.drawable.retweet))
        } else {
            holder.reTweetButton.setImageDrawable(ContextCompat.getDrawable(holder.reTweetButton.context, R.drawable.retweet_inactive))
        }

        if (isCommented) {
            holder.commentButton.setImageDrawable(ContextCompat.getDrawable(holder.commentButton.context, R.drawable.ic_comment_active))
        } else {
            holder.commentButton.setImageDrawable(ContextCompat.getDrawable(holder.commentButton.context, R.drawable.ic_comment_inactive))
        }

        if (isLiked) {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.like))
        } else {
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context, R.drawable.like_inactive))
        }
    }

}