package com.news.newssphere.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.news.newssphere.data.model.Article
import com.news.newssphere.data.model.SavedArticle

@Dao
interface NewsDao {
    @Delete
    suspend fun deleteArticle(article: SavedArticle): Int

    @Query("SELECT * FROM tb_articles ORDER BY publishedAt DESC")
    suspend fun fetchArticles(): List<Article>

    @Query("SELECT * FROM tb_saved_articles ORDER BY publishedAt DESC")
    suspend fun fetchSavedArticles(): List<SavedArticle>

    @Query("SELECT EXISTS(SELECT 1 FROM tb_saved_articles WHERE title = :title)")
    suspend fun isArticleSaved(title: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: SavedArticle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticles(articles: List<Article>)
}