package com.news.newssphere.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.news.newssphere.R
import com.news.newssphere.ui.navigation.BottomNavigation
import com.news.newssphere.utils.BottomNavItem

@Composable
fun NewsAppScreen(navController: NavController) {
    val navHostController = rememberNavController()
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Saved
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Red
            ) {
                navItems.forEachIndexed { _, navItem ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = colorResource(id = R.color.coral_red_25),
                            selectedIconColor = colorResource(id = R.color.space_cadet),
                            selectedTextColor = colorResource(id = R.color.space_cadet),
                            unselectedIconColor = colorResource(id = R.color.spanish_gray),
                            unselectedTextColor = colorResource(id = R.color.spanish_gray)
                        ),
                        icon = {
                            Icon(
                                painter = painterResource(id = navItem.icon),
                                contentDescription = navItem.title
                            )
                        },
                        label = {
                            Text(
                                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                text = navItem.title
                            )
                        },
                        onClick = {
                            navHostController.navigate(navItem.route) {
                                popUpTo(navHostController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        selected = currentRoute == navItem.route
                    )
                }
            }
        },
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding()
                ),
            color = Color.Transparent
        ) {
            BottomNavigation(navController, navHostController)
        }
    }
}