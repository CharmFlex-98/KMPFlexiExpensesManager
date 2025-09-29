package com.charmflex.cp.flexiexpensesmanager.core.crypto


internal interface Base64Manager {
    fun encode(from: ByteArray): ByteArray
    fun encodeToString(from: ByteArray): String
    fun decode(from: String): ByteArray
    fun decodeToString(from: String): String
}