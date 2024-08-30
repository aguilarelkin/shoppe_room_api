package com.androsh.shopee.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.androsh.shopee.data.database.dao.DaoProduct
import com.androsh.shopee.data.database.entities.CategoryDataRoom
import com.androsh.shopee.data.database.entities.ProductDataRoom

@Database(entities = [ProductDataRoom::class, CategoryDataRoom::class], version = 2)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun daoProduct(): DaoProduct
}
