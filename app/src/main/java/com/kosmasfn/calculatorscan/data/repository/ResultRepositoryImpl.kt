package com.kosmasfn.calculatorscan.data.repository

import com.kosmasfn.calculatorscan.data.ResultDao
import com.kosmasfn.calculatorscan.data.ResultEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Kosmas on September 01, 2023.
 */
class ResultRepositoryImpl @Inject constructor(
    private val resultDao: ResultDao
) : ResultRepository {

    override suspend fun saveResults(data: ResultEntity): Flow<Boolean> {
        resultDao.saveResult(data)
        return flow { emit(true) }
    }

    override suspend fun getResults(): Flow<Array<ResultEntity>> =
        flow {
            emit(
                resultDao.result
            )
        }.catch {
            throw it
        }
}