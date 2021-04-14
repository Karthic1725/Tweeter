package com.karthic.tweeter.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.karthic.tweeter.R
import com.karthic.tweeter.databinding.ActivityTweetBinding
import com.karthic.tweeter.utils.SELECTED_TWEET_ID
import com.karthic.tweeter.utils.SELECTED_TWEET_MESSAGE
import com.karthic.tweeter.utils.startMainActivity
import com.karthic.tweeter.viewmodels.TweetViewModel
import com.karthic.tweeter.viewmodels.TweetViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class TweetActivity: AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private lateinit var tweetViewModel: TweetViewModel
    private val factory : TweetViewModelFactory by instance()
    private var binding: ActivityTweetBinding? = null
    private var tweetIdentifier: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tweet)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet)
        tweetViewModel = ViewModelProviders.of(this, factory).get(TweetViewModel::class.java)

        binding?.viewModel = tweetViewModel

        updateTweetUI()
        registerListener()
    }

    private fun updateTweetUI() {
        tweetIdentifier = intent.getStringExtra(SELECTED_TWEET_ID)
        val tweetMessage = intent.getStringExtra(SELECTED_TWEET_MESSAGE)

        if (!tweetIdentifier.isNullOrBlank() && !tweetMessage.isNullOrBlank()) {
            binding?.tweetMessage?.setText(tweetMessage)
            binding?.tweetActionButton?.text = getString(R.string.update_tweet)
        }
    }

    private fun registerListener() {
        binding?.tweetActionButton?.setOnClickListener {
            tweetViewModel.postTweet(tweetIdentifier, binding?.tweetMessage?.text.toString())
            finishAndLaunchTweetsFeed()
        }

        binding?.closeButton?.setOnClickListener {
            finishAndLaunchTweetsFeed()
        }
    }

    private fun finishAndLaunchTweetsFeed() {
        finish()
        this.startMainActivity()
    }

}