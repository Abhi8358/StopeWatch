package com.vedic.stopewatch.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.vedic.stopewatch.ui.theme.StopeWatchTheme
import com.vedic.stopewatch.ui.viewModel.TimerViewModel
import com.vedic.stopewatch.util.TimerClickEvents
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val timerViewModel: TimerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StopeWatchTheme {
                val time by timerViewModel.currentTime.collectAsState()
                val uiState by timerViewModel.currentUiState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Log.d("Abhishek", "recomposition")
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = time,
                            fontSize = TextUnit(36F, TextUnitType.Sp),
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )

                        Row(modifier = Modifier.padding(8.dp)) {
                            Button(modifier = Modifier.padding(8.dp),
                                enabled = uiState.isStartEnable,
                                onClick = {
                                    timerViewModel.triggerClickEvents(if (uiState.isStarted) TimerClickEvents.Stop else TimerClickEvents.Start)
                                }) {

                                Text(text = if (uiState.isStarted) "Stop" else "Start")
                            }

                            Button(modifier = Modifier.padding(8.dp),
                                enabled = timerViewModel.currentUiState.collectAsState().value.isPauseEnable,
                                onClick = {
                                    if (timerViewModel.currentUiState.value.isPaused) {
                                        timerViewModel.triggerClickEvents(TimerClickEvents.Resume)
                                    } else {
                                        timerViewModel.triggerClickEvents(TimerClickEvents.Pause)
                                    }
                                }) {
                                Text(text = if (timerViewModel.currentUiState.collectAsState().value.isPaused) "Resume" else "Pause")
                            }
                        }

                        Button(modifier = Modifier.padding(8.dp),
                            enabled = timerViewModel.currentUiState.collectAsState().value.isResetEnable,
                            onClick = {
                                timerViewModel.triggerClickEvents(TimerClickEvents.Reset)
                            }) {
                            Text(text = "Reset")
                        }
                    }
                }
            }
        }
    }
}