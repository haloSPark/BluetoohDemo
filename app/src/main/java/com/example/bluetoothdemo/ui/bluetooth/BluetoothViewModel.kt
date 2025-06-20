package com.example.bluetoothdemo.ui.bluetooth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothdemo.data.repository.BluetoothRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

/**
 * 蓝牙ViewModel
 */
class BluetoothViewModel(app: Application): AndroidViewModel(app) {

    private val repo = BluetoothRepository(app)
    // 蓝牙设备
    val devices = repo.devicesFlow.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // 开始扫描
    fun startScan() = repo.startScan()

    //停止扫描
    fun stopScan() = repo.stopScan()
}
