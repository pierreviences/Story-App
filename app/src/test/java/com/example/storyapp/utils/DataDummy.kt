package com.example.storyapp.utils

import com.example.storyapp.data.model.story.ListStoryItem
import com.example.storyapp.data.model.story.StoryResponse

object DataDummy {
    const val DUMMY_TOKEN = "TOKEN"
    fun generateDummyStory(): List<ListStoryItem> {
        val stories = mutableListOf<ListStoryItem>()
        for (i in 1..10) {
            val storyItem = ListStoryItem(
                id = "story-V1NgjIFaBwi_T18_",
                name = "yazid",
                description = "p",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1698067138823_-36zx7Xu.jpg",
                createdAt = "2023-10-23T13:18:58.828Z",
                lat = -6.5420675,
                lon = 106.8245843
            )
            stories.add(storyItem)
        }

        return stories
    }


}