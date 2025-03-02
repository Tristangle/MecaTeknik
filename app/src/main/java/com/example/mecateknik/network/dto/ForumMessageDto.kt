package com.example.mecateknik.network.dto

import com.google.gson.annotations.SerializedName

data class ForumMessageDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("topic_id")
    val topicId: String,
    @SerializedName("author_id")
    val authorId: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("timestamp")
    val timestamp: Long
)