package com.vedic.stopewatch.data.model

data class StopWatchUiState(
    val isStarted: Boolean = false,
    val isStartEnable: Boolean = true,
    val isPaused: Boolean = false,
    val isPauseEnable: Boolean = true,
    val isResetEnable: Boolean = false
)
