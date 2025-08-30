package com.example.physiotherapyapp.presentation.device

import androidx.lifecycle.ViewModel
import com.example.physiotherapyapp.data.bluetooth.DevicePlacement
import com.example.physiotherapyapp.data.bluetooth.PhysiotherapyBluetoothManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DevicePlacementViewModel @Inject constructor(
    private val bluetoothManager: PhysiotherapyBluetoothManager
) : ViewModel() {
    
    val devicePlacement: StateFlow<DevicePlacement?> = bluetoothManager.devicePlacement
    
    private val _selectedBodyPart = MutableStateFlow<String?>(null)
    val selectedBodyPart: StateFlow<String?> = _selectedBodyPart.asStateFlow()
    
    val isPlacementCorrect = devicePlacement.map { placement ->
        placement == DevicePlacement.CORRECT
    }
    
    fun selectBodyPart(bodyPart: String) {
        _selectedBodyPart.value = bodyPart
        // Send body part information to device for calibration
        // This would be implemented based on your device's communication protocol
    }
}

