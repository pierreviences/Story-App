package com.example.storyapp.ui.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivitySplashScreenBinding
import com.example.storyapp.ui.factory.AuthViewModelFactory
import com.example.storyapp.ui.view.auth.LoginActivity
import com.example.storyapp.ui.viewmodel.LoginViewModel

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupAnimations()
        handleUserLogin()
    }
    private fun setupAnimations() {
        binding.apply {
            titleSplash.animation = AnimationUtils.loadAnimation(this@SplashScreenActivity, R.anim.animsplash)
        }
    }
    private fun handleUserLogin() {
        viewModel.getUserLogin().observe(this) { user ->
            val delay = if (user.token.isNullOrEmpty()) {
                Intent(this@SplashScreenActivity, LoginActivity::class.java)
            } else {
                Intent(this@SplashScreenActivity, MainActivity::class.java)
            }

            handler.postDelayed({
                startActivity(delay)
                finish()
            }, SPLASH_SCREEN_DURATION)
        }
    }

    companion object {
        private const val SPLASH_SCREEN_DURATION = 2000L
    }

}