package com.charmflex.cp.flexiexpensesmanager.core.utils.file

internal interface AssetReader {
    fun read(path: String): String
}