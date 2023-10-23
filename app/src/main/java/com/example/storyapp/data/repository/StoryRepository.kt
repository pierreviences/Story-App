package com.example.storyapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.R
import com.example.storyapp.data.local.room.StoryDatabase
import com.example.storyapp.data.local.room.StoryRemoteMediator
import com.example.storyapp.data.model.ErrorResponse
import com.example.storyapp.data.model.story.ListStoryItem
import com.example.storyapp.data.remote.ApiStoryService
import com.example.storyapp.utils.Result
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class StoryRepository private constructor(
    private val apiService: ApiStoryService,
    private val application: Application,
    private val storyDatabase: StoryDatabase
) {

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(
                getAuthToken(token),
                apiService,
                storyDatabase
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).liveData
    }

    fun uploadStories(lat: RequestBody? = null, lon: RequestBody? = null, description: String, photo: File, token: String)= liveData {
        emit(Result.Loading)
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )
        try {
            val response = apiService.uploadStory(getAuthToken(token), multipartBody, descriptionRequestBody, lat, lon )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            emit(handleHttpException(e))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }

    }

    fun getStoriesWithLocation(token: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoriesWithLocation(getAuthToken(token))
            emit(Result.Success(response))
        }  catch (e: HttpException) {
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

    private fun getAuthToken(token: String): String {
        return "Bearer $token"
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiStoryService,
            application: Application,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, application, storyDatabase)
            }.also { instance = it }
    }
}
