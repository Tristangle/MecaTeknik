package com.example.mecateknik.di

import com.example.mecateknik.network.services.ForumService
import com.example.mecateknik.repositories.ForumRepository
import com.example.mecateknik.viewmodel.ForumViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL_FORUM = "https://my-json-server.typicode.com/tristangle/forum-api/"

val forumModule = module {
    // Fournir OkHttpClient
    single<OkHttpClient> {
        OkHttpClient.Builder().build()
    }

    // Fournir Retrofit configuré pour l'API du forum
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL_FORUM)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Fournir le ForumService via Retrofit
    single<ForumService> {
        get<Retrofit>().create(ForumService::class.java)
    }

    // Fournir le ForumRepository
    single<ForumRepository> {
        ForumRepository(get())
    }

    // Fournir le ForumViewModel
    viewModel {
        ForumViewModel(get())
    }
}
