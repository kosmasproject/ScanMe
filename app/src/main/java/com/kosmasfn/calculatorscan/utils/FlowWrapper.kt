package com.kosmasfn.calculatorscan.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by Kosmas on September 01, 2023.
 */
class FlowWrapper<T : Any>(initialValue: T?) {
    private val stateFlow = MutableStateFlow(initialValue)

    fun postValue(value: T) {
        stateFlow.value = value
    }

    fun getValue(): T? = stateFlow.value

    fun getFlow() = stateFlow as StateFlow<T?>
}