package com.karthic.tweeter.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.karthic.tweeter.R
import com.karthic.tweeter.viewmodels.AuthViewModel
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.karthic.tweeter.databinding.ActivityLoginBinding
import com.karthic.tweeter.interfaces.AuthListener
import com.karthic.tweeter.utils.startMainActivity
import com.karthic.tweeter.viewmodels.AuthViewModelFactory
import org.kodein.di.generic.instance


class LoginActivity : AppCompatActivity(), KodeinAware, AuthListener {

    private lateinit var authViewModel: AuthViewModel
    override val kodein by kodein()
    private val factory : AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        authViewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.viewModel = authViewModel

        authViewModel.authListener = this
        registerListeners()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun registerListeners() {
        buttonLogin?.setOnClickListener { onLogin() }
        setTextChangeListener(editTextEmail, textInputLayoutEmail)
        setTextChangeListener(editTextPassword, textInputLayoutPassword)
        linearLayoutProgress.setOnTouchListener { _, _ -> true }
    }

    private fun setTextChangeListener(editText: EditText, textInputLayout: TextInputLayout) {
        editText.doOnTextChanged { _, _, _, _ -> textInputLayout.isErrorEnabled = false }
    }

    private fun onLogin() {
        var proceed = true

        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        when {
            email.isBlank() -> {
                textInputLayoutEmail.apply {
                    error = getString(R.string.email_is_required)
                    isErrorEnabled = true
                }
                proceed = false
            }
            password.isEmpty() -> {
                textInputLayoutPassword.apply {
                    error = getString(R.string.password_is_required)
                    isErrorEnabled = true
                }
                proceed = false
            }
        }

        if(proceed) {
            authViewModel.showLoginProcess(email = email, password = password)
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun onStarted() {
        linearLayoutProgress.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        linearLayoutProgress.visibility = View.GONE
        startMainActivity()
    }

    override fun onFailure(message: String) {
        linearLayoutProgress.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        authViewModel.user?.let {
            startMainActivity()
        }
    }
}