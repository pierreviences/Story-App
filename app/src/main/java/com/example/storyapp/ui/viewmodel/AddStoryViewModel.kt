package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.model.auth.LoginResult
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.repository.UserRepository
import okhttp3.RequestBody
import java.io.File

class AddStoryViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel(){
    fun addStory(lat: RequestBody?, lon: RequestBody?, description: String, file: File, token: String) =
        storyRepository.uploadStories(lat, lon, description, file, token)

    fun getUserLogin(): LiveData<LoginResult> = userRepository.getSession()
}