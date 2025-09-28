package com.charmflex.cp.flexiexpensesmanager.core.crypto


internal interface Base64Manager {
    fun encode(from: ByteArray): ByteArray
    fun encodeToString(from: ByteArray): String
    fun decode(from: ByteArray): ByteArray
    fun decodeToString(from: ByteArray): String
}