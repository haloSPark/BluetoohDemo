package com.example.bluetoothdemo.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothdemo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }
}
