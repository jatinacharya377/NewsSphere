package com.news.newssphere.viewmodel

import android.content.Context
import com.assignment.ekacarenewsassignmentapp.viewmodel.ViewModelBase
import com.google.gson.Gson
import com.news.newssphere.R
import com.news.newssphere.data.model.Article
import com.news.newssphere.data.model.SavedArticle
import com.news.newssphere.data.model.response.ErrorResponse
import com.news.newssphere.data.remote.repository.NewsRepository
import com.news.newssphere.utils.NetworkHelper
import com.news.newssphere.utils.UiData
import com.news.newssphere.utils.toStringOrNA
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsViewmodel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: NewsRepository
): ViewModelBase() {

    private val _articleDeleted = MutableStateFlow<UiData<Boolean>>(UiData.Success(false))
    val articleDeleted = _articleDeleted
    private val _articles = MutableStateFlow<UiData<List<Article>>>(UiData.None())
    val articles = _articles
    private val _savedArticles = MutableStateFlow<UiData<List<SavedArticle>>>(UiData.None())
    val savedArticles = _savedArticles
    private val _toastMessage = MutableStateFlow<UiData<String>>(UiData.None())
    val toastMessage = _toastMessage

    fun deleteArticle(article: SavedArticle) {
        launchCoroutineScope {
            val rowCount = repository.deleteArticle(article)
            if (rowCount > 0) {
                _articleDeleted.value = UiData.Success(true)
                _toastMessage.value = UiData.Success("Article deleted successfully!")
            } else {
                _articleDeleted.value = UiData.Success(false)
                _toastMessage.value = UiData.Success("Article not found!")
            }
        }
    }

    fun fetchArticles() {
        launchCoroutineScope {
            _articles.value = UiData.Loading()
            val currentArticles = repository.fetchArticlesFromRoomDB()
            if (NetworkHelper.isInternetConnected(context)) {
                val response = repository.fetchArticlesFromApi(context.getString(R.string.API_KEY), "us")
                if (response.isSuccessful) {
                    response.body()?.let { responseBody ->
                        val newArticles = responseBody.articles
                        if (newArticles.isEmpty()) {
                            if (currentArticles.isNotEmpty()) _articles.value = UiData.Success(currentArticles)
                            else _articles.value = UiData.NoData()
                        } else {
                            if (newArticles.size != currentArticles.size ||
                                newArticles.any { it.title !in currentArticles.map { article -> article.title } }
                            ) {
                                repository.saveArticlesToRoomDB(newArticles)
                            }
                            _articles.value = UiData.Success(newArticles)
                        }
                    } ?: run {
                        if (currentArticles.isNotEmpty()) _articles.value = UiData.Success(currentArticles)
                        else _articles.value = UiData.NoData()
                    }
                } else {
                    if (currentArticles.isNotEmpty()) {
                        _articles.value = UiData.Success(currentArticles)
                    } else {
                        when (response.code()) {
                            400, 401, 500 -> {
                                response.errorBody()?.let {
                                    val errorResponse: ErrorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                                    _articles.value = UiData.Error(errorResponse.message)
                                }
                            }
                            else -> _articles.value = UiData.Error("Oops! Something went wrong. There was an error in fetching articles.")
                        }
                    }
                }
            } else {
                if (currentArticles.isNotEmpty()) _articles.value = UiData.Success(currentArticles)
                else _articles.value = UiData.NoInternet()
            }
        }
    }

    fun fetchSavedArticles() {
        launchCoroutineScope {
            _savedArticles.value = UiData.Loading()
            val articles = repository.fetchSavedArticlesFromRoomDB()
            if (articles.isEmpty()) _savedArticles.value = UiData.NoData()
            else _savedArticles.value = UiData.Success(articles)
        }
    }

    fun saveArticle(article: Article) {
        launchCoroutineScope {
            val isArticleSaved = repository.isArticleSaved(article.title.toStringOrNA())
            if (isArticleSaved) {
                _toastMessage.value = UiData.Success("You have already saved this article!")
            } else {
                val savedArticle = SavedArticle(
                    author = article.title,
                    content = article.content,
                    description = article.description,
                    publishedAt = article.publishedAt,
                    source = article.source,
                    title = article.title,
                    url = article.url,
                    urlToImage = article.urlToImage,
                )
                repository.saveArticleToRoomDB(savedArticle)
                _toastMessage.value = UiData.Success("You have successfully bookmarked and saved this article. You can check it in Saved section of the app.")
            }
        }
    }

    override fun onCreate() {}
}