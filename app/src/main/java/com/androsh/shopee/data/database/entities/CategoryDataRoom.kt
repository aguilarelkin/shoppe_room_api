package com.androsh.shopee.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.androsh.shopee.domain.models.Category
import com.google.gson.annotations.SerializedName

@Entity(tableName = "category")
data class CategoryDataRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @SerializedName("url") val url: String,
) {
    fun toDomain() = Category(name, url)
}
