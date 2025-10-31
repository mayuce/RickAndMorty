package com.aliyuce.rickandmorty.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(separator = "||")

    @TypeConverter
    fun toStringList(data: String?): List<String> = data?.takeIf { it.isNotEmpty() }?.split("||") ?: emptyList()
}
