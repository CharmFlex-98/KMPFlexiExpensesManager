package com.charmflex.cp.flexiexpensesmanager.core.utils.file

import kotlinproject.composeapp.generated.resources.Res
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.readResourceBytes

internal interface AssetReader {
    fun read(path: String): String
}

internal class AssetReaderImpl : AssetReader {
    override fun read(path: String): String {
        return runBlocking {
            Res.readBytes("files/$path").decodeToString()
        }
    }
}