package com.news.newssphere.ui.screens

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.news.newssphere.R
import com.news.newssphere.data.model.Article
import com.news.newssphere.data.model.SavedArticle
import com.news.newssphere.ui.components.Toolbar
import com.news.newssphere.utils.UiData
import com.news.newssphere.utils.shareArticle
import com.news.newssphere.utils.toStringOrNA
import com.news.newssphere.utils.toast
import com.news.newssphere.viewmodel.NewsViewmodel

@Composable
fun ArticleDetailsScreen(
    article: Article?,
    navController: NavController,
    newsVM: NewsViewmodel,
    savedArticle: SavedArticle?
) {
    var isLoading by remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = newsVM.toastMessage.collectAsState().value) {
        when (val toastMessage = newsVM.toastMessage.value) {
            is UiData.Error -> {}
            is UiData.Loading -> {}
            is UiData.NoData -> {}
            is UiData.NoInternet -> {}
            is UiData.None -> {}
            is UiData.Success -> context.toast(toastMessage.data)
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        floatingActionButton = {
            if (!isLoading) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    FloatingActionButton(
                        containerColor = colorResource(id = R.color.coral_red),
                        onClick = {
                            var url = ""
                            article?.let {
                                url = article.url.toStringOrNA()
                            }
                            savedArticle?.let {
                                url = savedArticle.url.toStringOrNA()
                            }
                            if (url.isNotEmpty() || url != "N/A")
                                context.shareArticle(url)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_share_content),
                            contentDescription = "bookmark",
                            tint = Color.White
                        )
                    }
                    FloatingActionButton(
                        modifier = Modifier.padding(top = 10.dp),
                        containerColor = colorResource(id = R.color.coral_red),
                        onClick = {
                            article?.let {
                                newsVM.saveArticle(article)
                            }
                            savedArticle?.let {
                                context.toast("You have already saved this article!")
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark),
                            contentDescription = "bookmark",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        topBar = {
            Toolbar(
                navController = navController,
                title = "Content"
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                                isLoading = false
                            }
                        }
                        article?.let { mArticle ->
                            loadUrl(mArticle.url.toStringOrNA())
                        }
                        savedArticle?.let { mSavedArticle ->
                            loadUrl(mSavedArticle.url.toStringOrNA())
                        }
                    }
                }
            )
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(id = R.color.coral_red),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}