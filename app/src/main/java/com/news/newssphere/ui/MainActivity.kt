package com.news.newssphere.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.news.newssphere.ui.navigation.Navigation
import com.news.newssphere.ui.theme.NewsSphereTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsSphereTheme {
                Navigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EkaCareNewsAppPreview() {
    NewsSphereTheme {
        Navigation()
    }
}