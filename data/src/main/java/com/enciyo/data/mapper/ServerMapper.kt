package com.enciyo.data.mapper

import com.enciyo.data.model.ServerResponse
import com.example.domain.model.Server
import com.example.shared.Mapper
import dagger.Reusable
import javax.inject.Inject


@Reusable
class ServerMapper @Inject constructor() : Mapper<ServerResponse, Server> {
     override fun mapTo(input: ServerResponse): Server = Server(
        url = input.url,
        name = input.name,
        country = input.country,
        sponsor = input.sponsor,
        host = input.host
    )
}
