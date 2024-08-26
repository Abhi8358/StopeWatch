package com.vedic.stopewatch.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vedic.stopewatch.domain.TimerUseCase

class TimerViewModelFactory(private val timerUseCase: TimerUseCase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimerViewModel(timerUseCase = timerUseCase) as T

    }

}