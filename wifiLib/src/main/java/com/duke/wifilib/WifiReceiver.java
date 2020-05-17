package com.duke.wifilib;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:20
 * @Description:
 */
public class WifiReceiver extends BaseReceiver {
    private static final String WIFI_ACTION = WifiManager.SCAN_RESULTS_AVAILABLE_ACTION;
    private OnScanCompletedListener onScanCompletedListener;

    public interface OnScanCompletedListener {
        void onScanCompleted(boolean isSuccess);
    }

    public void register(Context context, OnScanCompletedListener onScanCompleted) {
        super.register(context, WIFI_ACTION);
        onScanCompletedListener = onScanCompleted;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean success = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
        }
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action)
                && WIFI_ACTION.equals(action)
                && onScanCompletedListener != null) {
            onScanCompletedListener.onScanCompleted(success);
        }
    }
}
