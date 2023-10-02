package com.kosmasfn.calculatorscan.data

/**
 * Created by Kosmas on September 01, 2023.
 */
interface ModelEntity<T> {
    fun mapToEntity(): T
}

fun <T> Array<ModelEntity<T>>.mapToEntityList(): MutableList<T> {
    val list = mutableListOf<T>()
    this.forEach {
        list.add(it.mapToEntity())
    }
    return list.toMutableList()
}