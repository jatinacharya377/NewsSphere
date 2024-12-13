package com.news.newssphere.data.model.response

import com.news.newssphere.data.model.Article

data class ArticleResponse(
    val articles: List<Article>,
    val status: String?,
    val totalResults: Int?
)
