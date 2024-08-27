package com.vedic.stopewatch.domain

import com.vedic.stopewatch.data.model.TimerViewData
import com.vedic.stopewatch.util.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimerUseCaseImpl @Inject constructor(private val repo: TimerRepo, private val dispatcherProvider: DispatcherProvider) : TimerUseCase {

    override suspend fun pause() {
        repo.pause()
    }

    override suspend fun stop() {
        repo.stop()
    }

    override suspend fun startAndGetElapsedTime(): Flow<TimerViewData> {
        return repo.startAndGetElapsedTime().map { elapseTime ->
            val milliSeconds = elapseTime % 1000
            val seconds = (elapseTime / 1000) % 60
            val minutes = (elapseTime / (1000 * 60)) % 60
            val hours = (elapseTime / (1000 * 60 * 60))
            TimerViewData(hours = hours, milliSecond = milliSeconds, minute = minutes, second = seconds)
        }.flowOn(dispatcherProvider.io)
    }

    override suspend fun reset() {
        repo.reset()
    }
}