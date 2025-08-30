package com.example.physiotherapyapp.presentation.device

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.physiotherapyapp.data.bluetooth.ConnectionState
import com.example.physiotherapyapp.data.bluetooth.PhysiotherapyBluetoothManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceConnectionViewModel @Inject constructor(
    private val bluetoothManager: PhysiotherapyBluetoothManager
) : ViewModel() {
    
    val connectionState: StateFlow<ConnectionState> = bluetoothManager.connectionState
    val discoveredDevices: StateFlow<List<BluetoothDevice>> = bluetoothManager.discoveredDevices
    
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()
    
    fun startScanning() {
        viewModelScope.launch {
            _isScanning.value = true
            bluetoothManager.startScanning()
            // Stop scanning after 30 seconds
            kotlinx.coroutines.delay(30000)
            stopScanning()
        }
    }
    
    fun stopScanning() {
        _isScanning.value = false
        bluetoothManager.stopScanning()
    }
    
    fun connectToDevice(device: BluetoothDevice) {
        stopScanning()
        bluetoothManager.connectToDevice(device)
    }
    
    fun disconnect() {
        bluetoothManager.disconnect()
    }
    
    override fun onCleared() {
        super.onCleared()
        stopScanning()
        disconnect()
    }
}

