package com.example.mecateknik.network.services

import com.example.mecateknik.network.dto.ForumMessageDto
import com.example.mecateknik.network.dto.ForumTopicDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ForumService {
    @GET("topics")
    fun getAllTopics(): Call<List<ForumTopicDto>>

    @GET("topics/{topicId}/messages")
    fun getMessagesByTopic(@Path("topicId") topicId: String): Call<List<ForumMessageDto>>

    @POST("topics")
    fun createTopic(@Body topic: ForumTopicDto): Call<ForumTopicDto>

    @POST("topics/{topicId}/messages")
    fun postMessage(@Path("topicId") topicId: String, @Body message: ForumMessageDto): Call<ForumMessageDto>
}
