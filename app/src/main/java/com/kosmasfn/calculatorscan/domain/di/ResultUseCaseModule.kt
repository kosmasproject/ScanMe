package com.kosmasfn.calculatorscan.domain.di

import com.kosmasfn.calculatorscan.domain.usecase.ResultUseCase
import com.kosmasfn.calculatorscan.domain.usecase.ResultUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by Kosmas on September 01, 2023.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class ResultUseCaseModule {

    @Binds
    abstract fun bindResultUseCase(useCase: ResultUseCaseImpl): ResultUseCase
}