package com.enciyo.jetspeed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetServersUseCase
import com.example.domain.usecase.UpdateHostUseCase
import com.example.domain.model.Server
import com.example.onFailure
import com.example.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getServersUseCase: GetServersUseCase,
    private val updateHostUseCase: UpdateHostUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(interactions: HomeScreenInteractions) {
        when (interactions) {
            is HomeScreenInteractions.OnSelected -> onSelected(interactions.server)
        }
    }

    init {
        getServers()
    }

    private fun getServers() {
        getServersUseCase.invoke(Unit)
            .onSuccess(::onServersSuccess)
            .onFailure(::onError)
            .launchIn(viewModelScope)
    }

    private fun onSelected(server: Server) {
        updateHostUseCase.invoke(server)
            .onSuccess(::onSelectedSuccess)
            .onFailure(::onError)
            .launchIn(viewModelScope)
    }

    private fun onServersSuccess(servers: List<Server>) {
        _uiState.update { exist ->
            exist.copy(
                servers = servers,
                currentServer = servers.firstOrNull()
            )
        }
    }

    private fun onSelectedSuccess(server: Server) {
        _uiState.update { exist -> exist.copy(currentServer = server) }
    }

    private fun onError(error: Throwable) {
        _uiState.update { exist -> exist.copy(error = error.message) }
    }


}

data class HomeUiState(
    val servers: List<Server> = emptyList(),
    val currentServer: Server? = null,
    val error: String? = null,
)

sealed interface HomeScreenInteractions {
    data class OnSelected(val server: Server) : HomeScreenInteractions
}