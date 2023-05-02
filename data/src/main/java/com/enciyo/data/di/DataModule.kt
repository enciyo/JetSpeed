package com.enciyo.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.enciyo.data.Network
import com.enciyo.data.RepositoryImp
import com.enciyo.data.source.SpeedTestSource
import com.enciyo.data.source.SpeedTestSourceImp
import com.enciyo.data.source.local.LocalDataSource
import com.enciyo.data.source.local.LocalDataSourceImp
import com.enciyo.data.source.remote.RemoteDataSource
import com.enciyo.data.source.remote.RemoteDataSourceImp
import com.example.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "JetSpeedSettings")

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

        @Provides
        @Singleton
        fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    }

    @Binds
    @Reusable
    fun bindRemoteDataSource(remoteDataSourceImp: RemoteDataSourceImp): RemoteDataSource

    @Binds
    @Reusable
    fun bindSpeedTestSource(speedTestSourceImp: SpeedTestSourceImp): SpeedTestSource

    @Binds
    @Reusable
    fun bindLocalDataSource(localDataSourceImp: LocalDataSourceImp): LocalDataSource

    @Binds
    @Reusable
    fun bindRepository(repositoryImp: RepositoryImp): Repository


}
