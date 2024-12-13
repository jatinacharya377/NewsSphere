package com.news.newssphere.ui.components

import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.news.newssphere.R
import com.news.newssphere.data.model.Article
import com.news.newssphere.data.model.SavedArticle
import com.news.newssphere.utils.Screens
import com.news.newssphere.utils.shareArticle
import com.news.newssphere.utils.toJson
import com.news.newssphere.utils.toStringOrNA

@Composable
inline fun <reified T> Article(
    isHome: Boolean,
    item: T,
    navController: NavController,
    crossinline onDeleteArticle: (SavedArticle) -> Unit,
    showDelete: Boolean
) {
    var article: Any? = null
    var articleDescription by remember {
        mutableStateOf("")
    }
    var articleHeading by remember {
        mutableStateOf("")
    }
    var articleImage by remember {
        mutableStateOf<Any?>(null)
    }
    val context = LocalContext.current
    var articleUrl by remember {
        mutableStateOf("")
    }
    when (T::class) {
        Article::class -> {
            article = item as Article
            articleDescription = article.description.toStringOrNA()
            articleImage = if (article.urlToImage.isNullOrEmpty()) R.drawable.ic_app_logo_placeholder
            else article.urlToImage
            articleHeading = article.title.toStringOrNA()
            articleUrl = article.url.toStringOrNA()
        }
        SavedArticle::class -> {
            article = item as SavedArticle
            articleDescription = article.description.toStringOrNA()
            articleImage = if (article.urlToImage.isNullOrEmpty()) R.drawable.ic_app_logo_placeholder
            else article.urlToImage
            articleHeading = article.title.toStringOrNA()
            articleUrl = article.url.toStringOrNA()
        }
    }
    var isLoading by remember {
        mutableStateOf(true)
    }
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                end = 16.dp,
                start = 16.dp,
                top = 14.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        val constraints = ConstraintSet {
            val articleDesc = createRefFor("article_description")
            val articleImg = createRefFor("article_image")
            val articleTitle = createRefFor("article_title")
            val delete = createRefFor("delete_article")
            val loadingPlaceholder = createRefFor("loading_placeholder")
            val readMore = createRefFor("read_more")
            val share = createRefFor("share")

            constrain(articleDesc) {
                end.linkTo(parent.end, margin = 12.dp)
                start.linkTo(articleImg.end, margin = 16.dp)
                top.linkTo(articleTitle.bottom, margin = 4.dp)
                width = Dimension.fillToConstraints
            }
            constrain(articleImg) {
                start.linkTo(parent.start, margin = 12.dp)
                top.linkTo(parent.top, margin = 12.dp)
                width = Dimension.value(80.dp)
                height = Dimension.value(80.dp)
            }
            constrain(articleTitle) {
                end.linkTo(delete.start, margin = 12.dp)
                start.linkTo(articleImg.end, margin = 16.dp)
                top.linkTo(articleImg.top)
                width = Dimension.fillToConstraints
            }
            constrain(delete) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
            constrain(loadingPlaceholder) {
                start.linkTo(parent.start, margin = 12.dp)
                top.linkTo(parent.top, margin = 12.dp)
                width = Dimension.value(80.dp)
                height = Dimension.value(80.dp)
            }
            constrain(readMore) {
                bottom.linkTo(parent.bottom, margin = 12.dp)
                end.linkTo(parent.end, margin = 12.dp)
                top.linkTo(articleDesc.bottom, margin = 6.dp)
                width = Dimension.wrapContent
            }
            constrain(share) {
                bottom.linkTo(parent.bottom, margin = 12.dp)
                end.linkTo(readMore.start, margin = 20.dp)
                top.linkTo(articleDesc.bottom, margin = 6.dp)
                width = Dimension.wrapContent
            }
        }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    article?.let {
                        navController.navigate(
                            "${Screens.ArticleDetailsScreen.name}/${
                                Uri.encode(
                                    article.toJson()
                                )
                            }/$isHome"
                        )
                    }
                },
            constraintSet = constraints
        ) {
            AsyncImage(
                modifier = Modifier
                    .layoutId("article_image")
                    .clip(RoundedCornerShape(8.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(articleImage)
                    .crossfade(true)
                    .error(R.drawable.ic_app_logo_placeholder)
                    .listener(
                        onError = { _, _ -> isLoading = false },
                        onStart = { isLoading = true },
                        onSuccess = { _, _ -> isLoading = false }
                    )
                    .build(),
                contentDescription = "article image",
                contentScale = ContentScale.Crop
            )
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .layoutId("loading_placeholder")
                        .background(color = Color.Transparent)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colorResource(id = R.color.coral_red),
                        strokeCap = StrokeCap.Round
                    )
                }
            }
            Text(
                modifier = Modifier.layoutId("article_title"),
                color = colorResource(id = R.color.onyx),
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                text = articleHeading
            )
            Text(
                modifier = Modifier.layoutId("article_description"),
                color = colorResource(id = R.color.spanish_gray),
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 12.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                text = articleDescription
            )
            if (showDelete) {
                Card(
                    modifier = Modifier.layoutId("delete_article"),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(id = R.color.coral_red_25)
                    ),
                    onClick = {
                        onDeleteArticle.invoke(article as SavedArticle)
                    },
                    shape = CircleShape
                ) {
                    Icon(
                        modifier = Modifier.padding(6.dp),
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "delete",
                        tint = Color.White
                    )
                }
            }
            Surface(
                modifier = Modifier
                    .layoutId("read_more")
                    .clickable {
                        article?.let {
                            navController.navigate(
                                "${Screens.ArticleDetailsScreen.name}/${
                                    Uri.encode(
                                        article.toJson()
                                    )
                                }/$isHome"
                            )
                        }
                    },
                color = Color.Transparent
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    color = colorResource(id = R.color.celtic_blue),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 12.sp,
                    text = "Read More..."
                )
            }
            Row(
                modifier = Modifier
                    .layoutId("share")
                    .clickable {
                        if (articleUrl.isNotEmpty() || articleUrl != "N/A")
                            context.shareArticle(articleUrl)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    color = colorResource(id = R.color.celtic_blue),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 12.sp,
                    text = "Share"
                )
                Icon(
                    modifier = Modifier.padding(
                        end = 4.dp,
                        start = 10.dp
                    ),
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "share",
                    tint = colorResource(id = R.color.celtic_blue)
                )
            }
        }
    }
}

@Composable
inline fun <reified T> Articles(
    articles: List<T>,
    isHome: Boolean,
    navController: NavController,
    crossinline onDeleteArticle: (SavedArticle) -> Unit,
    showDelete: Boolean,
) {
    Scaffold(
        modifier = Modifier.fillMaxHeight()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding()),
            contentPadding = PaddingValues(bottom = 14.dp)
        ) {
            items(
                items = articles
            ) { article ->
                Article(
                    isHome = isHome,
                    item = article,
                    navController = navController,
                    onDeleteArticle = { savedArticle ->
                        onDeleteArticle.invoke(savedArticle)
                    },
                    showDelete = showDelete
                )
            }
        }
    }
}

@Composable
fun ErrorPlaceholder(
    description: String,
    title: String
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "info"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 14.sp,
                    text = title
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontSize = 12.sp,
                    text = description
                )
            }
        }
    }
}

@Composable
fun ShimmerEffect(
    modifier: Modifier,
    angleOfAxisY: Float = 200f,
    durationMillis: Int = 1000,
    showShimmer: Boolean,
    widthOfShadowBrush: Int = 700
) {
    val brush: Brush
    if (showShimmer) {
        val shimmerColors = listOf(
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 1.0f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.3f)
        )
        val transition = rememberInfiniteTransition("shimmer_effect_transition")
        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer_loading_animation"
        )
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(
                x = translateAnimation.value - widthOfShadowBrush,
                y = 0f
            ),
            end = Offset(
                x = translateAnimation.value,
                y = angleOfAxisY
            )
        )
    } else {
        brush = Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero,
        )
    }
    Box(modifier = modifier) {
        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(brush)
        )
    }
}

@Composable
fun ShimmerItemArticle(
    showShimmer: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        repeat(6) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = 16.dp,
                        start = 16.dp,
                        top = 12.dp
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(2.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    ShimmerEffect(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        showShimmer = showShimmer
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        ShimmerEffect(
                            modifier = Modifier
                                .width(160.dp)
                                .height(16.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            showShimmer = showShimmer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ShimmerEffect(
                            modifier = Modifier
                                .width(120.dp)
                                .height(14.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            showShimmer = showShimmer
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Toolbar(
    navController: NavController,
    title: String
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(Color.Transparent),
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(start = 6.dp),
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "back"
                )
            }
        },
        title = {
            Text(
                modifier = Modifier.padding(start = 20.dp),
                color = colorResource(id = R.color.onyx),
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 16.sp,
                text = title
            )
        }
    )
}