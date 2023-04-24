package com.enciyo.jetspeed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enciyo.data.model.Server
import com.enciyo.data.model.SpeedTestResult
import com.enciyo.data.source.SpeedTestSource
import com.enciyo.data.source.local.LocalDataSource
import com.enciyo.data.source.remote.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val speedTestSource: SpeedTestSource
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeScreenState(loading = true))
    val uiState = _uiState.asStateFlow()

    private val _progressState = MutableStateFlow(ProgressState())
    val progressState = _progressState.asStateFlow()


    init {
        viewModelScope.launch {
            remoteDataSource.getSettings()
                .fold(
                    onSuccess = { result ->
                        _uiState.update {
                            it.copy(
                                loading = false,
                                servers = result,
                                selectedServer = null
                            )
                        }
                        result.firstOrNull()?.let(::onSelected)
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

    fun getDownloadSpeedTest() {
        viewModelScope.launch {
            val host = localDataSource.store.first().host
            speedTestSource.getDownloadSpeed(host)
                .onEach { result ->
                    _progressState.update {
                        when (result) {
                            SpeedTestResult.OnComplete -> it
                            is SpeedTestResult.OnProgress -> it.copy(
                                text = result.transferRateBit,
                                percent = result.percent
                            )
                        }
                    }
                }
                .launchIn(viewModelScope)
        }

    }

    fun onSelected(server: Server) {
        viewModelScope.launch {
            localDataSource.updateHost(server.host)
            _uiState.update { homeScreenState ->
                homeScreenState.copy(selectedServer = server)
            }
        }
    }
}

data class HomeScreenState(
    val servers: List<Server> = emptyList(),
    val loading: Boolean = false,
    val error: String = "",
    val selectedServer: Server? = null,
)

data class ProgressState(
    val text: String = "",
    val percent: Float = 0.1f
)