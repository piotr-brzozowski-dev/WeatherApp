package com.example.weatherapp.search

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SearchCityModule {

    @Provides
    @Singleton
    fun provideSearchCityApi(retrofit: Retrofit): SearchCityApi =
        retrofit.create(SearchCityApi::class.java)
}