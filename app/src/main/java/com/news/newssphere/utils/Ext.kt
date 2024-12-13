package com.news.newssphere.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.gson.Gson

inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson(this, T::class.java)
}

fun Context.shareArticle(url: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.setType("text/plain")
    intent.putExtra(
        Intent.EXTRA_TEXT,
        "Hey! Check out this article: $url"
    )
    this.startActivity(intent)
}

inline fun <reified T> T.toJson(): String {
    return Gson().toJson(this)
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String?.toStringOrNA(): String {
    return this ?: "N/A"
}