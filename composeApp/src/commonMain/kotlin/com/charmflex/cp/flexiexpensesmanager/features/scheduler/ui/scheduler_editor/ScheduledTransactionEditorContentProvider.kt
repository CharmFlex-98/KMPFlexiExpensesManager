package com.charmflex.cp.flexiexpensesmanager.features.scheduler.ui.scheduler_editor

import com.charmflex.cp.flexiexpensesmanager.core.domain.FEField
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.TransactionType
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TRANSACTION_SCHEDULER_PERIOD
import com.charmflex.cp.flexiexpensesmanager.features.transactions.provider.TransactionEditorContentProvider
import kotlinproject.composeapp.generated.resources.Res
import kotlinproject.composeapp.generated.resources.*

internal class ScheduledTransactionEditorContentProvider constructor() : TransactionEditorContentProvider() {

    override fun getContent(transactionType: TransactionType): List<FEField> {
        val list = mutableListOf(
            FEField(
                id = TRANSACTION_SCHEDULER_PERIOD,
                labelId = Res.string.transaction_scheduler_period,
                hintId = Res.string.transaction_scheduler_period_hint,
                type = FEField.FieldType.Callback
            )
        )
        val others = super.getContent(transactionType)
        list.addAll(others)
        return list
    }
}