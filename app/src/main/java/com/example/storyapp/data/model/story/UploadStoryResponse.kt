package com.example.storyapp.data.model.story

import com.google.gson.annotations.SerializedName

data class UploadStoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
