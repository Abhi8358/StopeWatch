package com.vedic.stopewatch.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class DispatcherProviderImpl: DispatcherProvider {
    override val io: CoroutineContext
        get() = Dispatchers.IO

    override val main: CoroutineContext
        get() = Dispatchers.Main
}