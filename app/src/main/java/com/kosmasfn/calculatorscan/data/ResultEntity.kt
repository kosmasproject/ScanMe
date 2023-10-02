package com.kosmasfn.calculatorscan.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kosmasfn.calculatorscan.view.di.ResultModel.CalculationData

/**
 * Created by Kosmas on September 01, 2023.
 */
@Entity
data class ResultEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "input")
    val input: String?,

    @ColumnInfo(name = "result")
    val result: String?

) : ModelEntity<CalculationData> {

    override fun mapToEntity(): CalculationData {
        return CalculationData(
            input = this.input ?: "",
            result = this.result ?: ""
        )
    }
}