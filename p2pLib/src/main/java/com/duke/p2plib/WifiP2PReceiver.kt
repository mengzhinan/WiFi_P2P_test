package com.duke.p2plib

import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pManager
import com.duke.baselib.BaseReceiver

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:20
 * @Description:
 */
class WifiP2PReceiver : BaseReceiver() {

    private var onP2PChangeListener: OnP2PChangeListener? = null
    private var peerListListener: WifiP2pManager.PeerListListener? = null


    interface OnP2PChangeListener {
        /**
         * 当设备的 WLAN 连接状态更改时广播。
         * WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
         */
        fun onWifiConnectStatusChange()

        /**
         * 当您调用 discoverPeers() 时广播。如果您在应用中处理此 Intent，
         * 则通常需要调用 requestPeers() 以获取对等设备的更新列表。
         * WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION
         */
        fun onDiscoverPeers()

        /**
         * 当 WLAN P2P 在设备上启用或停用时广播。
         * WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION
         */
        fun onP2PWifiEnableChange(isWifiP2PEnable: Boolean)

        /**
         * 当设备的详细信息（例如设备名称）更改时广播。
         * WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
         */
        fun onDeviceInfoChange()
    }

    fun register(
        context: Context?,
        onP2PListener: OnP2PChangeListener?,
        tPeerListListener: WifiP2pManager.PeerListListener?
    ) {
        super.register(
            context,
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION,
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION,
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION,
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
        )
        onP2PChangeListener = onP2PListener
        peerListListener = tPeerListListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                // 检查 WiFi 是否已启用并通知相应的活动
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                val isWiFiP2PEnable = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
                onP2PChangeListener?.onP2PWifiEnableChange(isWiFiP2PEnable)
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                // 调用 WifiP2pManager.requestPeers（）获取当前对等点的列表
                // 如果发现进程成功并检测到对等设备，则系统会广播 WIFI_P2P_PEERS_CHANGED_ACTION Intent，
                // 您可以在广播接收器中侦听该 Intent，以获取对等设备列表。
                // 当应用接收到 WIFI_P2P_PEERS_CHANGED_ACTION Intent 时，
                // 您可以通过 requestPeers() 请求已发现对等设备的列表。
                WifiP2PUtil.requestPeers(context, peerListListener)
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections
                // 响应新连接或断开连接

            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
                // 响应此设备的 WiFi 状态更改

            }
            else -> {
                // do nothing and ignore
            }
        }
    }
}