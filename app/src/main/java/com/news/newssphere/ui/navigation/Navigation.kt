package com.news.newssphere.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.news.newssphere.data.model.Article
import com.news.newssphere.data.model.SavedArticle
import com.news.newssphere.ui.screens.ArticleDetailsScreen
import com.news.newssphere.ui.screens.NewsAppScreen
import com.news.newssphere.ui.screens.SplashScreen
import com.news.newssphere.utils.Screens
import com.news.newssphere.utils.fromJson
import com.news.newssphere.utils.toStringOrNA
import com.news.newssphere.viewmodel.NewsViewmodel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.name
    ) {
        composable(route = Screens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(
            route = Screens.NewsAppScreen.name,
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            }
        ) {
            NewsAppScreen(navController = navController)
        }
        composable(
            route = "${Screens.ArticleDetailsScreen.name}/{article}/{is_home}",
            arguments = listOf(
                navArgument("article") {
                    type = NavType.StringType
                },
                navArgument("is_home") {
                    type = NavType.BoolType
                }
            ),
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(500)
                )
            },
            popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(500)
                )
            }
        ) { backstackEntry ->
            val newsVM = hiltViewModel<NewsViewmodel>()
            val isHome = backstackEntry.arguments?.getBoolean("is_home") ?: true
            val articleJson = backstackEntry.arguments?.getString("article").toStringOrNA()
            var article: Article? = null
            var savedArticle: SavedArticle? = null
            if (isHome) {
                article = articleJson.fromJson<Article>()
            } else {
                savedArticle = articleJson.fromJson<SavedArticle>()
            }
            ArticleDetailsScreen(
                article = article,
                navController = navController,
                newsVM = newsVM,
                savedArticle = savedArticle
            )
        }
    }
}