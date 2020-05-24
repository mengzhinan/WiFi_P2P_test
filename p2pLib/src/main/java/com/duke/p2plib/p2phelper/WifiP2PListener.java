package com.duke.p2plib.p2phelper;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;

import androidx.annotation.NonNull;

import java.util.Collection;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:24
 * description:
 */
public interface WifiP2PListener {

    void onWifiP2pEnabled(boolean isEnabled);

    void onCreateGroup(boolean isSuccess);

    void onRemoveGroup(boolean isSuccess);

    void onDiscoverPeers(boolean isSuccess);

    /**
     * 无用
     * 客户端主动发起的连接的动作是否成功 (非 与服务器连接是否成功)
     *
     * @param isConnected
     */
    void onConnectCallChanged(boolean isConnected);
    void onDisConnectCallChanged(boolean isDisConnected);

    /**
     * 废弃
     * 此设备的WiFi状态更改回调(本地设备 p2p 的连接状态，连接成功、失败时会回调)
     * 此广播 会和 WIFI_P2P_CONNECTION_CHANGED_ACTION 同时回调
     * 注册广播、连接成功、连接失败 三种时机都会调用
     *
     * @param wifiP2pDevice
     */
    void onSelfDeviceAvailable(@NonNull WifiP2pDevice wifiP2pDevice);

    /**
     * 是否与服务器成功建立连接(与服务器连接成功、失败)
     *
     * @param wifiP2pInfo
     */
    void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo);

    /**
     * 扫描的设备结果
     *
     * @param wifiP2pDeviceList
     */
    void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList);


    void onGroupInfoAvailable(WifiP2pGroup group);

}
