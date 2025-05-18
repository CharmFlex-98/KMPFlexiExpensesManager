package com.charmflex.cp.flexiexpensesmanager.core.utils.file

import androidx.compose.runtime.Composable

@Composable
expect fun DocumentPicker(
    show: Boolean,
    onPicked: (ByteArray?) -> Unit
)

