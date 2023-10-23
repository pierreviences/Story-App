package com.example.storyapp.data.local.room
@Parcelize
@Entity(tableName = "story")
data class StoryEntity(
    @field:ColumnInfo(name = "photoUrl")
    val photoUrl: String,

    @field:ColumnInfo(name = "createdAt")
    val createdAt: String,

    val name: String,

    val description: String,

    val lon: Double?,

    val lat: Double?,

    @PrimaryKey
    val id: String,
) : Parcelable