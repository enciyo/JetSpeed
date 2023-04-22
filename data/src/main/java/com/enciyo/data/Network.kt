package com.enciyo.data

import com.enciyo.data.model.Settings
import retrofit2.http.GET

interface Network {

    @GET("speedtest-servers-static.php")
    suspend fun getSettings(): Settings
}


