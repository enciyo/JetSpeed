package com.enciyo.data.di

import com.enciyo.data.Network
import com.enciyo.data.source.RemoteDataSource
import com.enciyo.data.source.RemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    companion object{
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient
        ): Retrofit = Retrofit.Builder()
            .baseUrl("https://c.speedtest.net/")
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideNetwork(retrofit: Retrofit) =
            retrofit.create<Network>()
    }

    @Binds
    @Reusable
    fun provideRemoteDataSource(remoteDataSourceImp: RemoteDataSourceImp): RemoteDataSource

}
