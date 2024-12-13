package com.news.newssphere.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.news.newssphere.ui.screens.HomeScreen
import com.news.newssphere.ui.screens.SavedArticlesScreen
import com.news.newssphere.utils.Screens
import com.news.newssphere.viewmodel.NewsViewmodel

@Composable
fun BottomNavigation(
    navController: NavController,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.HomeScreen.name
    ) {
        composable(Screens.HomeScreen.name) {
            val newsVM = hiltViewModel<NewsViewmodel>()
            HomeScreen(
                navController = navController,
                newsVM = newsVM
            )
        }
        composable(Screens.SavedArticlesScreen.name) {
            val newsVM = hiltViewModel<NewsViewmodel>()
            SavedArticlesScreen(
                navController = navController,
                newsVM = newsVM
            )
        }
    }
}