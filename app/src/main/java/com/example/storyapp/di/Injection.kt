package com.example.storyapp.di

import android.app.Application
import com.example.storyapp.data.local.UserPreferences
import com.example.storyapp.data.local.dataStore
import com.example.storyapp.data.remote.ApiStoryConfig
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideStoryRepository(application: Application): StoryRepository {
        val pref = UserPreferences.getInstance(application.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiStoryConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, application)
    }
    fun provideUserRepository(application: Application): UserRepository {
        val apiService = ApiStoryConfig.getApiService()
        val pref = UserPreferences.getInstance(application.dataStore)
        return UserRepository.getInstance(apiService, application, pref)
    }


}