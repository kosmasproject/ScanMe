package com.kosmasfn.calculatorscan.data.repository

import com.kosmasfn.calculatorscan.data.ResultEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by Kosmas on September 01, 2023.
 */
interface ResultRepository {
    suspend fun saveResults(data: ResultEntity): Flow<Boolean>
    suspend fun getResults(): Flow<Array<ResultEntity>>
}