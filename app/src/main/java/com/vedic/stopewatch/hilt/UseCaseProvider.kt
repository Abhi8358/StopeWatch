package com.vedic.stopewatch.hilt

import com.vedic.stopewatch.data.TimerRepoImpl
import com.vedic.stopewatch.domain.TimerRepo
import com.vedic.stopewatch.domain.TimerUseCase
import com.vedic.stopewatch.domain.TimerUseCaseImpl
import com.vedic.stopewatch.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseProvider {


    @Provides
    @Singleton
    fun getTimerUseCase(repoImpl: TimerRepoImpl, dispatcherProvider: DispatcherProvider): TimerUseCase = TimerUseCaseImpl(repoImpl, dispatcherProvider)
}