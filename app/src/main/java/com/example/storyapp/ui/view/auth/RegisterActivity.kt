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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.ui.factory.RegisterViewModelFactory
import com.example.storyapp.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory.getInstance(application)
    }
    private lateinit var progressDialog: Dialog
    private lateinit var alertDialog: AlertDialog.Builder
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvRegisterToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        alertDialog = AlertDialog.Builder(this@RegisterActivity)
        setupClickListeners()
        playAnimations()
    }

    private fun setupClickListeners() {
        binding.tvRegisterToLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister(){
        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString().trim()
        val passwordConfirm = binding.edRegisterPasswordConfirm.text.toString().trim()
        if (password != passwordConfirm) {
            Toast.makeText(this, getString(R.string.password_notmatch), Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.register(name, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading ->   showProgressDialog()
                is Result.Success -> onRegisterSuccess()
                is Result.Error -> onRegisterError(result.error)
            }
        }
    }


    private fun showProgressDialog() {
        progressDialog = Dialog(this@RegisterActivity)
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

    private fun onRegisterSuccess() {
        hideProgressDialog()
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog, null)

        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        handler.postDelayed({
            alertDialog.dismiss()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }, 3000)
    }
    private fun onRegisterError(errorMessage: String) {
        hideProgressDialog()
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

    }
    private fun playAnimations() {
        val fadeInDuration = 250L

        val titleAnimator = createFadeInAnimator(binding.tvTitleRegister, fadeInDuration)
        val nameTextAnimator = createFadeInAnimator(binding.tvName, fadeInDuration)
        val nameEditTextAnimator = createFadeInAnimator(binding.nameEditTextLayout, fadeInDuration)
        val emailTextAnimator = createFadeInAnimator(binding.emailTextView, fadeInDuration)
        val emailEditTextAnimator = createFadeInAnimator(binding.emailEditTextLayout, fadeInDuration)
        val passwordTextAnimator = createFadeInAnimator(binding.passwordTextView, fadeInDuration)
        val passwordEditTextAnimator = createFadeInAnimator(binding.passwordEditTextLayout, fadeInDuration)
        val passwordConfirmTextAnimator = createFadeInAnimator(binding.passwordConfirmTextView, fadeInDuration)
        val passwordConfirmEditTextAnimator = createFadeInAnimator(binding.passwordConfirmEditTextLayout, fadeInDuration)
        val registerButtonAnimator = createFadeInAnimator(binding.btnRegister, fadeInDuration)
        val registerTextAnimator = createFadeInAnimator(binding.textViewToRegister, fadeInDuration)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                nameTextAnimator,
                nameEditTextAnimator,
                emailTextAnimator,
                emailEditTextAnimator,
                passwordTextAnimator,
                passwordEditTextAnimator,
                passwordConfirmTextAnimator,
                passwordConfirmEditTextAnimator,
                registerButtonAnimator,
                registerTextAnimator
            )
            startDelay = fadeInDuration
        }.start()
    }

    private fun createFadeInAnimator(view: View, duration: Long): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, 1f).setDuration(duration)
    }
}