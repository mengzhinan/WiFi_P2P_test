package com.duke.p2plib.activity;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duke.baselib.DLog;
import com.duke.p2plib.R;
import com.duke.p2plib.RandomColor;
import com.duke.p2plib.p2phelper.WifiP2PHelper;
import com.duke.p2plib.p2phelper.WifiP2PListener;
import com.duke.p2plib.sockethelper.ClientSocketHelper;
import com.duke.p2plib.sockethelper.SocketBase;

import java.util.Collection;

public class ClientSendActivity extends BaseActivity {
    public static final String TAG = ClientSendActivity.class.getSimpleName();

    private Button btnScanDevice;
    private Button btnSend;
    private EditText input;
    private TextView showContent;
    private ConstraintLayout root;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DeviceAdapter adapter;

    private ClientSocketHelper clientSocketHelper;
    private String serverIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_send);

        root = findViewById(R.id.root);
        btnScanDevice = findViewById(R.id.btn_scan_device);
        btnSend = findViewById(R.id.btn_send);
        input = findViewById(R.id.input);
        showContent = findViewById(R.id.show_content);

        recyclerView = findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new DeviceAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final WifiP2pDevice wifiP2pDevice) {
                WifiP2PHelper.getInstance(ClientSendActivity.this).cancelConnect();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WifiP2PHelper.getInstance(ClientSendActivity.this).connect(wifiP2pDevice);
                    }
                }, 1000);
            }
        });

        btnScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiP2PHelper.getInstance(ClientSendActivity.this).discover();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                clientSocketHelper.send(text + RandomColor.INSTANCE.makeRandomColor());
                input.setText("");
            }
        });

        clientSocketHelper = new ClientSocketHelper(new SocketBase.OnReceiveListener() {
            @Override
            public void onReceived(String text) {
                showContent.setText(text);
                root.setBackgroundColor(RandomColor.INSTANCE.parseRandomColorInt(text));
            }
        });
    }

    @Override
    protected void onDestroy() {
        // 断开连接
        WifiP2PHelper.getInstance(this).cancelConnect();
        WifiP2PHelper.getInstance(this).removeGroup();
        if (clientSocketHelper != null) {
            clientSocketHelper.clear();
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
            toast(isEnabled ? "wifi p2p 可用" : "wifi p2p 不可用");
        }

        @Override
        public void onCreateGroup(boolean isSuccess) {
            DLog.INSTANCE.logV(isSuccess ? "创建群组成功" : "创建群组失败");
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
            String msg = "自身设备信息 " + wifiP2pDevice.deviceAddress;
//            toast(msg);
            DLog.INSTANCE.logV(msg);
        }

        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            if (wifiP2pInfo != null
                    && wifiP2pInfo.groupFormed
                    && wifiP2pInfo.groupOwnerAddress != null
                    && wifiP2pInfo.groupOwnerAddress.getHostAddress() != null) {
                serverIP = wifiP2pInfo.groupOwnerAddress.getHostAddress();
                clientSocketHelper.postConnectToServer(serverIP);
                toast("连接成功 serverIP = " + serverIP);
                btnSend.setEnabled(true);
            } else {
                btnSend.setEnabled(false);
                serverIP = null;
                toast("连接失败");
            }
        }

        @Override
        public void onPeersAvailable(@NonNull Collection<WifiP2pDevice> wifiP2pDeviceList) {
            adapter.setWifiP2pDeviceList(wifiP2pDeviceList);
            int size = 0;
            if (wifiP2pDeviceList != null) {
                size = wifiP2pDeviceList.size();
            }
            toast("扫描到设备数量: " + size);
        }

        @Override
        public void onGroupInfoAvailable(WifiP2pGroup group) {
            // do nothing
        }
    };
}
