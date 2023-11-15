package com.yazag.capstoneproject.di

import com.yazag.capstoneproject.data.repository.ProductRepository
import com.yazag.capstoneproject.data.source.local.ProductDao
import com.yazag.capstoneproject.data.source.remote.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductsRepository(
        productService: ProductService,
        productDao: ProductDao
    ) = ProductRepository(productService, productDao)
}