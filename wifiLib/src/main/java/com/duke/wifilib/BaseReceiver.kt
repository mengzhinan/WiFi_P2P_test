package com.duke.wifilib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.text.TextUtils

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:21
 * @Description:
 */
abstract class BaseReceiver : BroadcastReceiver() {
    private var isRegister = false
    private var isActionAdded = false
    private val intentFilter = IntentFilter()
    fun register(context: Context?, action: String?) {
        if (isRegister || context == null) {
            return
        }
        if (!isActionAdded && !TextUtils.isEmpty(action)) {
            intentFilter.addAction(action)
            isActionAdded = true
        }
        context.registerReceiver(this, intentFilter)
        isRegister = true
    }

    fun unRegister(context: Context?) {
        if (!isRegister || context == null) {
            return
        }
        context.unregisterReceiver(this)
        isRegister = false
    }
}