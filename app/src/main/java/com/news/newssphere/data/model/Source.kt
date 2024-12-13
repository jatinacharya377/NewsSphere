package com.news.newssphere.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson

data class Source(
    val id: String?,
    val name: String?
)

class SourceTypeConverter {
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return if (source == null) null else Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(source: String?): Source? {
        return if (source == null) null else Gson().fromJson(source, Source::class.java)
    }
}
