package com.charmflex.cp.flexiexpensesmanager.core.utils.file


interface DocumentManager {
    suspend fun writeCacheFile(data: ByteArray, fileName: String)
    suspend fun getCacheFile(fileName: String): PlatformFile
}

