package com.example.storyapp.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class StoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,


	@field:SerializedName("listStory")
	val listStory: List<ListStoryItem>
)

@Parcelize
data class ListStoryItem(

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("name")
	val name: String,


	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("lat")
	val lon: Double? = null,

	@field:SerializedName("lon")
	val lat: Double? = null


): Parcelable
