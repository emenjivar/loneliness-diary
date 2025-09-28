package com.emenjivar.feature.diary.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

private const val SUBSCRIBED_TIME = 5_000L
fun <T> Flow<T>.stateInDefault(
    scope: CoroutineScope,
    initialValue: T
) = this.stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(SUBSCRIBED_TIME),
    initialValue = initialValue
)