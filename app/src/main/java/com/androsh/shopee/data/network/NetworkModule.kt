package com.androsh.shopee.data.network

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.androsh.shopee.data.RepositoryProductImpl
import com.androsh.shopee.data.RepositoryProuctRoomImpl
import com.androsh.shopee.data.database.ProductDatabase
import com.androsh.shopee.data.database.dao.DaoProduct
import com.androsh.shopee.domain.repository.ProductRepository
import com.androsh.shopee.domain.repository.ProductRepositoryRoom
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DATABASE_NAME = "products"

    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE category_new (id INTEGER NOT NULL, " + "name TEXT NOT NULL, " +
                        "url TEXT NOT NULL, " +
                        "PRIMARY KEY(id))"
            )

            // 2. Copiar los datos
            database.execSQL("INSERT INTO category_new (id, name) SELECT id, title FROM category")

            // 3. Eliminar la tabla original
            database.execSQL("DROP TABLE category")

            // 4. Renombrar la tabla temporal
            database.execSQL("ALTER TABLE category_new RENAME TO category")
        }
    }

    /**
     * Provide Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("https://dummyjson.com/").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    /**
     * Provide interceptor for info apis
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.MINUTES).writeTimeout(30, TimeUnit.MINUTES)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    /**
     * Provide Service API product
     */
    @Provides
    fun provideProductApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    fun provideProductRepository(productApiService: ProductApiService): ProductRepository {
        return RepositoryProductImpl(productApiService)
    }

    /**
     * Provide DDBB
     */
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(context, ProductDatabase::class.java, DATABASE_NAME)
            .addMigrations(
                MIGRATION_2_3
            ).build()
    }

    /**
     * Dao product
     */
    @Singleton
    @Provides
    fun provideDaoProduct(productDatabase: ProductDatabase): DaoProduct {
        return productDatabase.daoProduct()
    }

    /**
     * Return repository implementation
     */
    @Provides
    fun provideRepositoryProduct(daoProduct: DaoProduct): ProductRepositoryRoom {
        return RepositoryProuctRoomImpl(daoProduct)
    }

}