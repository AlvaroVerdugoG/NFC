package com.example.nfc.data.services

import android.content.ComponentName
import android.content.pm.PackageManager
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.example.nfc.util.security.AES
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import java.util.Random


class MyHostApduService : HostApduService() {

    private var aes: AES? = null
    private var key: ByteArray? = null
    var rt: ByteArray? = null
    var id: ByteArray? = null

    companion object {

        private const val SELECT_APDU_HEADER = "00A40400"
        private const val AID = "F0010203040506"
        var enable: Boolean = false

        fun buildSelectApdu(aid: String): String {
            // Convierte el string AID a un array de bytes
            val aidLength = aid.length / 2
            val lengthHex = String.format("%02X", aidLength)
            return SELECT_APDU_HEADER + lengthHex + aid
        }

        fun bytesToHex(bytes: ByteArray): String {
            val hexArray = "0123456789ABCDEF".toCharArray()
            val hexChars = CharArray(bytes.size * 2)
            for (i in bytes.indices) {
                val v = bytes[i].toInt() and 0xFF
                hexChars[i * 2] = hexArray[v ushr 4]
                hexChars[i * 2 + 1] = hexArray[v and 0x0F]
            }
            return String(hexChars)
        }

        fun hexStringToByteArray(hex: String): ByteArray {
            val length = hex.length
            val data = ByteArray(length / 2)
            for (i in 0 until length step 2) {
                data[i / 2] =
                    ((Character.digit(hex[i], 16) shl 4) + Character.digit(hex[i + 1], 16)).toByte()
            }
            return data
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun onCreate() {
        aes = AES(this)
        val file = File(this.filesDir, "aeskeyiv")
        key = try {
            val keyReader = BufferedReader(FileReader(file))
            Base64.decode(keyReader.readLine())
        } catch (e: FileNotFoundException) { // Shouldn't happen because we create it in MainActivity.java
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        rt = ByteArray(4)
        id = byteArrayOf(0x01, 0x02, 0x03, 0x04)
    }

    private var messageCounter = 0
    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        val commandStr = bytesToHex(commandApdu)
        Log.d("Comunicacion", "APDU recibido: $commandStr")

        if (enable) {
            if (commandStr.contains(AI--D)) {
                Log.d("Comunicacion", "Comando SELECT AID recibido correctamente.")
                when (messageCounter) {
                    0 -> {
                        messageCounter++
                        return getFirstMessage(commandApdu)
                    }

                    1 -> {
                        messageCounter++
                        return getSecondMessage(commandApdu)
                    }

                    else -> {
                        Log.e("HCEDEMO", "Unexpected message counter value: $messageCounter")
                        "Error".toByteArray()
                    }
                }
               return  hexStringToByteArray("9000")
            } else {
                return hexStringToByteArray("6F00")
            }
        } else {
            return hexStringToByteArray("6F00")

        }


    }

    override fun onDeactivated(reason: Int) {
        enable = false
    }

    private fun getFirstMessage(apdu: ByteArray): ByteArray {
        if (apdu.size < 6 ) {
            return "Apdu is shorter than expected".toByteArray()
        }

        // Decypher Rr generated at server
        val rr = ByteArray(4)
        for (i in 0..3) {
            rr[i] = (apdu[i + 2].toInt() xor key!![i].toInt()).toByte() // key XOR key XOR Rr = Rr
        }

        // Encript Rr + Rt
        val encryptedMessage: ByteArray
        encryptedMessage = try {
            val random = Random()
            random.nextBytes(rt)
            val messageBytes = ByteArray(16)
            System.arraycopy(rr, 0, messageBytes, 0, 4)
            System.arraycopy(rt, 0, messageBytes, 4, 4)
            aes!!.doEncrypt(messageBytes)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        // Return encrypted Rr + Rt
        return encryptedMessage
    }

    private fun getSecondMessage(apdu: ByteArray): ByteArray {
        if (apdu.size < 18) {
            return "Apdu is shorter than expected".toByteArray()
        }

        // Decrypt Rr2 + Rt
        val decryptedMessage: ByteArray?
        decryptedMessage = try {
            val encryptedMessage = ByteArray(16)
            System.arraycopy(apdu, 2, encryptedMessage, 0, 16)
            aes!!.doDecrypt(encryptedMessage)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        if (decryptedMessage == null) {
            return "Error".toByteArray()
        }

        // Check if received Rt equals sent Rt (to avoid replay attacks)
        val rt = ByteArray(4)
        System.arraycopy(decryptedMessage, 4, rt, 0, 4)
        if (!rt.contentEquals(this.rt)) {
            return "Error".toByteArray()
        }

        // Get Rr2 sent from the server
        val rr2 = ByteArray(4)
        System.arraycopy(decryptedMessage, 0, rr2, 0, 4)
        val rt2 = ByteArray(4)
        val random = Random()
        random.nextBytes(rt2)

        // Encrypt Rr2 + Rt2 + id
        val messageToEncript = ByteArray(16)
        return try {
            System.arraycopy(rr2, 0, messageToEncript, 0, 4)
            System.arraycopy(rt2, 0, messageToEncript, 4, 4)
            System.arraycopy(id, 0, messageToEncript, 8, 4)
            aes!!.doEncrypt(messageToEncript)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

}