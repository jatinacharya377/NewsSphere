package com.news.newssphere.data.remote.api

import com.news.newssphere.data.model.response.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun fetchArticles(
        @Query("apiKey") apiKey: String,
        @Query("country") country: String
    ): Response<ArticleResponse>
}