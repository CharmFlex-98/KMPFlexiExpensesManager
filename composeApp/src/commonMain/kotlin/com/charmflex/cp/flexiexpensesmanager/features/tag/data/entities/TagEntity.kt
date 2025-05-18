package com.charmflex.cp.flexiexpensesmanager.features.tag.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("tag_name")
    val tagName: String
)