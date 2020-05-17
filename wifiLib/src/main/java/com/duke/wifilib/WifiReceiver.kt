package com.duke.wifilib

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.text.TextUtils

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:20
 * @Description:
 */
class WifiReceiver : BaseReceiver() {
    companion object {
        private const val WIFI_ACTION = WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
    }

    private var onScanCompletedListener: OnScanCompletedListener? = null

    interface OnScanCompletedListener {
        fun onScanCompleted(isSuccess: Boolean)
    }

    fun register(
        context: Context?,
        onScanCompleted: OnScanCompletedListener?
    ) {
        super.register(context, WIFI_ACTION)
        onScanCompletedListener = onScanCompleted
    }

    override fun onReceive(context: Context, intent: Intent) {
        var success = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        }
        val action = intent.action
        if (!TextUtils.isEmpty(action) && WIFI_ACTION == action) {
            onScanCompletedListener?.onScanCompleted(success)
        }
    }
}