package com.example.storyapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.model.story.ListStoryItem

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, ListStoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(vararg storyEntity: ListStoryItem)

    @Query("DELETE FROM story")
    fun deleteAllStories()
}