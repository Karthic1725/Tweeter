package com.karthic.tweeter

import android.app.Application
import com.karthic.tweeter.data.AuthRepository
import com.karthic.tweeter.data.FirebaseSource
import com.karthic.tweeter.data.TweetRepository
import com.karthic.tweeter.viewmodels.AuthViewModelFactory
import com.karthic.tweeter.viewmodels.TweetViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class TweeterApp : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        bind() from singleton { FirebaseSource() }
        bind() from singleton { AuthRepository(instance()) }
        bind() from singleton { TweetRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { TweetViewModelFactory(instance()) }
    }

    /**
    override val kodein by Kodein.lazy {
        import(androidXModule(this@TweeterApp))

        import(repositoryModule)
        import(viewModelModule)
    }**/

}