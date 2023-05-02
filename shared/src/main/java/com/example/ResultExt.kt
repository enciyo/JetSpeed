package com.example

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach


fun <T> Flow<Result<T>>.onSuccess(
    onSuccess: (value: T) -> Unit,
) =
    this
        .onEach {
            it.getOrNull()?.let(onSuccess)
        }

fun <T> Flow<Result<T>>.onFailure(
    onFailure: (exception: Throwable) -> Unit
) =
    this
        .onEach {
            it.exceptionOrNull()?.let(onFailure)
        }

