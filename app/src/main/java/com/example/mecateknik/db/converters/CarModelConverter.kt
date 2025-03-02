package com.example.mecateknik.db.converters

import androidx.room.TypeConverter

class CarModelConverter {
    @TypeConverter
    fun fromCarModelList(models: List<String>?): String? {
        return models?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toCarModelList(data: String?): List<String>? {
        return data?.split(",")?.map { it.trim() }
    }
}
