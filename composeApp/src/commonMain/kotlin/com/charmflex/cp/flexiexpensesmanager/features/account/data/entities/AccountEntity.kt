package com.charmflex.cp.flexiexpensesmanager.features.account.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.charmflex.cp.flexiexpensesmanager.features.currency.data.entities.CurrencyMetaDataEntity

// Delete account should not real delete it, because transaction need a reference to it
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AccountGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["account_group_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CurrencyMetaDataEntity::class,
            parentColumns = ["currencyCode"],
            childColumns = ["currency"]
        )
    ]
)
internal data class AccountEntity(
    @PrimaryKey(true)
    val id: Int = 0,
    @ColumnInfo("account_group_id", index = true)
    val accountGroupId: Int,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("currency")
    val currency: String,
    @ColumnInfo("is_deleted", defaultValue = "false")
    val isDeleted: Boolean = false,
    @Embedded
    val additionalInfo: AdditionalInfo?
) {
    data class AdditionalInfo(
        @ColumnInfo("remarks")
        val remarks: String? = null
    )
}