package com.androsh.shopee.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.androsh.shopee.data.database.dao.DaoProduct
import com.androsh.shopee.data.database.entities.CategoryDataRoom
import com.androsh.shopee.data.database.entities.ProductDataRoom

@Database(
    entities = [ProductDataRoom::class, CategoryDataRoom::class], version = 3
)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun daoProduct(): DaoProduct
}
