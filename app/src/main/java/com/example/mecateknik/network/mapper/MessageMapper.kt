package com.example.mecateknik.network.mapper

import com.example.mecateknik.data.model.forum.ForumMessage
import com.example.mecateknik.network.dto.ForumMessageDto

class MessageMapper {

    fun mapMessageDtoToForumMessage(dto: ForumMessageDto): ForumMessage {
        return ForumMessage(
            id = dto.id,
            topicId = dto.topicId,
            authorId = dto.authorId,
            content = dto.content,
            timestamp = dto.timestamp
        )
    }
}