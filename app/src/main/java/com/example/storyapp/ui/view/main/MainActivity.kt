package com.example.storyapp.ui.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.model.ListStoryItem
import com.example.storyapp.data.model.StoryResponse
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.adapter.StoryAdapter
import com.example.storyapp.ui.factory.AuthViewModelFactory
import com.example.storyapp.ui.factory.ViewModelFactory
import com.example.storyapp.ui.view.auth.LoginActivity
import com.example.storyapp.ui.view.story.AddStoryActivity
import com.example.storyapp.ui.viewmodel.LoginViewModel
import com.example.storyapp.ui.viewmodel.MainViewModel
import com.example.storyapp.utils.Result

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val  loginViewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }

    private val  mainViewModel:MainViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alertDialog = AlertDialog.Builder(this@MainActivity)
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    showLogoutDialog()
                    true
                }

                else -> false
            }

        }

        val adapter = StoryAdapter()
        setupRecyclerView(adapter)
        mainViewModel.getStories().observe(this) { result ->
            handleStoryResult(result, adapter)
        }
    }
        private fun setupRecyclerView(adapter: StoryAdapter) {
            val layoutManager = LinearLayoutManager(this)
            binding.rvListStory.layoutManager = layoutManager
            binding.rvListStory.adapter = adapter
        }
    private fun handleStoryResult(result: Result<StoryResponse>, adapter: StoryAdapter) {
        when (result) {
            is Result.Loading -> {
                showLoading(true)
            }

            is Result.Success -> {
                showLoading(false)
                val stories = result.data.listStory
                adapter.submitList(stories)
            }

            is Result.Error -> {
                showLoading(false)
                val message = result.error
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

        override fun onResume() {
        super.onResume()
        mainViewModel.getStories()
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "MainActivity"
    }



}