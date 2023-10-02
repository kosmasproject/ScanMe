package com.kosmasfn.calculatorscan.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by Kosmas on September 01, 2023.
 */
@Database(entities = [ResultEntity::class], version = 1,
    exportSchema = false)
abstract class CalculatorScanDatabase : RoomDatabase() {
    abstract fun resultDao(): ResultDao
}