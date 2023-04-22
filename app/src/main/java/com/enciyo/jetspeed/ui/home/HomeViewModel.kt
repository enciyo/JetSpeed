package com.enciyo.jetspeed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enciyo.data.model.Server
import com.enciyo.data.source.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeScreenState(loading = true))
    val uiState = _uiState.asStateFlow()


    init {
        viewModelScope.launch {
            remoteDataSource.getSettings()
                .fold(
                    onSuccess = { result ->
                        _uiState.update {
                            it.copy(
                                loading = false,
                                servers = result,
                                selectedServer = result.firstOrNull()
                            )
                        }
                    },
                    onFailure = { throwable ->
                        _uiState.update {
                            it.copy(
                                loading = false,
                                error = throwable.message.orEmpty()
                            )
                        }
                    })
        }
    }

    fun onSelected(server: Server) {
        _uiState.update { homeScreenState ->
            homeScreenState.copy(selectedServer = server)
        }
    }

}


data class HomeScreenState(
    val servers: List<Server> = emptyList(),
    val loading: Boolean = false,
    val error: String = "",
    val selectedServer: Server? = null
)