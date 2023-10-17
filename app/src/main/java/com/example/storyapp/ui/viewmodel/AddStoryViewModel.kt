package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.model.LoginResult
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.repository.UserRepository
import java.io.File

class AddStoryViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel(){
    fun addStory(description: String, file: File, token: String) =
        storyRepository.uploadStories(description, file, token)

    fun getUserLogin(): LiveData<LoginResult> {
        return userRepository.getSession()
    }
}