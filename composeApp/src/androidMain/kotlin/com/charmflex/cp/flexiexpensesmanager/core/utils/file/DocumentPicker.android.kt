package com.charmflex.cp.flexiexpensesmanager.core.utils.file

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun DocumentPicker(
    show: Boolean,
    onPicked: (ByteArray?) -> Unit
) {
    val localContext = LocalContext.current
    val excelPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { excelUri ->
        excelUri?.let { it ->
            localContext.contentResolver.openInputStream(it).use { data ->
                onPicked(data?.readBytes())
            }
        }
    }

    LaunchedEffect(show) {
        if (show) {
            excelPickerLauncher.launch(
                arrayOf(
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
            )
        }
    }
}