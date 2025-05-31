package com.example.nfc.data.services

import android.content.ComponentName
import android.content.pm.PackageManager
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHostApduService : HostApduService() {

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

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        val commandStr = bytesToHex(commandApdu)
        Log.d("Comunicacion", "APDU recibido: $commandStr")

        return if (enable) {
            if (commandStr.contains(AID)) {
                Log.d("Comunicacion", "Comando SELECT AID recibido correctamente.")
                hexStringToByteArray("9000")
            } else {
                hexStringToByteArray("6F00")
            }
        } else {
            hexStringToByteArray("6F00")

        }
    }

    override fun onDeactivated(reason: Int) {
        enable = false
    }


}