package com.kosmasfn.calculatorscan.data.di

import android.content.Context
import androidx.room.Room
import com.kosmasfn.calculatorscan.data.CalculatorScanDatabase
import com.kosmasfn.calculatorscan.data.ResultDao
import com.kosmasfn.calculatorscan.data.repository.ResultRepository
import com.kosmasfn.calculatorscan.data.repository.ResultRepositoryImpl
import com.kosmasfn.calculatorscan.utils.Constant.CALCULATOR_DATABASE
import com.kosmasfn.calculatorscan.utils.Constant.CALCULATOR_RESULT_DAO
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

/**
 * Created by Kosmas on September 01, 2023.
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Reusable
    @Provides
    @Named(CALCULATOR_DATABASE)
    fun provideCalculatorScanDatabase(@ApplicationContext context: Context): CalculatorScanDatabase {
        try {
            return Room.databaseBuilder(context, CalculatorScanDatabase::class.java, "calculator_db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        } catch (e: Exception) {
            throw e
        }
    }

    @Reusable
    @Provides
    @Named(CALCULATOR_RESULT_DAO)
    fun provideResultDao(@Named(CALCULATOR_DATABASE) database: CalculatorScanDatabase): ResultDao {
        return database.resultDao()
    }

    @Provides
    @Reusable
    fun provideResultRepository(@Named(CALCULATOR_RESULT_DAO) resultDao: ResultDao): ResultRepository =
        ResultRepositoryImpl(resultDao)
}