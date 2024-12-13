package com.news.newssphere.data.remote.repository

import com.news.newssphere.data.model.Article
import com.news.newssphere.data.model.SavedArticle
import com.news.newssphere.data.remote.api.NewsApi
import com.news.newssphere.data.room.NewsDao
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApi,
    private val dao: NewsDao
) {
    suspend fun deleteArticle(article: SavedArticle) = dao.deleteArticle(article)

    suspend fun fetchArticlesFromApi(
        apiKey: String,
        country: String
    ) = api.fetchArticles(apiKey, country)

    suspend fun fetchArticlesFromRoomDB() = dao.fetchArticles()

    suspend fun fetchSavedArticlesFromRoomDB() = dao.fetchSavedArticles()

    suspend fun isArticleSaved(title: String) = dao.isArticleSaved(title)

    suspend fun saveArticleToRoomDB(article: SavedArticle) = dao.saveArticle(article)

    suspend fun saveArticlesToRoomDB(articles: List<Article>) = dao.saveArticles(articles)
}