package com.duke.p2plib.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.duke.baselib.DPermission;
import com.duke.p2plib.R;

import java.util.ArrayList;

public class WifiP2PActivity extends AppCompatActivity {

    private String[] permissionArray = {Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Button btnServer;
    private Button btnClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_p2p);

        btnServer = findViewById(R.id.btn_server_receive);
        btnClient = findViewById(R.id.btn_client_send);

        btnServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WifiP2PActivity.this, ServerReceiveActivity.class));
            }
        });
        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WifiP2PActivity.this, ClientSendActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        DPermission.Companion.newInstance(this).setCallback(new DPermission.DCallback() {
            @Override
            public void onResult(ArrayList<DPermission.PermissionInfo> permissionInfoList) {

            }
        }).startRequest(permissionArray);
    }
}
