package com.example.mecateknik.data.model.forum

data class ForumMessage (
    val id: String,
    val topicId: String,
    val authorId: String,
    val content: String,
    val timestamp: Long
)