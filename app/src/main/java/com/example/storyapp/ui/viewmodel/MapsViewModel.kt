package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.model.auth.LoginResult
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.repository.UserRepository

class MapsViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    fun getStoriesWithLocation(token: String) = storyRepository.getStoriesWithLocation(token)

    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()
}