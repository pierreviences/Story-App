package com.example.storyapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.liveData
import com.example.storyapp.R
import com.example.storyapp.data.model.ErrorResponse
import com.example.storyapp.data.remote.ApiStoryService
import com.example.storyapp.utils.Result
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class StoryRepository private constructor(
    private val apiService: ApiStoryService,
    private val application: Application
) {

    fun getStories(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories("Bearer $token")
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    fun uploadStories(description: String, photo: File, token: String)= liveData {
        emit(Result.Loading)
        val authToken  = "Bearer $token"
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )
        try {
            val response = apiService.uploadStory(authToken , multipartBody, requestBody )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }

    }

    private fun handleHttpException(exception: HttpException): Result.Error {
        val jsonInString = exception.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
        val errorMessage = errorBody.message
        return Result.Error(errorMessage)
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiStoryService,
            application: Application
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, application)
            }.also { instance = it }
    }
}
