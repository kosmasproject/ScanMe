package com.kosmasfn.calculatorscan.utils

import android.util.Base64
import android.util.Log
import java.io.UnsupportedEncodingException
import java.security.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Kosmas on September 01, 2023.
 */
object CipherUtils {
    private const val TAG = "AESCrypt"
    private const val AES_MODE = "AES/CBC/PKCS7Padding"
    private val CHARSET = "UTF-8"
    private const val HASH_ALGORITHM = "SHA-256"
    private val ivBytes = byteArrayOf(
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00,
        0x00, 0x00, 0x00, 0x00
    )
    var DEBUG_LOG_ENABLED = false

    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class)
    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val bytes = password.toByteArray(charset(CHARSET))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        log("SHA-256 key ", key)
        return SecretKeySpec(key, "AES")
    }

    @Throws(GeneralSecurityException::class)
    fun encrypt(password: String, message: String): String {
        return try {
            val key = generateKey(password)
            val cipherText = encrypt(key, ivBytes, message.toByteArray(charset(CHARSET)))
            val encoded = Base64.encodeToString(cipherText, Base64.NO_WRAP)
            log("Base64.NO_WRAP", encoded)
            encoded
        } catch (e: UnsupportedEncodingException) {
            if (DEBUG_LOG_ENABLED) Log.e(TAG, "UnsupportedEncodingException ", e)
            throw GeneralSecurityException(e)
        }
    }

    @Throws(GeneralSecurityException::class)
    fun encrypt(key: SecretKeySpec?, iv: ByteArray?, message: ByteArray?): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val cipherText = cipher.doFinal(message)
        log("cipherText", cipherText)
        return cipherText
    }

    @Throws(GeneralSecurityException::class)
    fun decrypt(password: String, base64EncodedCipherText: String): String {
        return try {
            val key = generateKey(password)
            val decodedCipherText = Base64.decode(base64EncodedCipherText, Base64.NO_WRAP)
            val decryptedBytes = decrypt(key, ByteArray(16), decodedCipherText)
            val message = String(decryptedBytes, charset(CHARSET))
            message
        } catch (e: UnsupportedEncodingException) {
            if (DEBUG_LOG_ENABLED) Log.e(TAG, "UnsupportedEncodingException ", e)
            throw GeneralSecurityException(e)
        }
    }

    @Throws(GeneralSecurityException::class)
    fun decrypt(key: SecretKeySpec?, iv: ByteArray?, decodedCipherText: ByteArray?): ByteArray {
        val cipher = Cipher.getInstance(AES_MODE)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        val decryptedBytes = cipher.doFinal(decodedCipherText)
        log("decryptedBytes", decryptedBytes)
        return decryptedBytes
    }

    private fun log(what: String, bytes: ByteArray) {
        if (DEBUG_LOG_ENABLED) Log.d(TAG, what + "[" + bytes.size + "] [" + bytesToHex(bytes) + "]")
    }

    private fun log(what: String, value: String) {
        if (DEBUG_LOG_ENABLED) Log.d(TAG, what + "[" + value.length + "] [" + value + "]")
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexArray = charArrayOf(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        )
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}