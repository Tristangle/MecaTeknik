package com.example.mecateknik.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mecateknik.data.model.forum.ForumTopic
import com.example.mecateknik.network.dto.ForumTopicDto
import com.example.mecateknik.network.mapper.TopicMapper
import com.example.mecateknik.network.services.ForumService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumRepository(private val forumService: ForumService) {

    private val _topics = MutableLiveData<List<ForumTopic>>()
    val topics: LiveData<List<ForumTopic>> get() = _topics

    fun loadTopics() {
        val call: Call<List<ForumTopicDto>> = forumService.getAllTopics()
        call.enqueue(object : Callback<List<ForumTopicDto>> {
            override fun onResponse(
                call: Call<List<ForumTopicDto>>,
                response: Response<List<ForumTopicDto>>
            ) {
                if (response.isSuccessful) {
                    val topicsDto = response.body() ?: emptyList()
                    // Mapping de DTO vers ton modèle (par exemple via TopicMapper)
                    val topics = topicsDto.map { TopicMapper().mapTopicDtoToForumTopic(it) }
                    _topics.value = topics
                }
            }

            override fun onFailure(call: Call<List<ForumTopicDto>>, t: Throwable) {
                // Log de l'erreur
                Log.e("ForumRepository", "Error loading topics: ${t.message}")
            }
        })
    }

}
