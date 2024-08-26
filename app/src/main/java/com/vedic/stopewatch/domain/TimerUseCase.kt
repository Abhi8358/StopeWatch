package com.vedic.stopewatch.domain

import com.vedic.stopewatch.data.model.TimerViewData
import kotlinx.coroutines.flow.Flow

interface TimerUseCase {
    suspend fun pause()
    suspend fun stop()
    suspend fun reset()
    suspend fun startAndGetElapsedTime(): Flow<TimerViewData>
}