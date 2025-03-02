package com.example.mecateknik.api

import retrofit2.http.GET
import retrofit2.http.Query

data class YouTubeSearchResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    val videoId: String
)

data class Snippet(
    val title: String,
    val description: String
)

interface YouTubeApiService {
    @GET("search")

    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("q") query: String,
        @Query("key") key: String,
        @Query("maxResults") maxResults: Int = 1,
        @Query("type") type: String = "video"
    ): YouTubeSearchResponse
}
