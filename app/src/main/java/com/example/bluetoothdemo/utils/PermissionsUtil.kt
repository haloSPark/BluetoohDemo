package com.example.bluetoothdatademo.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

/**
 * 蓝牙权限检测类
 */
object PermissionUtils {

    /**
     * 获取扫描蓝牙所需权限，自动适配不同 Android 版本
     */
    private fun getBluetoothPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }
    }

    /**
     * 检查是否全部权限都已授权
     * @param context
     * @param permissions 需要检查的权限
     */
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * 快速判断是否已授权蓝牙权限
     * @param context context
     */
    fun hasBluetoothPermissions(context: Context): Boolean {
        return hasPermissions(context, getBluetoothPermissions())
    }

    /**
     * 启动权限请求
     * @param launcher registerForActivityResult() 注册的 launcher
     */
    private fun requestPermissions(
        launcher: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>
    ) {
        launcher.launch(permissions)
    }

    /**
     * 启动蓝牙权限请求
     */
    fun requestBluetoothPermissions(
        launcher: ActivityResultLauncher<Array<String>>
    ) {
        requestPermissions(launcher, getBluetoothPermissions())
    }
}
