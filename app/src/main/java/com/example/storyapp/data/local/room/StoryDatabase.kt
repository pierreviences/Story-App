package com.example.storyapp.data.local.room

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapp.data.model.story.ListStoryItem

@Database(
    entities = [ListStoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(application: Application): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    application,
                    StoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}