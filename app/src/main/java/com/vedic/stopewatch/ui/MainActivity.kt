package com.vedic.stopewatch.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.vedic.stopewatch.data.TimerRepoImpl
import com.vedic.stopewatch.domain.TimerUseCaseImpl
import com.vedic.stopewatch.ui.theme.StopeWatchTheme
import com.vedic.stopewatch.ui.viewModel.TimerViewModel
import com.vedic.stopewatch.ui.viewModel.TimerViewModelFactory
import com.vedic.stopewatch.util.TimerClickEvents

class MainActivity : ComponentActivity() {
    private lateinit var timerViewModel: TimerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val timerViewModelFactory = TimerViewModelFactory(TimerUseCaseImpl(TimerRepoImpl()))
        timerViewModel = ViewModelProvider(this, timerViewModelFactory)[TimerViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            StopeWatchTheme {
                var isStarted = remember {
                    mutableStateOf(false)
                }

                var isPaused = remember {
                    mutableStateOf(true)
                }

                val isPauseEnable = remember {
                    mutableStateOf(false)
                }

                val isStartEnable = remember {
                    mutableStateOf(true)
                }

                val isResetEnable = remember {
                    mutableStateOf(false)
                }

                val time by timerViewModel.currentTime.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Log.d("Abhishek", "recomposition")
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
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
                                enabled = isStartEnable.value,
                                onClick = {
                                    if (isStarted.value) {
                                        isStarted.value = false
                                        isPaused.value = true
                                        isPauseEnable.value = false
                                        timerViewModel.triggerClickEvents(TimerClickEvents.Stop)
                                    } else {
                                        isResetEnable.value = true
                                        isPauseEnable.value = true
                                        isStarted.value = true
                                        isPaused.value = false
                                        timerViewModel.triggerClickEvents(TimerClickEvents.Start)
                                    }
                                }) {

                                Text(text = if (isStarted.value) "Stop" else "Start")
                            }

                            Button(modifier = Modifier.padding(8.dp),
                                enabled = isPauseEnable.value,
                                onClick = {
                                    if (isPaused.value) {
                                        isPaused.value = false
                                        isStarted.value = true
                                        isStartEnable.value = true
                                        timerViewModel.triggerClickEvents(TimerClickEvents.Resume)
                                    } else {
                                        isPaused.value = true
                                        isStarted.value = false
                                        isStartEnable.value = false
                                        timerViewModel.triggerClickEvents(TimerClickEvents.Pause)
                                    }
                                }) {
                                Text(text = if (isPaused.value) "Resume" else "Pause")
                            }
                        }

                        Button(modifier = Modifier.padding(8.dp),
                            enabled = isResetEnable.value,
                            onClick = {
                                timerViewModel.triggerClickEvents(TimerClickEvents.Reset)
                                isPaused.value = false
                                isStarted.value = false
                                isPauseEnable.value = false
                                isStartEnable.value = true
                            }) {
                            Text(text = "Reset")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StopeWatchTheme {
        Greeting("Android")
    }
}