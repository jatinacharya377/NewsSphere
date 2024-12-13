package com.news.newssphere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.news.newssphere.R
import com.news.newssphere.data.model.SavedArticle
import com.news.newssphere.ui.components.Articles
import com.news.newssphere.ui.components.ErrorPlaceholder
import com.news.newssphere.ui.components.ShimmerItemArticle
import com.news.newssphere.utils.UiData
import com.news.newssphere.utils.toast
import com.news.newssphere.viewmodel.NewsViewmodel

@Composable
private fun ConfirmationDialog(
    onClickDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    val fontFamily = FontFamily(Font(R.font.poppins_regular))
    AlertDialog(
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.coral_red)),
                onClick = {
                    onClickDelete.invoke()
                }
            ) {
                Text(
                    color = Color.White,
                    fontFamily = fontFamily,
                    fontSize = 14.sp,
                    text = "Delete"
                )
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(Color.White),
                onClick = {
                    onDismiss.invoke()
                },
            ) {
                Text(
                    color = colorResource(id = R.color.space_cadet),
                    fontFamily = fontFamily,
                    fontSize = 14.sp,
                    text = "Cancel"
                )
            }
        },
        onDismissRequest = {
            onDismiss.invoke()
        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.space_cadet),
                fontFamily = fontFamily,
                fontSize = 12.sp,
                text = "Are you sure you want to delete this article? It can not be undone once deleted."
            )
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "info"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    color = colorResource(id = R.color.space_cadet),
                    fontFamily = fontFamily,
                    fontSize = 16.sp,
                    text = "Delete Subject"
                )
            }
        }
    )
}

@Composable
fun SavedArticlesScreen(
    navController: NavController,
    newsVM: NewsViewmodel
) {
    val context = LocalContext.current
    var savedArticle by remember {
        mutableStateOf<SavedArticle?>(null)
    }
    var showConfirmationDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(
        key1 = newsVM.articleDeleted.collectAsState().value,
        key2 = newsVM.toastMessage.collectAsState().value
    ) {
        when (val articleDelete = newsVM.articleDeleted.value) {
            is UiData.Error -> {}
            is UiData.Loading -> {}
            is UiData.NoData -> {}
            is UiData.NoInternet -> {}
            is UiData.None -> {}
            is UiData.Success -> {
                showConfirmationDialog = false
                if (articleDelete.data) {
                    newsVM.fetchSavedArticles()
                }
            }
        }
        when (val toastMessage = newsVM.toastMessage.value) {
            is UiData.Error -> {}
            is UiData.Loading -> {}
            is UiData.NoData -> {}
            is UiData.NoInternet -> {}
            is UiData.None -> {}
            is UiData.Success -> context.toast(toastMessage.data)
        }
    }
    LaunchedEffect(key1 = true) {
        newsVM.fetchSavedArticles()
    }
    when (val articleData = newsVM.savedArticles.collectAsState().value) {
        is UiData.Error -> {}
        is UiData.Loading -> {
            ShimmerItemArticle(showShimmer = true)
            showConfirmationDialog = false
        }
        is UiData.NoData -> {
            ErrorPlaceholder(
                description = "You can mark and save some of the articles to in your saved section.",
                title = "No articles saved yet!"
            )
        }
        is UiData.NoInternet -> {}
        is UiData.None -> {}
        is UiData.Success -> {
            ShimmerItemArticle(showShimmer = false)
            Articles(
                articles = articleData.data.filter { it.title != "[Removed]" },
                isHome = false,
                navController = navController,
                onDeleteArticle = { article ->
                    savedArticle = article
                    showConfirmationDialog = true
                },
                showDelete = true
            )
        }
    }
    if (showConfirmationDialog) {
        ConfirmationDialog(
            onClickDelete = {
                savedArticle?.let {
                    newsVM.deleteArticle(it)
                }
            },
            onDismiss = {
                showConfirmationDialog = false
            }
        )
    }
}