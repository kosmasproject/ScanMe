package com.kosmasfn.calculatorscan.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kosmasfn.calculatorscan.data.ResultEntity
import com.kosmasfn.calculatorscan.domain.usecase.ResultUseCase
import com.kosmasfn.calculatorscan.utils.*
import com.kosmasfn.calculatorscan.view.base.Resource
import com.kosmasfn.calculatorscan.view.di.ResultModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kosmas on September 01, 2023.
 */
@HiltViewModel
class ResultViewModel @Inject constructor(private val useCase: ResultUseCase) : ViewModel() {

    private val resultStateFlow = FlowWrapper<Resource<ResultModel>>(null)
    private val saveResultStateFlow = FlowWrapper<Resource<Boolean>>(null)

    fun saveResult(data: ResultEntity) {
        viewModelScope.launch {
            saveResultStateFlow.postValue(Resource.StartLoading())
            useCase.saveResults(data)
                .catch { e ->
                    saveResultStateFlow.postValue(Resource.EndLoading())
                    saveResultStateFlow.postValue(Resource.Error(e.message.orEmpty()))
                }.collect {
                    saveResultStateFlow.postValue(Resource.EndLoading())
                    saveResultStateFlow.postValue(Resource.Success(it))
                }
        }
    }

    fun fetchResults() {
        viewModelScope.launch {
            resultStateFlow.postValue(Resource.StartLoading())
            useCase.getResults()
                .catch { e ->
                    resultStateFlow.postValue(Resource.EndLoading())
                    resultStateFlow.postValue(Resource.Error(e.toString()))
                }.collect {
                    resultStateFlow.postValue(Resource.EndLoading())
                    resultStateFlow.postValue(Resource.Success(it.mapToDomainModel()))
                }
        }
    }

    private fun Array<ResultEntity>.mapToDomainModel(): ResultModel {

        val data: MutableList<ResultModel.CalculationData> = mutableListOf()
        this.forEach { data.add(it.mapToEntity()) }

        return ResultModel(
            data = data
        )
    }

    fun saveResultToFile(context: Context, data: ResultModel) {
        return writeToFile(context, data.toJson())
    }

    fun fetchResultFromFile(context: Context): ResultModel? {
        return getResultFromFile(context)
    }

    fun getResultStateFlow() = resultStateFlow.getFlow()
    fun getSaveResultStateFlow() = saveResultStateFlow.getFlow()

}