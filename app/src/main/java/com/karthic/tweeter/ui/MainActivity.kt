package com.karthic.tweeter.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.Query
import com.karthic.tweeter.R
import com.karthic.tweeter.adapter.TweetAdapter
import com.karthic.tweeter.databinding.ActivityMainBinding
import com.karthic.tweeter.interfaces.ITweetAdapter
import com.karthic.tweeter.models.Tweet
import com.karthic.tweeter.utils.SELECTED_TWEET_ID
import com.karthic.tweeter.utils.SELECTED_TWEET_MESSAGE
import com.karthic.tweeter.utils.TWEET_TIME
import com.karthic.tweeter.viewmodels.TweetViewModel
import com.karthic.tweeter.viewmodels.TweetViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_tweet.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.tweet_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class MainActivity : AppCompatActivity(), ITweetAdapter, KodeinAware  {

    override val kodein by kodein()

    private lateinit var tweetAdapter: TweetAdapter
    private lateinit var tweetViewModel: TweetViewModel
    private val factory : TweetViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        tweetViewModel = ViewModelProviders.of(this, factory).get(TweetViewModel::class.java)

        binding.viewModel = tweetViewModel
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        val query = tweetViewModel.getTweetCollections().orderBy(TWEET_TIME, Query.Direction.DESCENDING)

        val recyclerViewOptions = FirestoreRecyclerOptions
            .Builder<Tweet>()
            .setQuery(query, Tweet::class.java)
            .setLifecycleOwner(this)
            .build()

        tweetAdapter = TweetAdapter(recyclerViewOptions, this)
        tweetAdapter.notifyDataSetChanged()

        recyclerView.apply {
            adapter = tweetAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onStart() {
        super.onStart()
        tweetAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        tweetAdapter.stopListening()
    }

    override fun onLikeClicked(tweetId: String) {
        TODO("Not yet implemented")
    }

    override fun onActionMenuClicked(tweetId: String, message: String) {
        showBottomSheetDialog(tweetId, message)
    }

    override fun onReTweetClicked(tweetId: String) {
        TODO("Not yet implemented")
    }

    override fun onCommentClicked(tweetId: String) {
        TODO("Not yet implemented")
    }

    private fun showBottomSheetDialog(selectedTweetId: String, tweetMessage: String) {

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet)

        bottomSheetDialog.delete_tweet.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {

                withContext(Dispatchers.Main) {
                    linearLayoutProgress.visibility = View.VISIBLE
                }

                tweetViewModel.deleteTweet(selectedTweetId)

                withContext(Dispatchers.Main) {
                    linearLayoutProgress.visibility = View.GONE
                    tweetAdapter.notifyDataSetChanged()
                    bottomSheetDialog.dismiss()
                }
            }
        }

        bottomSheetDialog.edit_tweet.setOnClickListener {
            bottomSheetDialog.dismiss()
            launchTweetScreen(selectedTweetId, tweetMessage)
        }

        bottomSheetDialog.show()
    }

    private fun launchTweetScreen(tweetId: String, message: String) {
        Intent(this, TweetActivity::class.java).apply {
            putExtra(SELECTED_TWEET_ID, tweetId)
            putExtra(SELECTED_TWEET_MESSAGE, message)
        }.also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }
}