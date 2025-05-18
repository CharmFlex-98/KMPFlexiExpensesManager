package com.charmflex.cp.flexiexpensesmanager.core.utils.file


import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File


class AndroidDocumentManager(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : DocumentManager {
    override suspend fun writeCacheFile(data: ByteArray, fileName: String) {
        withContext(dispatcher) {
            val file = File(context.cacheDir, fileName).apply { createNewFile() }
            file.writeBytes(data)
        }
    }

    override suspend fun getCacheFile(fileName: String): PlatformFile {
        return withContext(dispatcher) {
            val file = File(context.cacheDir, fileName)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            PlatformFile(file, uri)
        }
    }
}
