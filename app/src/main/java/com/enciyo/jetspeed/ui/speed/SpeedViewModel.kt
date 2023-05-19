package com.enciyo.jetspeed.ui.speed

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Repository
import com.example.domain.model.SpeedTestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SpeedViewModel @Inject constructor(private val repository: Repository) : ViewModel() {


    private val _uiState = MutableStateFlow(SpeedUiState())
    val state = _uiState.asStateFlow()


    init {
        start()
    }

    private fun start(){
        getDownloadSpeed()
    }


    private fun getDownloadSpeed() {
        repository.getDownloadSpeed()
            .onEach { result ->
                when (result) {
                    is SpeedTestResult.OnComplete -> {
                        _uiState.update {
                            it.copy(
                                downloadTime = result.time,
                                speedText = "",
                            )
                        }
                        getUploadSpeed()
                    }

                    is SpeedTestResult.OnProgress -> {
                        onProgress(result)
                    }
                }
            }
            .launchIn(viewModelScope)
    }


    private fun getUploadSpeed() {
        repository.getUploadSpeed()
            .onEach { result ->
                when (result) {
                    is SpeedTestResult.OnComplete -> {
                        _uiState.update {
                            it.copy(
                                uploadTime = result.time,
                                speedText = "",
                                progress = 0f,
                                isCompleted = true,
                            )
                        }
                    }

                    is SpeedTestResult.OnProgress -> onProgress(result)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun onProgress(result: SpeedTestResult.OnProgress) {
        _uiState.update {
            it.copy(
                speedText = result.transferRateBit,
                progress = result.percent,
                isCompleted = false
            )
        }
    }

    fun retry() {
        _uiState.value = SpeedUiState()
        start()
    }


}


data class SpeedUiState(
    val uploadTime: String = "-",
    val downloadTime: String = "-",
    val speedText: String = "",
    val progress: Float = 0f,
    val isCompleted: Boolean = false
)