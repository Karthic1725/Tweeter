package com.karthic.tweeter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.karthic.tweeter.R
import com.karthic.tweeter.databinding.ActivitySignUpBinding
import com.karthic.tweeter.interfaces.AuthListener
import com.karthic.tweeter.utils.startLoginActivity
import com.karthic.tweeter.viewmodels.AuthViewModel
import com.karthic.tweeter.viewmodels.AuthViewModelFactory
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.editTextEmail
import kotlinx.android.synthetic.main.activity_sign_up.editTextPassword
import kotlinx.android.synthetic.main.activity_sign_up.linearLayoutProgress
import kotlinx.android.synthetic.main.activity_sign_up.textInputLayoutEmail
import kotlinx.android.synthetic.main.activity_sign_up.textInputLayoutPassword
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private lateinit var authViewModel: AuthViewModel
    private val factory : AuthViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val binding: ActivitySignUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        authViewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)

        binding.viewModel = authViewModel

        authViewModel.authListener = this

        registerListeners()
    }

    private fun registerListeners() {

        buttonSignUp.setOnClickListener { onSignUp() }
        setTextChangeListener(editTextEmail, textInputLayoutEmail)
        setTextChangeListener(editTextPassword, textInputLayoutPassword)
        setTextChangeListener(editTextConfirmPassword, textInputLayoutConfirmPassword)

        linearLayoutProgress.setOnTouchListener { _, _ -> true }
    }

    private fun setTextChangeListener(et: EditText, til: TextInputLayout) {
        et.doOnTextChanged { _, _, _, _ ->  til.isErrorEnabled = false}
    }

    private fun onSignUp() {
        var proceed = true
        val userName = editTextUserName.text.toString()
        val email = editTextEmail.text.toString()
        val textPassword = editTextPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()

        when {
            userName.isEmpty() -> {
                textInputLayoutUserName.apply {
                    error = getString(R.string.username_is_require)
                    isErrorEnabled = true
                }
                proceed = false
            }
            email.isEmpty() -> {
                textInputLayoutEmail.apply {
                    error = getString(R.string.email_is_required)
                    isErrorEnabled = true
                }
                proceed = false
            }
            textPassword.isEmpty() -> {
                textInputLayoutPassword.apply {
                    error = getString(R.string.password_is_required)
                    isErrorEnabled = true
                }
                proceed = false
            }
            confirmPassword.isEmpty() -> {
                textInputLayoutConfirmPassword.apply {
                    error = getString(R.string.confirm_password_is_required)
                    isErrorEnabled = true
                }
                proceed = false
            }
            textPassword != confirmPassword -> {
                textInputLayoutPassword.apply {
                    error = getString(R.string.password_mismatch)
                    isErrorEnabled = true
                }
                proceed = false
            }
        }

        if(proceed) {
            authViewModel.showSignUpProcess(email, password = textPassword)
        }
    }

    override fun onStarted() {
        linearLayoutProgress.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        linearLayoutProgress.visibility = View.GONE
        startLoginActivity()
    }

    override fun onFailure(message: String) {
        linearLayoutProgress.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}