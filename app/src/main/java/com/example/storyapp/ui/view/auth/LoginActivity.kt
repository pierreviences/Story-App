package com.example.storyapp.ui.view.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.factory.LoginViewModelFactory
import com.example.storyapp.ui.view.main.MainActivity
import com.example.storyapp.ui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: Dialog
    private lateinit var alertDialog: AlertDialog.Builder
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alertDialog = AlertDialog.Builder(this@LoginActivity)
        setupClickListeners()
        playAnimations()
    }


    private fun setupClickListeners() {
        binding.tvLoginToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin(){
        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()


        viewModel.login(email, password).observe(this) { result ->
            when (result) {
                is Result.Loading ->   showProgressDialog()
                is Result.Success -> onLoginSuccess()
                is Result.Error -> onLoginError(result.error)
            }
        }
    }

    private fun onLoginSuccess() {

        hideProgressDialog()
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog_success, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()


        viewModel.getUserLogin().observe(this) {
            val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
            tvDescription.text =  getString(R.string.login_success_desc) + " " +  it.name
        }

        handler.postDelayed({
            viewModel.getUserLogin().observe(this) {
                if (it.token.isNotEmpty()) {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }, 3000)

    }
    private fun onLoginError(errorMessage: String) {
        hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() {
        progressDialog = Dialog(this@LoginActivity)
        progressDialog.setContentView(R.layout.custom_progressbar)
        val progressBar: ProgressBar = progressDialog.findViewById(R.id.progressBar)
        progressBar.isIndeterminate = true
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }
    private fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun playAnimations() {
        val fadeInDuration = 300L

        val titleAnimator = createFadeInAnimator(binding.titleTextView, fadeInDuration)
        val messageAnimator = createFadeInAnimator(binding.messageTextView, fadeInDuration)
        val emailTextAnimator = createFadeInAnimator(binding.emailTextView, fadeInDuration)
        val emailEditTextAnimator = createFadeInAnimator(binding.emailEditTextLayout, fadeInDuration)
        val passwordTextAnimator = createFadeInAnimator(binding.passwordTextView, fadeInDuration)
        val passwordEditTextAnimator = createFadeInAnimator(binding.passwordEditTextLayout, fadeInDuration)
        val loginButtonAnimator = createFadeInAnimator(binding.btnLogin, fadeInDuration)
        val registerTextAnimator = createFadeInAnimator(binding.textViewToRegister, fadeInDuration)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                messageAnimator,
                emailTextAnimator,
                emailEditTextAnimator,
                passwordTextAnimator,
                passwordEditTextAnimator,
                loginButtonAnimator,
                registerTextAnimator
            )
            startDelay = fadeInDuration
        }.start()
    }

    private fun createFadeInAnimator(view: View, duration: Long): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(duration)
    }
}