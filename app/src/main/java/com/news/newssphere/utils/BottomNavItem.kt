package com.news.newssphere.utils

import com.news.newssphere.R

sealed class BottomNavItem(val route: String, val title: String, val icon: Int) {
    data object Home : BottomNavItem(Screens.HomeScreen.name, "Home", R.drawable.ic_home)
    data object Saved : BottomNavItem(Screens.SavedArticlesScreen.name, "Saved", R.drawable.ic_saved)
}