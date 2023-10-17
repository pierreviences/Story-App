package com.example.storyapp.data.remote

import com.example.storyapp.BuildConfig
import com.example.storyapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiStoryConfig {
    companion object {
        fun getApiService(token: String? = null): ApiStoryService {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)

            token?.let {
                val authInterceptor = Interceptor { chain ->
                    val req = chain.request()
                    val requestHeaders = req.newBuilder()
                        .addHeader("Authorization", "Bearer $it")
                        .build()
                    chain.proceed(requestHeaders)
                }
                clientBuilder.addInterceptor(authInterceptor)
            }

            val client = clientBuilder.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiStoryService::class.java)
        }

    }
}