package com.news.newssphere.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.news.newssphere.data.model.Article
import com.news.newssphere.data.model.SavedArticle
import com.news.newssphere.data.model.SourceTypeConverter
import com.news.newssphere.data.room.NewsDao

@Database(
    entities = [Article::class, SavedArticle::class],
    version = 1
)
@TypeConverters(
    SourceTypeConverter::class
)
abstract class NewsRoomDatabase: RoomDatabase() {

    abstract fun getDao(): NewsDao
}