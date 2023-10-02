package com.kosmasfn.calculatorscan.view.di

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

/**
 * Created by Kosmas on September 01, 2023.
 */
data class ResultModel(
    val data: MutableList<CalculationData>
) {
    data class CalculationData(
        val input: String = "",
        val result: String = ""
    )

    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun toObject(json: String?): ResultModel? {
            return if (!TextUtils.isEmpty(json)) {
                try {
                    Gson().fromJson(json, ResultModel::class.java)
                } catch (e: JsonSyntaxException) {
                    null
                }
            } else {
                null
            }
        }
    }
}
