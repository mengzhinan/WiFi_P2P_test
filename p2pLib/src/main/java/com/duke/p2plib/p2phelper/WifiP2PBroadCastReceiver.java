package com.duke.p2plib.p2phelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.text.TextUtils;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:25
 * description:
 */
public class WifiP2PBroadCastReceiver extends BroadcastReceiver {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2PListener mWifiP2PListener;
    private boolean isReceiverRegistered;

    public WifiP2PBroadCastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel) {
        this.mManager = manager;
        this.mChannel = channel;
    }

    public void setListener(WifiP2PListener wifiP2PListener) {
        this.mWifiP2PListener = wifiP2PListener;
    }

    public void registerReceiver(Context context) {
        if (context == null || isReceiverRegistered) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intentFilter.addAction(WifiP2pManager.EXTRA_WIFI_P2P_GROUP);
        }
        context.registerReceiver(this, intentFilter);
        isReceiverRegistered = true;
    }

    public void unRegisterReceiver(Context context) {
        if (context == null || !isReceiverRegistered) {
            return;
        }
        context.unregisterReceiver(this);
        isReceiverRegistered = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null ||
                intent == null ||
                TextUtils.isEmpty(intent.getAction())
                || mManager == null) {
            return;
        }
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            // 检查 Wi-Fi P2P 是否已启用
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            boolean isEnabled = (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
            if (mWifiP2PListener != null) {
                mWifiP2PListener.onWifiP2pEnabled(isEnabled);
            }
            /*if (isEnabled) {
                // Wifi P2P is enabled
            } else {
                // Wi-Fi P2P is not enabled
            }*/
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            WifiP2pDeviceList wifiP2pDeviceList = intent.getParcelableExtra(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
            if (mWifiP2PListener != null && wifiP2pDeviceList != null) {
                mWifiP2PListener.onPeersAvailable(wifiP2pDeviceList.getDeviceList());
            }
            // 异步方法
            WifiP2PHelper.getInstance(context).requestPeers();
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            // 链接状态变化回调
            // 此广播 会和 WIFI_P2P_THIS_DEVICE_CHANGED_ACTION 同时回调
            // 注册广播、连接成功、连接失败 三种时机都会调用
            // 应用可使用 requestConnectionInfo()、requestNetworkInfo() 或 requestGroupInfo() 来检索当前连接信息。
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null
                    && networkInfo.isConnected()
                    && mManager != null
                    && mWifiP2PListener != null) {
                WifiP2PHelper.getInstance(context).requestConnectInfo();
            } else {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onConnectionInfoAvailable(null);
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            // 此设备的WiFi状态更改回调
            // 此广播 会和 WIFI_P2P_CONNECTION_CHANGED_ACTION 同时回调
            // 注册广播、连接成功、连接失败 三种时机都会调用
            // 应用可使用 requestDeviceInfo() 来检索当前连接信息。
            WifiP2pDevice wifiP2pDevice = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            if (mWifiP2PListener != null) {
                mWifiP2PListener.onSelfDeviceAvailable(wifiP2pDevice);
            }
        }
    }
}
