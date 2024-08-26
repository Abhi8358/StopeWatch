package com.vedic.stopewatch.domain

import kotlinx.coroutines.flow.Flow

interface TimerRepo {

    suspend fun pause()
    suspend fun stop()
    suspend fun reset()
    suspend fun startAndGetElapsedTime(): Flow<Long>
}