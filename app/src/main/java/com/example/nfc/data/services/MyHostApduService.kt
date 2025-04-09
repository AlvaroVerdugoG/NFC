package com.example.nfc.data.services

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHostApduService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {
        TODO()
    }

    override fun onDeactivated(reason: Int) {
       //TODO()
    }

}
