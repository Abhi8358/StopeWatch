package com.vedic.stopewatch.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vedic.stopewatch.domain.TimerUseCase
import com.vedic.stopewatch.util.TimerClickEvents
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.CancellationException

class TimerViewModel(val timerUseCase: TimerUseCase) : ViewModel() {

    private val _currentTime = MutableStateFlow("Start Timer")
    val currentTime = _currentTime.asStateFlow()
    private var currentJob: Job? = null

    fun triggerClickEvents(timerClickEvents: TimerClickEvents) {
        when (timerClickEvents) {
            TimerClickEvents.Start -> {
                collectLatestTime()
            }

            TimerClickEvents.Resume -> {
                collectLatestTime()
            }

            TimerClickEvents.Stop -> {
                viewModelScope.launch {
                    timerUseCase.stop()
                    currentJob?.cancel(CancellationException("Timer is Stop"))
                }
            }

            TimerClickEvents.Pause -> {
                viewModelScope.launch {
                    timerUseCase.pause()
                    currentJob?.cancel(CancellationException("Timer is Pause"))
                }
            }

            TimerClickEvents.Reset -> {
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
}