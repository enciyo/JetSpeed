package com.example.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

abstract class UseCase<in I, out O> {

    fun execute(request: I) =
        invoke(request)
            .catch { t ->
                Result.failure<O>(t)
            }

    abstract fun invoke(request: I): Flow<Result<O>>
}