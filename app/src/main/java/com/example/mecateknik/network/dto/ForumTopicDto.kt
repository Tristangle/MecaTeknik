package com.example.mecateknik.network.dto

import com.google.gson.annotations.SerializedName

data class ForumTopicDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("creator_id")
    val creatorId: String,
    @SerializedName("creation_date")
    val creationDate: Long
)