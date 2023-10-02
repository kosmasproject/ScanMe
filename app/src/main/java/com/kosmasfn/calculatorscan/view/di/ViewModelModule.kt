package com.kosmasfn.calculatorscan.view.di

import androidx.lifecycle.ViewModel
import com.kosmasfn.calculatorscan.view.ResultViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Kosmas on September 01, 2023.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindResultViewModel(viewModel: ResultViewModel): ViewModel
}