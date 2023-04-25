package com.enciyo.data.source.remote

import com.enciyo.data.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class RemoteDataSourceImp @Inject constructor(
    private val network: Network,
) : RemoteDataSource {

    override suspend fun getSettings() = withContext(Dispatchers.IO) {
        return@withContext try {
            val servers = network.getSettings().servers.server
            Result.success(servers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}