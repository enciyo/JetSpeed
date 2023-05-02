package com.example.domain.usecase

import com.example.domain.Repository
import com.example.domain.UseCase
import com.example.domain.model.Server
import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@Reusable
class GetServersUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Unit, List<Server>>() {
    override fun invoke(request: Unit): Flow<Result<List<Server>>> =
        repository.getSettings()
}


