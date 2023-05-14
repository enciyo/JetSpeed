package com.enciyo.data

import com.enciyo.data.source.SpeedTestSource
import com.enciyo.data.source.local.LocalDataSource
import com.enciyo.data.source.remote.RemoteDataSource
import com.example.domain.Repository
import com.example.domain.model.Server
import dagger.Reusable
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val speedTestSource: SpeedTestSource
) : Repository {


    override fun getSettings() = flow {
        emit(remoteDataSource.getSettings())
    }

    override fun updateHost(server: Server) = flow<Result<Server>> {
        localDataSource.updateHost(server.host)
        emit(Result.success(server))
    }

    override fun getDownloadSpeed() =
        localDataSource.store
            .map { it.host }
            .flatMapConcat { speedTestSource.getDownloadSpeed(it) }


    override fun getUploadSpeed() = localDataSource.store
        .map { it.host }
        .flatMapConcat { speedTestSource.getUploadSpeed(it) }

}