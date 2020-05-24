package com.duke.p2plib.sockethelper;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @Author: duke
 * @DateTime: 2019-05-26 10:53
 * @Description:
 */
public abstract class SocketBase {
    protected static final int SERVER_PORT = 65432;
    // 品牌-型号
    protected static final String PHONE_INFO = Build.BRAND + " - " + Build.MODEL;

    public interface OnReceiveListener {
        void onReceived(String text);
    }

    private OnReceiveListener onReceiveListener;

    public SocketBase(OnReceiveListener onReceiveListener) {
        this.onReceiveListener = onReceiveListener;
    }

    protected void postToUI(String text) {
        Message message = Message.obtain(handler);
        message.obj = text;
        message.sendToTarget();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg == null || msg.obj == null || onReceiveListener == null) {
                return;
            }
            onReceiveListener.onReceived(msg.obj.toString());
        }
    };

    public void clear() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        onReceiveListener = null;
    }
}
