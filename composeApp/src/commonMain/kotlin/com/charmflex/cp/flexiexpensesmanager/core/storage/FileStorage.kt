package com.charmflex.cp.flexiexpensesmanager.core.storage

import com.charmflex.cp.flexiexpensesmanager.core.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface FileStorage {

    suspend fun write(fileName: String, byteArray: ByteArray)

    suspend fun read(fileName: String): String

}

internal class FileStorageImpl (
    private val appContext: Context,
    @Dispatcher(dispatcherType = Dispatcher.Type.IO)
    private val dispatcher: CoroutineDispatcher
) : FileStorage {
    override suspend fun write(fileName: String, byteArray: ByteArray) = withContext(dispatcher) {
        val filesDir = appContext.filesDir
        val file = File(filesDir, fileName).apply { createNewFile() }
        FileOutputStream(file).use {
            it.write(byteArray)
        }
    }

    override suspend fun read(fileName: String): String = withContext(dispatcher) {
        val filesDir = appContext.filesDir
        val file = File(filesDir, fileName)
        if (!file.exists()) ""
        else {
            FileInputStream(file).bufferedReader().use {
                it.readText()
            }
        }
    }
}