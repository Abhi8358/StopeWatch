package com.vedic.stopewatch.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vedic.stopewatch.data.model.StopWatchUiState
import com.vedic.stopewatch.domain.TimerUseCase
import com.vedic.stopewatch.util.TimerClickEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.CancellationException

class TimerViewModel(val timerUseCase: TimerUseCase) : ViewModel() {

    private val _currentTime = MutableStateFlow("00:00:00:000")
    val currentTime = _currentTime.asStateFlow()

    private val _currentUiState = MutableStateFlow(StopWatchUiState())
    val currentUiState = _currentUiState.asStateFlow()
    private var currentJob: Job? = null

    fun triggerClickEvents(timerClickEvents: TimerClickEvents) {
        when (timerClickEvents) {
            TimerClickEvents.Start -> {
                startTrigger()
                collectLatestTime()
            }

            TimerClickEvents.Resume -> {
                resumeTrigger()
                collectLatestTime()
            }

            TimerClickEvents.Stop -> {
                stopTrigger()
                viewModelScope.launch {
                    timerUseCase.stop()
                    currentJob?.cancel(CancellationException("Timer is Stop"))
                }
            }

            TimerClickEvents.Pause -> {
                pauseTrigger()
                viewModelScope.launch {
                    timerUseCase.pause()
                    currentJob?.cancel(CancellationException("Timer is Pause"))
                }
            }

            TimerClickEvents.Reset -> {
                resetTrigger()
                viewModelScope.launch {
                    timerUseCase.reset()
                    currentJob?.cancel(CancellationException("Timer is Reset"))
                    _currentTime.value = "0:00:00:000"
                }
            }
        }
    }

    private fun collectLatestTime() {
        currentJob = viewModelScope.launch {
            timerUseCase.startAndGetElapsedTime().collect { timerViewData ->
                Log.d("Abhishek ", "data $timerViewData")
                _currentTime.value =
                    String.format(
                        Locale.ENGLISH,
                        "%02d:%02d:%02d:%03d",
                        timerViewData.hours,
                        timerViewData.minute,
                        timerViewData.second,
                        timerViewData.milliSecond
                    )
                   // "${timerViewData.hours} : ${timerViewData.minute} : ${timerViewData.second} : ${timerViewData.milliSecond}"
            }
        }
    }

    private fun startTrigger() {
        _currentUiState.value = _currentUiState.value.copy(
            isStartEnable = true,
            isStarted = true,
            isPauseEnable = true,
            isPaused = false,
            isResetEnable = true
        )
    }

    private fun stopTrigger() {
        _currentUiState.value = _currentUiState.value.copy(
            isStartEnable = true,
            isPauseEnable = false,
            isStarted = false,
            isPaused = false
        )
    }

    private fun resumeTrigger() {
        _currentUiState.value = _currentUiState.value.copy(
            isPauseEnable = true,
            isStartEnable = true,
            isPaused = false,
            isStarted = true
        )
    }

    private fun pauseTrigger() {
        _currentUiState.value = _currentUiState.value.copy(
            isPauseEnable = true,
            isStartEnable = true,
            isPaused = true,
            isStarted = true
        )
    }

    private fun resetTrigger() {
        _currentUiState.value = _currentUiState.value.copy(
            isResetEnable = true,
            isStartEnable = true,
            isPauseEnable = false,
            isPaused = false,
            isStarted = false
        )
    }
}