package com.androsh.shopee.data.network

import android.content.Context
import androidx.room.Room
import com.androsh.shopee.data.RepositoryProductImpl
import com.androsh.shopee.data.RepositoryProuctRoomImpl
import com.androsh.shopee.data.database.ProductDatabase
import com.androsh.shopee.data.database.dao.DaoProduct
import com.androsh.shopee.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DATABASE_NAME = "products"

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
        return OkHttpClient.Builder()
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
    fun provideRepositoryProduct(daoProduct: DaoProduct): RepositoryProuctRoomImpl {
        return RepositoryProuctRoomImpl(daoProduct)
    }

    /**
     * Provide DDBB
     */
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(context, ProductDatabase::class.java, DATABASE_NAME).build()
    }
}