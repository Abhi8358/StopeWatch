package com.vedic.stopewatch.data.model

data class StopWatchUiState(
    val isStarted: Boolean = false,
    val isStartEnable: Boolean = true,
    val isPaused: Boolean = true,
    val isPauseEnable: Boolean = false,
    val isResetEnable: Boolean = false
)
