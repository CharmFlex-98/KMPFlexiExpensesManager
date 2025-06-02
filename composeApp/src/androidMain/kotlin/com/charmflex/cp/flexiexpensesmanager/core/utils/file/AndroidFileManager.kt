package com.charmflex.cp.flexiexpensesmanager.core.utils.file


import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.charmflex.cp.flexiexpensesmanager.core.di.DispatcherType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.qualifier.named
import org.koin.mp.KoinPlatformTools
import java.io.File


class AndroidDocumentManager(
    private val context: Context,
) : DocumentManager {
    private val dispatcher = KoinPlatformTools.defaultContext().get().get<CoroutineDispatcher>(named(DispatcherType.IO))
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
