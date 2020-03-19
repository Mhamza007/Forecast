package com.mhamza007.forecast.internal

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

fun <T> Task<T>.asDeferredAsync(): Deferred<T> {
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener {
        deferred.complete(it) // result
    }

    this.addOnFailureListener {
        deferred.completeExceptionally(it) //exception
    }

    return deferred
}