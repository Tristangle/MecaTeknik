package com.example.mecateknik.network.mapper

import com.example.mecateknik.data.model.forum.ForumTopic
import com.example.mecateknik.network.dto.ForumTopicDto

class TopicMapper {
    fun mapTopicDtoToForumTopic(dto: ForumTopicDto): ForumTopic {
        return ForumTopic(
            id = dto.id,
            title = dto.title,
            creatorId = dto.creatorId,
            creationDate = dto.creationDate
        )
    }
}