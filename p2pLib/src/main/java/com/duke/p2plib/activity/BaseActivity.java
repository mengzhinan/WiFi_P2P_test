package com.duke.p2plib.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.duke.baselib.DPermission;
import com.duke.p2plib.p2phelper.WifiP2PHelper;
import com.duke.p2plib.p2phelper.WifiP2PListener;

import java.util.ArrayList;

/**
 * @Author: duke
 * @DateTime: 2019-05-25 18:14
 * @Description:
 */
public abstract class BaseActivity extends AppCompatActivity {

    private String[] permissionArray = {Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DPermission.Companion.newInstance(this).setCallback(new DPermission.DCallback() {
            @Override
            public void onResult(ArrayList<DPermission.PermissionInfo> permissionInfoList) {
                WifiP2PHelper.getInstance(BaseActivity.this)
                        .setListener(getListener())
                        .registerReceiver();
            }
        }).startRequest(permissionArray);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WifiP2PHelper.getInstance(this).unRegisterReceiver();
    }

    protected abstract WifiP2PListener getListener();

    protected final void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
