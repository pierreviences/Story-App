package com.example.storyapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.data.model.ErrorResponse
import com.example.storyapp.data.remote.ApiStoryService
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class UserRepository private constructor(
    private val apiService: ApiStoryService,
    private val application: Application,
) {
    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            val errorMessage = exception.message ?: "Unknown error"
            Log.e("UserRepository", "Login error: $errorMessage")
            emit(Result.Error(exception.message ?: "Unknown error"))
        }
    }

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