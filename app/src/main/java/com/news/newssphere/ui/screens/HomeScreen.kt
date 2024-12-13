package com.news.newssphere.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.news.newssphere.ui.components.Articles
import com.news.newssphere.ui.components.ErrorPlaceholder
import com.news.newssphere.ui.components.ShimmerItemArticle
import com.news.newssphere.utils.UiData
import com.news.newssphere.viewmodel.NewsViewmodel

@Composable
fun HomeScreen(
    navController: NavController,
    newsVM: NewsViewmodel
) {
    LaunchedEffect(key1 = true) {
        newsVM.fetchArticles()
    }
    when (val articleData = newsVM.articles.collectAsState().value) {
        is UiData.Error -> {
            ErrorPlaceholder(
                description = articleData.message,
                title = "Couldn't load articles!"
            )
        }
        is UiData.Loading -> {
            ShimmerItemArticle(showShimmer = true)
        }
        is UiData.NoData -> {
            ErrorPlaceholder(
                description = "There are no articles related to your vicinity or country. Try modifying the filters.",
                title = "No articles!"
            )
        }
        is UiData.NoInternet -> {
            ErrorPlaceholder(
                description = "Failed to fetch articles. Please, check your internet connection!",
                title = "No internet connection!"
            )
        }
        is UiData.None -> {}
        is UiData.Success -> {
            ShimmerItemArticle(showShimmer = false)
            Articles(
                articles = articleData.data.filter { it.title != "[Removed]" },
                isHome = true,
                navController = navController,
                onDeleteArticle = {},
                showDelete = false
            )
        }
    }
}