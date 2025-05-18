package com.charmflex.cp.flexiexpensesmanager.features.transactions.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.charmflex.cp.flexiexpensesmanager.features.tag.data.entities.TagEntity
import com.charmflex.cp.flexiexpensesmanager.features.transactions.data.entities.TransactionEntity

@Entity(
   foreignKeys = [
       ForeignKey(
           entity = TagEntity::class,
           parentColumns = ["id"],
           childColumns = ["tagId"],
           onDelete = ForeignKey.CASCADE
       ),
       ForeignKey(
           entity = TransactionEntity::class,
           parentColumns = ["id"],
           childColumns = ["transaction_id"],
           onDelete = ForeignKey.CASCADE
       )
   ]
)
internal data class TransactionTagEntity(
    @PrimaryKey(true)
    val id: Int = 0,
    @ColumnInfo("tagId", index = true)
    val tagId: Int,
    @ColumnInfo("transaction_id", index = true)
    val transactionId: Int
)