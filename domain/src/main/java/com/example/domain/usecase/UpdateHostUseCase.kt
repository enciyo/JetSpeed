package com.example.domain.usecase

import com.example.domain.Repository
import com.example.domain.UseCase
import com.example.domain.model.Server
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@Reusable
class UpdateHostUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Server, Server>() {

    override fun invoke(request: Server): Flow<Result<Server>> =
        repository.updateHost(request)

}