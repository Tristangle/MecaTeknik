package com.example.mecateknik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mecateknik.data.model.forum.ForumTopic
import com.example.mecateknik.repositories.ForumRepository

class ForumViewModel(private val repository: ForumRepository) : ViewModel() {

    val topics: LiveData<List<ForumTopic>> = repository.topics

    fun fetchTopics() {
        repository.loadTopics()
    }
}
