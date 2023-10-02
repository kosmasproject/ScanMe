package com.kosmasfn.calculatorscan.domain.usecase

import com.kosmasfn.calculatorscan.data.ResultEntity
import com.kosmasfn.calculatorscan.data.repository.ResultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Kosmas on September 01, 2023.
 */
class ResultUseCaseImpl @Inject constructor(
    private val repository: ResultRepository
) : ResultUseCase {

    override suspend fun saveResults(data: ResultEntity): Flow<Boolean> {
        return repository.saveResults(data)
    }

    override suspend fun getResults(): Flow<Array<ResultEntity>> {
        return repository.getResults()
    }

}