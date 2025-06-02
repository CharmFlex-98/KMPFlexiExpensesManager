package com.charmflex.cp.flexiexpensesmanager.core.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import org.koin.core.annotation.Factory

@Factory
internal class CurrencyVisualTransformationBuilder constructor(
    private val currencyFormatter: CurrencyFormatter,
    private val outputFormatter: CurrencyTextFieldOutputFormatter,
) {

    fun create(currencyCode: String): CurrencyVisualTransformation {
        return CurrencyVisualTransformation(
            currencyFormatter,
            outputFormatter,
            currencyCode
        )
    }
}

internal class CurrencyVisualTransformation(
    private val currencyFormatter: CurrencyFormatter,
    private val outputFormatter: CurrencyTextFieldOutputFormatter,
    private val currencyCode: String,
) : VisualTransformation, OffsetMapping {

    private var originalText = ""
    private var formattedText = ""


    override fun filter(text: AnnotatedString): TransformedText {
        originalText = text.text
        formattedText = getFormattedText(annotatedString = text)
        return TransformedText(AnnotatedString(formattedText), this)
    }

    fun getFormattedText(annotatedString: AnnotatedString): String {
        val text = annotatedString.text
        val trimmedText = outputFormatter.format(text)
        if (trimmedText.isEmpty()) return trimmedText

        return try {
            currencyFormatter.format(trimmedText.toLong(), currencyCode)
        } catch (e: Exception) {
            text
        }
    }

    override fun originalToTransformed(offset: Int): Int {
        return formattedText.length
    }

    override fun transformedToOriginal(offset: Int): Int {
        return originalText.length
    }
}


@Factory
internal class CurrencyTextFieldOutputFormatter  constructor() {
    fun format(value: String): String {
        var trimmedText = value.trim()

        if (trimmedText.isEmpty()) return trimmedText
        val isNegative = trimmedText.count { it == '-' } % 2 != 0
        trimmedText = trimmedText.replace("-", "");
        if (trimmedText.isDigitOnly().not()) {
            trimmedText = trimmedText.split(',', '.', '-', '_').first()
        }

        if (trimmedText.isEmpty()) return trimmedText
        if (isNegative) return "-$trimmedText"
        return trimmedText
    }
}

fun String.isDigitOnly(): Boolean {
    return this.matches(Regex("\\d+"))
}