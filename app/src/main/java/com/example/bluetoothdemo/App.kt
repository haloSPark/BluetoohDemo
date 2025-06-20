package com.example.bluetoothdemo

import android.app.Application
import com.drake.brv.BR
import com.drake.brv.utils.BRV
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // BRV绑定model ID
        BRV.modelId = BR.m
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { _, _ -> MaterialHeader(this) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { _, _ -> ClassicsFooter(this) }
    }
}