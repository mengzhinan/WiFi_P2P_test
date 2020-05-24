package com.duke.p2plib.p2phelper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * author: duke
 * version: 1.0
 * dateTime: 2019-05-20 18:26
 * description: https://developer.android.google.cn/guide/topics/connectivity/wifip2p
 * Android 4.0 设备
 */
public class WifiP2PHelper {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2PBroadCastReceiver mReceiver;

    private WifiP2PListener mWifiP2PListener;
    private Context mApplicationContext;

    private static volatile WifiP2PHelper instance;

    public synchronized static WifiP2PHelper getInstance(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Parameter context is null exception.");
        }
        if (instance == null) {
            instance = new WifiP2PHelper(context);
        }
        return instance;
    }

    private WifiP2PHelper(Context context) {
        if (context == null || context.getApplicationContext() == null) {
            throw new IllegalArgumentException("context is null exception.");
        }
        mApplicationContext = context.getApplicationContext();

        mManager = (WifiP2pManager) mApplicationContext.getSystemService(Context.WIFI_P2P_SERVICE);
        // 将此应用注册到 WLAN P2P 框架
        mChannel = mManager.initialize(mApplicationContext, Looper.getMainLooper(), null);
        mReceiver = new WifiP2PBroadCastReceiver(mManager, mChannel);
    }

    public WifiP2PHelper setListener(WifiP2PListener wifiP2PListener) {
        this.mWifiP2PListener = wifiP2PListener;
        if (mReceiver != null) {
            mReceiver.setListener(this.mWifiP2PListener);
        }
        return this;
    }

    public boolean isSupportWifiP2P() {
        return mApplicationContext != null
                && mApplicationContext.getPackageManager() != null
                && mApplicationContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT);
    }

    public void createGroup() {
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onCreateGroup(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onCreateGroup(false);
                }
            }
        });
    }

    public void removeGroup() {
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onRemoveGroup(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onRemoveGroup(false);
                }
            }
        });
    }

    public void connect(WifiP2pDevice wifiP2pDevice) {
        if (wifiP2pDevice == null || TextUtils.isEmpty(wifiP2pDevice.deviceAddress)) {
            return;
        }
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = wifiP2pDevice.deviceAddress;
        config.wps.setup = WpsInfo.PBC;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onConnectCallChanged(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onConnectCallChanged(false);
                }
            }
        });
    }

    /**
     * 取消任何正在进行的对等群组协商
     */
    public void cancelConnect() {
        mManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onDisConnectCallChanged(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onDisConnectCallChanged(false);
                }
            }
        });
    }

    public void discover() {
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onDiscoverPeers(true);
                }
            }

            @Override
            public void onFailure(int reason) {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onDiscoverPeers(false);
                }
            }
        });
    }

    /**
     * 请求最新的配对数据列表
     */
    public void requestPeers() {
        mManager.requestPeers(mChannel, new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                if (mWifiP2PListener != null) {
                    if (peers != null) {
                        mWifiP2PListener.onPeersAvailable(peers.getDeviceList());
                    } else {
                        mWifiP2PListener.onPeersAvailable(new ArrayList<WifiP2pDevice>());
                    }
                }
            }
        });
    }

    /**
     * 请求设备连接信息
     */
    public void requestConnectInfo() {
        mManager.requestConnectionInfo(mChannel, new WifiP2pManager.ConnectionInfoListener() {

            @Override
            public void onConnectionInfoAvailable(WifiP2pInfo info) {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onConnectionInfoAvailable(info);
                }
            }
        });
    }

    /**
     * 请求设备连接信息
     */
    public void requestGroupInfo() {
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {

            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                if (mWifiP2PListener != null) {
                    mWifiP2PListener.onGroupInfoAvailable(group);
                }
            }
        });
    }

    public String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "可用的";
            case WifiP2pDevice.INVITED:
                return "邀请中";
            case WifiP2pDevice.CONNECTED:
                return "已连接";
            case WifiP2pDevice.FAILED:
                return "失败的";
            case WifiP2pDevice.UNAVAILABLE:
                return "不可用的";
            default:
                return "未知";
        }
    }

    public void registerReceiver() {
        if (mReceiver != null) {
            mReceiver.registerReceiver(mApplicationContext);
        }
    }

    public void unRegisterReceiver() {
        if (mReceiver != null) {
            mReceiver.unRegisterReceiver(mApplicationContext);
        }
    }
}
