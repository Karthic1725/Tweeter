package com.karthic.tweeter.di

import bindViewModel
import com.karthic.tweeter.viewmodels.AuthViewModel
import com.karthic.tweeter.viewmodels.TweetViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelModule = Kodein.Module("viewModel") {
    bindViewModel<AuthViewModel>() with provider {
        AuthViewModel(
            instance()
        )
    }

    bindViewModel<TweetViewModel>() with provider {
        TweetViewModel(
            instance()
        )
    }

}
