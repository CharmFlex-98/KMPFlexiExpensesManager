package com.charmflex.cp.flexiexpensesmanager.core.utils.file

import android.net.Uri

// Abstract representation of a file across platforms
data class AndroidDeviceDocument(
    val uri: Uri
) : ImportedDocument {
    override fun getBytes(): ByteArray {
        TODO("Not yet implemented")
    }

}