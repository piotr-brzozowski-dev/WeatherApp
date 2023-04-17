package com.example.weatherapp

import java.util.concurrent.CancellationException

inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        else Result.failure(e)
    }
}