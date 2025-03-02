package com.example.mecateknik.utils

import android.content.Context
import java.util.Properties

object ApiKeyProvider {
    fun getYoutubeApiKey(context: Context): String? {
        return try {
            val properties = Properties()
            context.assets.open("apikey.properties").use { inputStream ->
                properties.load(inputStream)
            }
            properties.getProperty("YOUTUBE_API_KEY")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
