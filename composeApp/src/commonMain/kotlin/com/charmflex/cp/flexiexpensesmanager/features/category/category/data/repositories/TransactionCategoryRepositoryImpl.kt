package com.charmflex.cp.flexiexpensesmanager.features.category.category.data.repositories

import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.daos.TransactionCategoryDao
import com.charmflex.cp.flexiexpensesmanager.features.category.category.data.entities.TransactionCategoryEntity
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.CategoryNode
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.buildCategoryTree
import com.charmflex.cp.flexiexpensesmanager.features.transactions.domain.model.Transaction
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.models.TransactionCategories
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.models.TransactionCategory
import com.charmflex.cp.flexiexpensesmanager.features.category.category.domain.repositories.TransactionCategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.transformLatest

internal class TransactionCategoryRepositoryImpl constructor(
    private val transactionCategoryDao: TransactionCategoryDao
) : TransactionCategoryRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllCategoryTransactionAmount(
        startDate: String?,
        endDate: String?,
        transactionTypeCode: String
    ): Flow<List<CategoryAmountNode>> {
        return transactionCategoryDao.getAllCategoryTransactionAmount(startDate, endDate, transactionTypeCode)
            .transformLatest { list ->
                val map = list.groupBy { it.parentCategoryId }
                val items = list.filter { it.parentCategoryId == 0 }
                    .map {
                        buildCategoryTree(
                            retrievalKey = {
                                it.categoryId
                            },
                            responseEntity = it,
                            parentCatIDChildrenMap = map
                        ) { level, it ->
                            CategoryAmountNode(
                                categoryId = it.categoryId,
                                categoryName = it.categoryName,
                                parentCategoryId = it.parentCategoryId,
                                level = level,
                                primaryMinorUnitAmount = it.primaryMinorUnitAmount
                            )
                        }
                    }
                emit(items)
            }
    }

    override suspend fun getAllCategoriesIncludedDeleted(): List<TransactionCategory> {
        return transactionCategoryDao.getAllCategoriesIncludedDeleted().firstOrNull()?.map {
            TransactionCategory(
                it.id,
                it.transactionTypeCode,
                it.name,
                it.parentId,
                it.isDeleted
            )
        } ?: listOf()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCategories(transactionTypeCode: String): Flow<TransactionCategories> {
        val res = transactionCategoryDao.getCategories(transactionTypeCode)
        return res.transformLatest { list ->
            val map = list.groupBy { it.parentId }
            val items = list.filter { it.parentId == 0 }
                .map {
                    buildCategoryTree(
                        retrievalKey = {
                            it.id
                        },
                        parentCatIDChildrenMap = map,
                        responseEntity = it,
                    ) { level, model ->
                        TransactionCategories.BasicCategoryNode(
                            categoryId = model.id,
                            categoryName = model.name,
                            parentCategoryId = model.parentId,
                            level = level
                        )
                    }
                }
            emit(TransactionCategories(items = items))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCategoriesIncludeDeleted(transactionTypeCode: String): Flow<TransactionCategories> {
        val res = transactionCategoryDao.getCategoriesIncludeDeleted(transactionTypeCode)
        return res.transformLatest { list ->
            val map = list.groupBy { it.parentId }
            val items = list.filter { it.parentId == 0 }
                .map {
                    buildCategoryTree(
                        retrievalKey = {
                            it.id
                        },
                        parentCatIDChildrenMap = map,
                        responseEntity = it,
                    ) { level, entity ->
                        TransactionCategories.BasicCategoryNode(
                            categoryId = entity.id,
                            categoryName = entity.name,
                            parentCategoryId = entity.parentId,
                            level = level
                        )
                    }
                }
            emit(TransactionCategories(items = items))
        }
    }

    override suspend fun addCategory(category: String, parentId: Int, transactionTypeCode: String) {
        val entity = TransactionCategoryEntity(
            name = category,
            parentId = parentId,
            transactionTypeCode = transactionTypeCode
        )
        return transactionCategoryDao.addCategory(entity)
    }

    override fun getCategoryById(categoryId: Int): Transaction.TransactionCategory {
        val res = transactionCategoryDao.getCategoryById(categoryId)
        return Transaction.TransactionCategory(
            res.id,
            res.name
        )
    }

    override suspend fun deleteCategory(categoryId: Int) {
        transactionCategoryDao.deleteCategory(categoryId)
    }
}

internal data class CategoryAmountNode(
    override val categoryId: Int,
    override val categoryName: String,
    override val parentCategoryId: Int,
    val level: Int,
    private val primaryMinorUnitAmount: Long
) : CategoryNode<CategoryAmountNode> {
    val adjustedExpensesAmountInMinorUnit: Long
        get() {
            return primaryMinorUnitAmount + (children
                .map { it.adjustedExpensesAmountInMinorUnit }
                .reduceOrNull { acc, l -> acc + l } ?: 0)
        }
    private val _children = mutableListOf<CategoryAmountNode>()
    override val children get() = _children

    override fun addChildren(children: List<CategoryAmountNode>) {
        _children.addAll(children)
    }
}