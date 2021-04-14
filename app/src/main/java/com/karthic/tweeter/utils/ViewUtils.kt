package com.karthic.tweeter.utils

import android.content.Context
import android.content.Intent
import com.karthic.tweeter.ui.LoginActivity
import com.karthic.tweeter.ui.MainActivity
import com.karthic.tweeter.ui.SignUpActivity
import com.karthic.tweeter.ui.TweetActivity


fun Context.startMainActivity() = Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
}

fun Context.startLoginActivity() = Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
}


fun Context.startSignUpActivity() = Intent(this, SignUpActivity::class.java).also {
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(it)
}


fun Context.startTweetActivity() = Intent(this, TweetActivity::class.java).also {
    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(it)
}