package com.example.bluetoothdemo.ui.bluetooth

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.example.bluetoothdatademo.utils.PermissionUtils
import com.example.bluetoothdemo.R
import com.example.bluetoothdemo.data.model.BluetoothDeviceItem
import com.example.bluetoothdemo.databinding.FragmentBluetoothBinding
import kotlinx.coroutines.launch

/**
 * 蓝牙页面
 */
class BluetoothFragment : Fragment(R.layout.fragment_bluetooth) {

    private val viewModel: BluetoothViewModel by viewModels()
    private lateinit var binding: FragmentBluetoothBinding

    override fun onViewCreated(v: View, s: Bundle?) {
        binding = FragmentBluetoothBinding.bind(v)
        binding.recycler.linear().setup {
            addType<BluetoothDeviceItem>(R.layout.item_bluetooth)
            onClick(R.id.item_layout) {
                val bluetoothDeviceItem = getModel<BluetoothDeviceItem>()
                val action =
                    BluetoothFragmentDirections.toDetail(bluetoothDeviceItem.name ?: getString(R.string.unknown), bluetoothDeviceItem.address)
                findNavController().navigate(action)
            }
        }.models = viewModel.devices.value


        lifecycleScope.launch {
            viewModel.devices.collect {
                binding.recycler.models = it
            }
        }
        binding.scanBtn.setOnClickListener {
            checkAndRequestPermissions()
        }
        binding.nextPage.setOnClickListener {
            val action = BluetoothFragmentDirections.toPost()
            findNavController().navigate(action)
        }
    }

    // 获取蓝牙权限并开始扫描
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            viewModel.startScan()
        } else {
            Toast.makeText(requireContext(), getString(R.string.please_grant_bluetooth_permission), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 检查蓝牙权限并开始扫描
     */
    private fun checkAndRequestPermissions() {
        if (PermissionUtils.hasBluetoothPermissions(requireContext())) {
            viewModel.startScan()
        } else {
            PermissionUtils.requestBluetoothPermissions(permissionLauncher)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopScan()
    }
}