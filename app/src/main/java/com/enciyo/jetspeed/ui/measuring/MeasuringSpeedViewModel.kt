package com.enciyo.jetspeed.ui.measuring

import androidx.lifecycle.ViewModel
import com.enciyo.data.source.SpeedTestSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MeasuringSpeedViewModel @Inject constructor(
    private val speedTestSource: SpeedTestSource
) : ViewModel() {


}