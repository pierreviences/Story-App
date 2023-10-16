package com.example.storyapp.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.storyapp.R
import com.example.storyapp.data.Result
import com.example.storyapp.data.local.UserPreferences
import com.example.storyapp.data.model.ErrorResponse
import com.example.storyapp.data.model.LoginResult
import com.example.storyapp.data.remote.ApiStoryService
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class UserRepository private constructor(
    private val apiService: ApiStoryService,
    private val application: Application,
    private val pref: UserPreferences
) {
    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            saveToken(response.loginResult)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        } catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            val errorMessage = exception.message ?: application.resources.getString(R.string.unknown_error)
            Log.e(TAG, "${application.resources.getString(R.string.login_error)}: $errorMessage")
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }
    }

    fun register(name:String, email:String, password:String) = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage))
        }catch (exception: IOException) {
            emit(Result.Error(application.resources.getString(R.string.network_error_message)))
        } catch (exception: Exception) {
            val errorMessage = exception.message ?: application.resources.getString(R.string.unknown_error)
            Log.e(TAG, "${application.resources.getString(R.string.login_error)}: $errorMessage")
            emit(Result.Error(exception.message ?: application.resources.getString(R.string.unknown_error)))
        }

    }

    suspend fun saveToken(data: LoginResult) {
        pref.saveUserLogin(data)
    }

    fun getUserLogin(): LiveData<LoginResult> {
        return pref.getUserLogin().asLiveData()
    }

    suspend fun deleteLogin() {
        pref.deleteUserLogin()
    }
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiStoryService,
            application: Application,
            pref: UserPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, application, pref)
            }.also { instance = it }

        private const val TAG = "UserRepository"
    }

}