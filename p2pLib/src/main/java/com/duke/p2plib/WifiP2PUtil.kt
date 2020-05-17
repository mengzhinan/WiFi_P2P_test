package com.duke.p2plib

import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import android.os.Looper

/**
 * @Author: duke
 * @DateTime: 2020-05-17 12:34
 * @Description: WLAN 直连（对等连接或 P2P）
 * https://developer.android.google.cn/guide/topics/connectivity/wifip2p?hl=zh_cn
 */
object WifiP2PUtil {

    private var wifiP2PManager: WifiP2pManager? = null
    private var channel: WifiP2pManager.Channel? = null
    private var wifiP2PReceiver: WifiP2PReceiver? = null

    @Synchronized
    fun getWifiP2PManager(context: Context?): WifiP2pManager? {
        if (context == null || wifiP2PManager != null) {
            return wifiP2PManager
        }
        wifiP2PManager = context.applicationContext
            .getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        return wifiP2PManager
    }

    @Synchronized
    fun init(context: Context?, channelListener: WifiP2pManager.ChannelListener?) {
        getWifiP2PManager(context)
        channel = wifiP2PManager?.initialize(context, Looper.getMainLooper(), channelListener)
    }


    @Synchronized
    fun registerReceiver(
        context: Context?,
        onP2PChangeListener: WifiP2PReceiver.OnP2PChangeListener?
    ) {
        if (wifiP2PReceiver == null) {
            wifiP2PReceiver = WifiP2PReceiver()
        }
        getWifiP2PManager(context)
        wifiP2PReceiver?.register(context, onP2PChangeListener)
    }

    @Synchronized
    fun unRegisterReceiver(context: Context?) {
        wifiP2PReceiver?.unRegister(context)
        wifiP2PReceiver = null
    }

}