package com.example.storyapp.ui.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.ui.factory.LoginViewModelFactory
import com.example.storyapp.ui.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory.getInstance(application)
    }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvLoginToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()
            viewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                        is Result.Error -> {
                            val message = result.error
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }
                }
            }
        }

        playAnimation()
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun playAnimation() {
        val fadeInDuration = 500L

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