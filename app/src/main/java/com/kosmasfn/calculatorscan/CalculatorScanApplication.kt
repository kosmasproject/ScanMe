package com.kosmasfn.calculatorscan

import android.app.Application
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import dagger.hilt.android.HiltAndroidApp
import java.io.File

/**
 * Created by Kosmas on September 01, 2023.
 */
@HiltAndroidApp
class CalculatorScanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initEncryptedFile()
    }

    private fun initEncryptedFile() {
        EncryptedFile.Builder(
            this,
            File(filesDir, "secret_data"),
            MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }
}