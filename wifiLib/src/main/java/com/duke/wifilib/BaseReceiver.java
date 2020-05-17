package com.duke.wifilib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

/**
 * @Author: duke
 * @DateTime: 2020-05-17 11:21
 * @Description:
 */
public abstract class BaseReceiver extends BroadcastReceiver {
    private boolean isRegister;
    private boolean isActionAdded;
    private IntentFilter intentFilter = new IntentFilter();

    public void register(Context context, String action) {
        if (isRegister || context == null || TextUtils.isEmpty(action)) {
            return;
        }
        if (!isActionAdded) {
            intentFilter.addAction(action);
            isActionAdded = true;
        }
        context.registerReceiver(this, intentFilter);
        isRegister = true;
    }

    public void unRegister(Context context) {
        if (!isRegister || context == null) {
            return;
        }
        context.unregisterReceiver(this);
        isRegister = false;
    }
}
