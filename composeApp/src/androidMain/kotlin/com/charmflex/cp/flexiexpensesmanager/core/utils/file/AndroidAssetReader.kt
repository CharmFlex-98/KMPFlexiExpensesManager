package com.charmflex.cp.flexiexpensesmanager.core.utils.file
import android.content.Context

class AndroidAssetReader(
    private val appContext: Context,
) : AssetReader {
    override fun read(path: String): String {
        return appContext.assets.open("currency_metadata.json").bufferedReader()
            .use { it.readText() }
    }
}