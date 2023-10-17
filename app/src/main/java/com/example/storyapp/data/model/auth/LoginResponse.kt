package com.example.storyapp.data.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("loginResult")
	val loginResult: LoginResult,
)

@Parcelize
data class LoginResult(

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("token")
	val token: String
): Parcelable
