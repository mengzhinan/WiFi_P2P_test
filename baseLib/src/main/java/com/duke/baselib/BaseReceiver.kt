package com.duke.baselib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:21
 * @Description:
 */
abstract class BaseReceiver : BroadcastReceiver() {
    private var isRegister = false
    private val intentFilter = IntentFilter()
    fun register(context: Context?, vararg actions: String?) {
        if (isRegister || context == null || actions.isEmpty()) {
            return
        }
        for (index in actions.indices) {
            intentFilter.addAction(actions[index])
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