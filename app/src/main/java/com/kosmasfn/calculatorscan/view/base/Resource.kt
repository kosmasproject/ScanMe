package com.kosmasfn.calculatorscan.view.base

/**
 * Created by Kosmas on September 01, 2023.
 */
sealed class Resource<out T>(val data: T? = null) {
    class Success<out T>(data: T) : Resource<T>(data)
    class StartLoading<out T> : Resource<T>()
    class EndLoading<out T> : Resource<T>()
    class Error<out T>(val message: String) : Resource<T>()
}