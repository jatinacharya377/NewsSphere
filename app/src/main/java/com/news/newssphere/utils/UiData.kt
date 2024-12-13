package com.news.newssphere.utils

sealed class UiData<T> {
    class Error<T>(val message: String): UiData<T>()
    class Loading<T>: UiData<T>()
    class NoData<T>: UiData<T>()
    class NoInternet<T>: UiData<T>()
    class None<T>: UiData<T>()
    class Success<T>(val data: T): UiData<T>()
}