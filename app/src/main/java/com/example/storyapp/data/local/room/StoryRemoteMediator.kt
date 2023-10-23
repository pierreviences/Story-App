package com.example.storyapp.data.local.room

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyapp.data.remote.ApiStoryService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val token: String,
    private val apiService: ApiStoryService,
    private val database: StoryDatabase
) : RemoteMediator<Int, StoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX
        try {
            val responseData = apiService.getStories(token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.listStory.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAllStories()
                }
                responseData.listStory.forEach { storyItem ->
                    val story = StoryEntity(
                        storyItem.id,
                        storyItem.name,
                        storyItem.description,
                        storyItem.photoUrl,
                        storyItem.createdAt,
                        storyItem.lat,
                        storyItem.lon,
                    )

                    database.storyDao().insertStories(story)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

}