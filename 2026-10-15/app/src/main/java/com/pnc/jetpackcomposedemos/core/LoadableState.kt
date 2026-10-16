package com.pnc.jetpackcomposedemos.core

sealed interface LoadableState<out T> {
    data object Loading: LoadableState<Nothing>
    data object Empty: LoadableState<Nothing>
    data class Success<T>(
        val data: T
    ): LoadableState<T>
    data class Error(
        val message: String
    ): LoadableState<Nothing>
}


