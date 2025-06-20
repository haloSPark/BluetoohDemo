package com.example.bluetoothdemo.data.repository

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.example.bluetoothdemo.data.model.BluetoothDeviceItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 蓝牙设备获取封装类
 */
class BluetoothRepository(private val context: Context) {

    // 获取蓝牙适配器
    private val adapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    // 存储设备列表
    private val _devices = MutableSharedFlow<List<BluetoothDeviceItem>>(replay = 1)
    // 转换成flow
    val devicesFlow = _devices.asSharedFlow()
    // 被扫描到的蓝牙设备
    private val discovered = mutableListOf<BluetoothDeviceItem>()

    // 蓝牙设备扫描监听广播
    private val receiver = object: BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(ctx: Context, intent: Intent) {

            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE", BluetoothDevice::class.java)
                    } else {
                        intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")
                    }

                    device?.let {
                        discovered.add(BluetoothDeviceItem(it.name, it.address))
                        _devices.tryEmit(discovered.toList())
                    }
                }

            }
        }
    }

    /**
     * 开始扫描蓝牙
     */
    @SuppressLint("MissingPermission")
    fun startScan() {
        discovered.clear()
        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        adapter.startDiscovery()
    }

    /**
     * 停止扫描蓝牙
     */
    @SuppressLint("MissingPermission")
    fun stopScan() {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: Exception){
            e.printStackTrace()
        }
        adapter.cancelDiscovery()
    }
}
