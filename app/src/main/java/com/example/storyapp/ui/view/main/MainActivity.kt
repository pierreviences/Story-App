package com.example.storyapp.ui.view.main

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.factory.AuthViewModelFactory
import com.example.storyapp.ui.view.auth.LoginActivity
import com.example.storyapp.ui.viewmodel.LoginViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val  loginViewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }
    private lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alertDialog = AlertDialog.Builder(this@MainActivity)
        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    showLogoutDialog()
                    true
                }
                else -> false
            }
        }
    }
    private fun showLogoutDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog_logout, null)
        val alertDialog = buildAlertDialog(customDialogView)
        val yesButton = customDialogView.findViewById<Button>(R.id.btnyes)
        val noButton = customDialogView.findViewById<Button>(R.id.btnno)
        yesButton.setOnClickListener {
            handleYesButtonClick()
        }
        noButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    private fun buildAlertDialog(customDialogView: View): AlertDialog {
        return AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
    }
    private fun handleYesButtonClick() {
        loginViewModel.deleteUserLogin()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }



}