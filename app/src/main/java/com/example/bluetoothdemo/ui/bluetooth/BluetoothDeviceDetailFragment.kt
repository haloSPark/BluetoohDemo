package com.example.bluetoothdemo.ui.bluetooth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.bluetoothdemo.R
import com.example.bluetoothdemo.databinding.FragmentBluetoothDeviceDetailBinding


/**
 * 蓝牙设备详情页
 */
class BluetoothDeviceDetailFragment : Fragment(R.layout.fragment_bluetooth_device_detail) {

    private lateinit var binding : FragmentBluetoothDeviceDetailBinding
    private val args: BluetoothDeviceDetailFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding =  FragmentBluetoothDeviceDetailBinding.bind(view)
        binding.deviceName.text = getString(R.string.device_name, args.deviceName)
        binding.deviceAddress.text = getString(R.string.device_address, args.deviceAddress)
    }
}