package com.soheibbettahar.litarus.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error<T>(val exception: Throwable? = null) : Result<T>()

    fun <T> Flow<T>.asResult(): Flow<Result<T>> =
        this.map<T, Result<T>> { Success(it) }
            .onStart { emit(Loading) }
            .catch { emit(Error(it)) }
}
