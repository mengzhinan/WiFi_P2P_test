package com.duke.p2plib

import android.content.Context
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
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

    

    /**
     * 检测范围内的可用对等设备。此操作为异步的。
     */
    fun discoverPeers(context: Context?, actionListener: WifiP2pManager.ActionListener?) {
        getWifiP2PManager(context)
        wifiP2PManager?.discoverPeers(channel, actionListener)
    }

    /**
     * 请求已发现对等设备的列表。此操作为异步的。
     */
    fun requestPeers(context: Context?, peerListListener: WifiP2pManager.PeerListListener?) {
        getWifiP2PManager(context)
        wifiP2PManager?.requestPeers(channel, peerListListener)
    }

    fun connect(
        context: Context?,
        device: WifiP2pDevice,
        actionListener: WifiP2pManager.ActionListener?
    ) {
        val config = WifiP2pConfig()
        config.deviceAddress = device.deviceAddress
        wifiP2PManager?.connect(channel, config, actionListener)
    }

    fun registerReceiver(
        context: Context?,
        onP2PChangeListener: WifiP2PReceiver.OnP2PChangeListener?,
        peerListListener: WifiP2pManager.PeerListListener?
    ) {
        if (wifiP2PReceiver == null) {
            wifiP2PReceiver = WifiP2PReceiver()
        }
        getWifiP2PManager(context)
        wifiP2PReceiver?.register(context, onP2PChangeListener, peerListListener)
    }

    fun unRegisterReceiver(context: Context?) {
        wifiP2PReceiver?.unRegister(context)
        wifiP2PReceiver = null
    }

}