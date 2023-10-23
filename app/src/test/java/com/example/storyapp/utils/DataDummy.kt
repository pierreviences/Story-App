package com.example.storyapp.utils

import com.example.storyapp.data.model.story.ListStoryItem
import com.example.storyapp.data.model.story.StoryResponse

object DataDummy {
    const val DUMMY_TOKEN = "TOKEN"
    fun generateDummyStory(): List<ListStoryItem> {
        val stories = mutableListOf<ListStoryItem>()
        for (i in 1..10) {
            val storyItem = ListStoryItem(
                id = i.toString(),
                name = "name $i",
                description = "desc $i",
                photoUrl = "photo $i",
                createdAt = "createdAr $i",
                lon = i.toDouble(),
                lat = i.toDouble(),
            )
            stories.add(storyItem)
        }

        return stories
    }


}