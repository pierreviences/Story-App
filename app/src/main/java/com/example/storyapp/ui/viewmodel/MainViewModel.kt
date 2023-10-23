package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.model.auth.LoginResult
import com.example.storyapp.data.model.story.ListStoryItem
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.repository.UserRepository

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStories(token).cachedIn(viewModelScope)

    fun getUserLogin(): LiveData<LoginResult> =  userRepository.getSession()

}