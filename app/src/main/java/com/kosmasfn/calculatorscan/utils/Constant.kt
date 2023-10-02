package com.kosmasfn.calculatorscan.utils

import android.os.Build
import com.kosmasfn.calculatorscan.BuildConfig

/**
 * Created by Kosmas on September 01, 2023.
 */
object Constant {
    const val CALCULATOR_DATABASE = "CALCULATOR_DATABASE"
    const val CALCULATOR_RESULT_DAO = "CALCULATOR_RESULT_DAO"
    const val ALL_PERMISSIONS = 10
    const val PLUS_SIGN = "+"
    const val MINUS_SIGN = "-"
    const val TIMES_SIGN = "*"
    const val DIVISION_SIGN = "/"
    val REQUIRED_PERMISSIONS = when {
        BuildConfig.FLAVOR.contains("camera") -> mutableListOf(android.Manifest.permission.CAMERA).toTypedArray()
        else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mutableListOf(android.Manifest.permission.READ_MEDIA_IMAGES).toTypedArray()
            } else {
                mutableListOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).toTypedArray()
            }
        }
    }
}