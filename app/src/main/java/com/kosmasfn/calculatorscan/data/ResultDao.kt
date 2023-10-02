package com.kosmasfn.calculatorscan.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Kosmas on September 01, 2023.
 */
@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveResult(resultEntity: ResultEntity)

    @Insert
    fun saveAll(users: Array<ResultEntity>)

    @get:Query("SELECT * FROM ResultEntity")
    val result: Array<ResultEntity>

    @Query("DELETE FROM ResultEntity")
    fun deleteAll()
}