package com.duke.wifilib

import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:18
 * @Description: wifi 工具类
 * https://developer.android.google.cn/guide/topics/connectivity/wifi-scan?hl=zh_cn
 */
object WifiUtil {
    private var wifiManager: WifiManager? = null
    private var wifiReceiver: WifiReceiver? = null

    @Synchronized
    fun getWifiManager(context: Context?): WifiManager? {
        if (context == null || wifiManager != null) {
            return wifiManager
        }
        wifiManager = context.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager
    }

    /**
     * 执行扫描操作是否成功。
     * 每个前台应用可以在 2 分钟内扫描 4 次。这样便可在短时间内进行多次扫描。
     * 所有后台应用组合可以在 30 分钟内扫描一次。
     * 以后的版本中可能会删除此方法。
     *
     * @param context context
     * @return is succeeded
     * @throws SecurityException() 抛出异常，如果没有需要的权限之一
     * ACCESS_FINE_LOCATION
     * ACCESS_COARSE_LOCATION
     * CHANGE_WIFI_STATE
     */
    @Synchronized
    @Throws(SecurityException::class)
    fun startScan(context: Context?): Boolean {
        return getWifiManager(context)?.startScan() ?: false
    }

    /**
     * 收到扫描完成广播后，调用此方法可获得最新的扫描结果，否则获得到的是历史结果。
     *
     * @param context context
     * @return 扫描结果
     */
    @Synchronized
    fun getScanResults(context: Context?): List<ScanResult>? {
        return getWifiManager(context)?.scanResults
    }

    @Synchronized
    fun registerReceiver(
        context: Context?,
        onScanCompletedListener: WifiReceiver.OnScanCompletedListener?
    ) {
        if (wifiReceiver == null) {
            wifiReceiver = WifiReceiver()
        }
        getWifiManager(context)
        wifiReceiver?.register(context, onScanCompletedListener)
    }

    @Synchronized
    fun unRegisterReceiver(context: Context?) {
        wifiReceiver?.unRegister(context)
        wifiReceiver = null
    }
}