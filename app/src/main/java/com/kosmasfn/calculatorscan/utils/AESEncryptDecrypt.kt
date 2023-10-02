package com.kosmasfn.calculatorscan.utils

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Kosmas on September 01, 2023.
 */
object AESEncryptDecrypt {

    @Throws(
        IOException::class, NoSuchAlgorithmException::class,
        NoSuchPaddingException::class, InvalidKeyException::class
    )
    fun encrypt(file: File) {
        val extStore: File = Environment.getExternalStorageDirectory()
        val fis = FileInputStream(file)
        val fos = FileOutputStream(file)
        val sks = SecretKeySpec(
            "Calcalator^%&$#$%%^*(^&**HGFDSDFSCcxcxzdzd".toByteArray(), "AES"
        )

        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, sks)
        val cos = CipherOutputStream(fos, cipher)
        var b: Int
        val d = ByteArray(8)
        while (fis.read(d).also { b = it } != -1) {
            cos.write(d, 0, b)
        }
        cos.flush()
        cos.close()
        fis.close()
    }

    @Throws(
        IOException::class, NoSuchAlgorithmException::class,
        NoSuchPaddingException::class, InvalidKeyException::class
    )
    fun decrypt(file: File) {
        val extStore: File = Environment.getExternalStorageDirectory()
        val fis = FileInputStream("$extStore/encrypted")
        val fos = FileOutputStream("$extStore/decrypted")
        val sks = SecretKeySpec(
            "Calcalator^%&$#$%%^*(^&**HGFDSDFSCcxcxzdzd".toByteArray(), "AES"
        )

        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, sks)
        val cis = CipherInputStream(fis, cipher)
        var b: Int
        val d = ByteArray(8)
        while (cis.read(d).also { b = it } != -1) {
            fos.write(d, 0, b)
        }
        fos.flush()
        fos.close()
        cis.close()
    }
}