package com.example.storyapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
}