package com.example.nfc.data.services

import android.nfc.cardemulation.HostApduService
import android.os.Bundle


class MyHostApduService : HostApduService() {
    companion object {
        var isEnabled = false
    }

    private val SELECT_APDU: ByteArray = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)


    private val ACCESS_GRANTED_RESPONSE: ByteArray = byteArrayOf(
        0xD3.toByte(), 0xAC.toByte(), 0x0F, 0x2F,
        0x90.toByte(), 0x00
    )

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray? {
        if (commandApdu == null) return null
        if (!isEnabled) {
            return byteArrayOf(0x69.toByte(), 0x85.toByte())
        }
        if (commandApdu.contentEquals(SELECT_APDU)) {
            return ACCESS_GRANTED_RESPONSE
        }
        return byteArrayOf(0x6F.toByte(), 0x00)
    }

    override fun onDeactivated(reason: Int) {
        isEnabled = false
    }
}