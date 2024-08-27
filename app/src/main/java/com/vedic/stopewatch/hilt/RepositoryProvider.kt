package com.vedic.stopewatch.hilt

import com.vedic.stopewatch.data.TimerRepoImpl
import com.vedic.stopewatch.domain.TimerRepo
import com.vedic.stopewatch.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryProvider {

    @Provides
    @Singleton
    fun getTimerRepo(dispatcherProvider: DispatcherProvider): TimerRepo = TimerRepoImpl(dispatcherProvider)
}