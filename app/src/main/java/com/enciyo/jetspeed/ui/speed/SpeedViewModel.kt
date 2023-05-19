package com.enciyo.jetspeed.ui.speed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.EMPTY
import com.example.ZERO
import com.example.HYPHEN
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

    private fun start() {
        getDownloadSpeed()
    }

    private fun getDownloadSpeed() {
        repository.getDownloadSpeed()
            .onEach { result ->
                when (result) {
                    is SpeedTestResult.OnComplete -> onCompletedDownloadSpeed(result)
                    is SpeedTestResult.OnProgress -> onProgress(result)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getUploadSpeed() {
        repository.getUploadSpeed()
            .onEach { result ->
                when (result) {
                    is SpeedTestResult.OnComplete -> onCompletedUploadSpeed(result)
                    is SpeedTestResult.OnProgress -> onProgress(result)
                }
            }
            .launchIn(viewModelScope)
    }


    private fun onCompletedDownloadSpeed(result: SpeedTestResult.OnComplete) {
        _uiState.update { exist ->
            exist.copy(
                downloadTime = result.time,
                speedText = String.EMPTY
            )
        }
        getUploadSpeed()
    }

    private fun onCompletedUploadSpeed(result: SpeedTestResult.OnComplete) {
        _uiState.update { exist ->
            exist.copy(
                uploadTime = result.time,
                speedText = String.EMPTY,
                progress = Float.ZERO,
                isCompleted = true
            )
        }
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
    val uploadTime: String = String.HYPHEN,
    val downloadTime: String = String.HYPHEN,
    val speedText: String = String.EMPTY,
    val progress: Float = Float.ZERO,
    val isCompleted: Boolean = false
)