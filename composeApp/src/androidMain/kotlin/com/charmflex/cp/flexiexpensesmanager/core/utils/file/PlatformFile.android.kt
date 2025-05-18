package com.charmflex.cp.flexiexpensesmanager.core.utils.file

import android.net.Uri
import java.io.File

actual data class PlatformFile(
    val file: File,
    val uri: Uri
)