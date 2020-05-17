package com.duke.wifilib;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:18
 * @Description: wifi 工具类
 * https://developer.android.google.cn/guide/topics/connectivity/wifi-scan?hl=zh_cn
 */
public class WifiUtil {

    private static WifiManager wifiManager = null;
    private static WifiReceiver wifiReceiver = null;

    public static synchronized WifiManager getWifiManager(Context context) {
        if (context == null || wifiManager != null) {
            return wifiManager;
        }
        wifiManager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager;
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
     *                             ACCESS_FINE_LOCATION
     *                             ACCESS_COARSE_LOCATION
     *                             CHANGE_WIFI_STATE
     */
    public static synchronized boolean startScan(Context context) throws SecurityException {
        getWifiManager(context);
        if (wifiManager == null) {
            return false;
        }
        return wifiManager.startScan();
    }

    /**
     * 收到扫描完成广播后，调用此方法可获得最新的扫描结果，否则获得到的是历史结果。
     *
     * @param context context
     * @return 扫描结果
     */
    public static synchronized List<ScanResult> getScanResults(Context context) {
        getWifiManager(context);
        if (wifiManager == null) {
            return null;
        }
        return wifiManager.getScanResults();
    }

    public static synchronized void registerReceiver(Context context,
                                                     WifiReceiver.OnScanCompletedListener onScanCompletedListener) {
        if (wifiReceiver == null) {
            wifiReceiver = new WifiReceiver();
        }
        wifiReceiver.register(context, onScanCompletedListener);
    }

    public static synchronized void unRegisterReceiver(Context context) {
        if (wifiReceiver == null) {
            return;
        }
        wifiReceiver.unRegister(context);
        wifiReceiver = null;
    }
}
