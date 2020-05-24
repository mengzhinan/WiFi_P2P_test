package com.duke.p2plib.activity;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.duke.baselib.DLog;
import com.duke.p2plib.R;
import com.duke.p2plib.p2phelper.WifiP2PHelper;
import com.duke.p2plib.p2phelper.WifiP2PListener;
import com.duke.p2plib.sockethelper.ServerSocketHelper;
import com.duke.p2plib.sockethelper.SocketBase;

import java.util.Collection;

public class ServerReceiveActivity extends BaseActivity {

    private TextView showContent;
    private EditText input;
    private Button btnSend;

    private ServerSocketHelper serverSocketHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_receive);

        showContent = findViewById(R.id.show_content);
        input = findViewById(R.id.input);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                serverSocketHelper.send(text);
                input.setText("");
            }
        });

        serverSocketHelper = new ServerSocketHelper(new SocketBase.OnReceiveListener() {
            @Override
            public void onReceived(String text) {
                showContent.setText(text);
            }
        });

        WifiP2PHelper.getInstance(ServerReceiveActivity.this).createGroup();
    }

    @Override
    protected void onDestroy() {
        WifiP2PHelper.getInstance(ServerReceiveActivity.this).removeGroup();
        if (serverSocketHelper != null) {
            serverSocketHelper.clear();
        }
        super.onDestroy();
    }

    @Override
    protected WifiP2PListener getListener() {
        return mWifiP2PListener;
    }

    private WifiP2PListener mWifiP2PListener = new WifiP2PListener() {
        @Override
        public void onDiscoverPeers(boolean isSuccess) {
            toast(isSuccess ? "扫描设备成功" : "扫描设备失败");
        }

        @Override
        public void onWifiP2pEnabled(boolean isEnabled) {
            DLog.INSTANCE.logV(isEnabled ? "wifi p2p 可用" : "wifi p2p 不可用");
        }

        @Override
        public void onCreateGroup(boolean isSuccess) {
            toast(isSuccess ? "创建群组成功" : "创建群组失败");
        }

        @Override
        public void onRemoveGroup(boolean isSuccess) {
            toast(isSuccess ? "移除群组成功" : "移除群组失败");
        }

        @Override
        public void onConnectCallChanged(boolean isConnected) {
            String msg = isConnected ? "调用连接成功" : "调用连接失败";
            toast(msg);
            DLog.INSTANCE.logV(msg);
        }

        @Override
        public void onDisConnectCallChanged(boolean isDisConnected) {
            String msg = isDisConnected ? "断开连接成功" : "断开连接失败";
            toast(msg);
        }

        @Override
        public void onSelfDeviceAvailable(@NonNull WifiP2pDevice wifiP2pDevice) {
            String msg = "当前设备信息 " + wifiP2pDevice.deviceAddress;
//            toast(msg);
            DLog.INSTANCE.logV(msg);
        }

        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            if (wifiP2pInfo != null && !TextUtils.isEmpty(wifiP2pInfo.groupOwnerAddress.getHostAddress())) {
                String ip = wifiP2pInfo.groupOwnerAddress.getHostAddress();
                DLog.INSTANCE.logV("连接成功 - " + ip);
            } else {
                DLog.INSTANCE.logV("连接失败");
            }
        }

        @Override
        public void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList) {
            toast("发现设备数量 " + wifiP2pDeviceList.size());
            DLog.INSTANCE.logV("发现设备数量 " + wifiP2pDeviceList.size());
        }

        @Override
        public void onGroupInfoAvailable(WifiP2pGroup group) {

        }
    };
}
