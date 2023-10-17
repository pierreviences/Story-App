package com.example.storyapp.data.remote

import com.example.storyapp.data.model.LoginResponse
import com.example.storyapp.data.model.RegisterResponse
import com.example.storyapp.data.model.StoryResponse
import com.example.storyapp.data.model.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiStoryService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
    @GET("stories")
    suspend fun getStories(): StoryResponse

    @Multipart
    @POST("stories/guest")
    fun uploadStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): UploadStoryResponse

}