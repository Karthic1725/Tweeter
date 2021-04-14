package com.karthic.tweeter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.karthic.tweeter.data.TweetRepository


class TweetViewModelFactory(private val repository: TweetRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TweetViewModel(repository) as T
    }

}