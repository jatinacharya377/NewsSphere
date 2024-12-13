package com.assignment.ekacarenewsassignmentapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class ViewModelBase : ViewModel() {

    fun launchCoroutineScope(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO, CoroutineStart.DEFAULT, block)
    }

    abstract fun onCreate()
}