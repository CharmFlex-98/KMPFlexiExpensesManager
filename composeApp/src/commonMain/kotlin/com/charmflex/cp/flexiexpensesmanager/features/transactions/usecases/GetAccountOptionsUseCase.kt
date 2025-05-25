package com.charmflex.cp.flexiexpensesmanager.features.transactions.usecases

import AccountRepository

internal class GetAccountOptionsUseCase constructor(
    private val accountRepository: AccountRepository,
) {

//    suspend operator fun invoke(): List<AccountSelectionItem> {
//        val res = mutableListOf<AccountSelectionItem>()
//        accountRepository.getAllAccounts().forEach {
//            res.addAll(
//                it.accounts.map {
//                    AccountSelectionItem(
//                        id = it.accountId.toString(),
//                        title = it.accountName
//                    )
//                }
//            )
//        }
//
//        return res
//    }
}