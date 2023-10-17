package com.example.storyapp.di

import android.app.Application
import com.example.storyapp.data.local.datastore.UserPreferences
import com.example.storyapp.data.local.datastore.dataStore
import com.example.storyapp.data.remote.ApiStoryConfig
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.repository.UserRepository

object Injection {

    private fun provideApiService() = ApiStoryConfig.getApiService()

    fun provideStoryRepository(application: Application): StoryRepository {
        return StoryRepository.getInstance(provideApiService(), application)
    }

    fun provideUserRepository(application: Application): UserRepository {
        val pref = UserPreferences.getInstance(application.dataStore)
        return UserRepository.getInstance(provideApiService(), application, pref)
    }


}