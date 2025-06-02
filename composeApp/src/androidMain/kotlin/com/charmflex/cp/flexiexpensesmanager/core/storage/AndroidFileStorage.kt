package com.charmflex.cp.flexiexpensesmanager.core.storage

import android.content.Context
import com.charmflex.cp.flexiexpensesmanager.core.di.DispatcherType
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.Koin
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatformTools
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


internal class AndroidFileStorage (
    private val appContext: Context,
) : FileStorage {
    private val dispatcher: CoroutineDispatcher = KoinPlatformTools.defaultContext().get().get(named(DispatcherType.IO))
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