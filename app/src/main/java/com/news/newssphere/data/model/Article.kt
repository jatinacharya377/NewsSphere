package com.news.newssphere.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)
