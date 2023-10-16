package com.example.storyapp.di

import android.app.Application
import com.example.storyapp.data.remote.ApiStoryConfig
import com.example.storyapp.data.repository.UserRepository

object Injection {
    fun provideUserRepository(application: Application): UserRepository {
        val apiService = ApiStoryConfig.getApiService()
        return UserRepository.getInstance(apiService, application)
    }
}