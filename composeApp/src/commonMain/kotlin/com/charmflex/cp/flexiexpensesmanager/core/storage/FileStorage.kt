package com.charmflex.cp.flexiexpensesmanager.core.storage

internal interface FileStorage {

    suspend fun write(fileName: String, byteArray: ByteArray)

    suspend fun read(fileName: String): String

}