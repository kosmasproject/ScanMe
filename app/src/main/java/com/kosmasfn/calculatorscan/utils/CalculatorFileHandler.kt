package com.kosmasfn.calculatorscan.utils

import android.content.Context
import android.util.Log
import com.kosmasfn.calculatorscan.view.di.ResultModel
import java.io.*

/**
 * Created by Kosmas on September 01, 2023.
 */
fun getResultFromFile(
    context: Context,
    file: File = File(
        context.filesDir.parentFile,
        "/shared_prefs/__androidx_security_crypto_encrypted_file_pref__"
    )
): ResultModel? {
    return if (file.exists()) {
        try {
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            ResultModel.toObject(stringBuilder.toString())
        } catch (e: IOException) {
            null
        }
    } else {
        null
    }
}

fun writeToFile(
    context: Context,
    data: String,
    file: File = File(
        context.filesDir.parentFile,
        "/shared_prefs/__androidx_security_crypto_encrypted_file_pref__"
    )
) {
    try {

        val fileWriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(fileWriter)
        bufferedWriter.write(data)
        bufferedWriter.close()

    } catch (ex: FileNotFoundException) {
        Log.e("write", ex.message.toString())
    } catch (ex: java.lang.Exception) {
        Log.e("write", ex.message.toString())
    }
}

fun getResultFromFileStream(
    context: Context,
    file: File = File("calculation_history.txt")
): ResultModel? {
    return if (file.exists()) {
        try {
            val fileInputStream = FileInputStream(file)
            val bufferedReader = fileInputStream.bufferedReader()
            val stringBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            ResultModel.toObject(stringBuilder.toString())

        } catch (e: IOException) {
            null
        }
    } else {
        null

    }
}

fun writeFileStream(
    context: Context,
    data: String,
    file: File = File("calculation_history.txt")
) {
    if (file.exists()) {
        FileOutputStream(file).use { output ->
            output.write(data.toByteArray())
        }
    } else {
        context.openFileOutput("calculation_history.txt", Context.MODE_PRIVATE).use { output ->
            output.write(data.toByteArray())
        }
    }

}

