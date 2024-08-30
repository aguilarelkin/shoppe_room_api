package com.androsh.shopee.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androsh.shopee.domain.models.Category

@Entity(tableName = "category")
data class CategoryDataRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
) {
    fun toDomain() = Category(title)
}
