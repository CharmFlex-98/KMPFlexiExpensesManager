package com.charmflex.cp.flexiexpensesmanager.core.domain
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

internal data class FEField(
    val id: String = "",
    val labelId: StringResource,
    val hintId: StringResource,
    val valueItem: Value = Value(),
    val type: FieldType,
    val allowClear: Boolean = false,
    val supportingText: String? = null
) {
    data class Value(
        val id: String = "",
        val value: String = ""
    )
    val isEnable: Boolean
        get() {
            return type !is FieldType.Selection
        }

    sealed interface FieldType {
        data object Text : FieldType

        data object Number : FieldType
        data object Currency : FieldType

        sealed interface Selection : FieldType
        data object Callback : Selection

        data class SingleItemSelection(
            val options: List<Option>
        ) : Selection {
            interface Option {
                val id: String
                val title: String
                val subtitle: String?
                val icon: DrawableResource?
            }
        }
    }
}

