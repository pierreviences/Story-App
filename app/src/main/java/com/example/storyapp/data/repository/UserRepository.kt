package com.example.storyapp.data.repository

import android.app.Application
import com.example.storyapp.data.remote.ApiStoryService

class UserRepository private constructor(
    private val apiService: ApiStoryService,
    private val application: Application,
) {

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiStoryService,
            application: Application
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, application)
            }.also { instance = it }
    }

}