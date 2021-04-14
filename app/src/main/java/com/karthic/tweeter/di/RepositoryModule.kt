package com.karthic.tweeter.di

import com.karthic.tweeter.data.AuthRepository
import com.karthic.tweeter.data.FirebaseSource
import com.karthic.tweeter.data.TweetRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val repositoryModule = Kodein.Module("repository") {
    /**val firebaseSource = FirebaseSource()
    bind<AuthRepository>() with singleton { firebaseSource }
    bind<TweetRepository>() with singleton { firebaseSource }**/
}