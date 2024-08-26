package com.vedic.stopewatch.data

import com.vedic.stopewatch.domain.TimerRepo
import com.vedic.stopewatch.util.DispatcherProvider
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive

class TimerRepoImpl(private val dispatcherProvider: DispatcherProvider) : TimerRepo {

    private var startTime = 0L
    private var elapseTime = 0L

    override suspend fun pause() {
        if (startTime == 0L) return
        elapseTime += System.currentTimeMillis() - startTime
    }

    override suspend fun stop() {
        startTime = 0L
        elapseTime = 0L
    }

    override suspend fun reset() {
        startTime = 0L
        elapseTime = 0L
    }

    override suspend fun startAndGetElapsedTime(): Flow<Long> {
        startTime = System.currentTimeMillis()
        return flow {
            while (currentCoroutineContext().isActive) {
                emit(elapseTime + System.currentTimeMillis() - startTime)
                delay(50)
            }
        }.flowOn(dispatcherProvider.io)
    }

}