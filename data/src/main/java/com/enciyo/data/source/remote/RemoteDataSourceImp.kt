package com.enciyo.data.source.remote

import com.enciyo.data.Network
import com.enciyo.data.mapper.ServerMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val network: Network,
    private val serverMapper: ServerMapper
) : RemoteDataSource {

    override suspend fun getSettings() = withContext(Dispatchers.IO) {
        return@withContext try {
            val servers = network.getSettings().servers.server
            Result.success(servers.map { serverMapper.mapTo(it) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}