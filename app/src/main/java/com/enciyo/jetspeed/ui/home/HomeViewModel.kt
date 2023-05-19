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

    init {
        getServersUseCase.invoke(Unit)
            .onSuccess { settings ->
                _uiState.update {
                    it.copy(
                        servers = settings,
                        currentServer = settings.firstOrNull()
                    )
                }
            }
            .onFailure { throwable ->
                _uiState.update { it.copy(error = throwable.message) }
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(interactions: HomeScreenInteractions) {
        when (interactions) {
            is HomeScreenInteractions.OnSelected -> onSelected(interactions.server)
        }

    }

    private fun onSelected(server: Server) {
        updateHostUseCase.invoke(server)
            .onSuccess { result ->
                _uiState.update { it.copy(currentServer = result) }
            }
            .onFailure {
                _uiState.update { it.copy(error = it.error) }
            }
            .launchIn(viewModelScope)

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