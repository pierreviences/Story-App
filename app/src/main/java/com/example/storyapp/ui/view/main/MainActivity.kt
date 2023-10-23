package com.example.storyapp.ui.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.R
import com.example.storyapp.data.model.auth.LoginResult
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.ui.adapter.LoadingStateAdapter
import com.example.storyapp.ui.adapter.StoryAdapter
import com.example.storyapp.ui.factory.AuthViewModelFactory
import com.example.storyapp.ui.factory.ViewModelFactory
import com.example.storyapp.ui.view.auth.LoginActivity
import com.example.storyapp.ui.view.map.MapsActivity
import com.example.storyapp.ui.view.story.AddStoryActivity
import com.example.storyapp.ui.viewmodel.LoginViewModel
import com.example.storyapp.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val  loginViewModel: LoginViewModel by viewModels {
        AuthViewModelFactory.getInstance(application)
    }

    private val  mainViewModel:MainViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var dataUser: LoginResult
    private val adapter = StoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
        setupRecyclerView(adapter)
    }
    private fun setupViews(){
        mainViewModel.getUserLogin().observe(this@MainActivity) {
            dataUser = it
        }
        alertDialog = AlertDialog.Builder(this@MainActivity)
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.localization -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.logout -> {
                    showLogoutDialog()
                    true
                }
                R.id.map -> {
                    startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                    true
                }

                else -> false
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            mainViewModel.getStories(dataUser.token).observe(this) { result ->
                adapter.submitData(lifecycle, result)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
    private fun setupRecyclerView(adapter: StoryAdapter) {
            val layoutManager = LinearLayoutManager(this)
            binding.rvListStory.layoutManager = layoutManager
            binding.rvListStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }

    override fun onResume() {
        super.onResume()
        getStories()
    }
    private fun getStories() {
        mainViewModel.getStories(dataUser.token).observe(this) { result ->
            adapter.submitData(lifecycle, result)
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
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}