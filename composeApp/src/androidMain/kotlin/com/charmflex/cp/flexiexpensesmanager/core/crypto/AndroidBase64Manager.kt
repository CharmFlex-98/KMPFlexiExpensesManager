package com.charmflex.cp.flexiexpensesmanager.core.crypto

import android.util.Base64
import org.koin.core.annotation.Singleton

@Singleton
class AndroidBase64Manager : Base64Manager {
    override fun encode(from: ByteArray): ByteArray {
        return Base64.encode(from, Base64.NO_WRAP)
    }

    override fun encodeToString(from: ByteArray): String {
        return encode(from).toString()
    }

    override fun decode(from: String): ByteArray {
        return Base64.decode(from, Base64.NO_WRAP)
    }

    override fun decodeToString(from: String): String {
        return decode(from).toString()
    }
}