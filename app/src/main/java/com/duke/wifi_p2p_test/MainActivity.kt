package com.duke.wifi_p2p_test

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.duke.baselib.DPermission
import com.duke.p2plib.activity.WifiP2PActivity
import com.duke.wifi_p2p_test.awaredemo.AwareActivity
import com.duke.wifi_p2p_test.wifidemo.WifiActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val permissionArray: Array<String?>? = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.CHANGE_NETWORK_STATE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.wifi_aware).setOnClickListener {
            requestPermission()
            startActivity(Intent(MainActivity@ this, AwareActivity::class.java))
        }
        findViewById<Button>(R.id.wifi_p2p).setOnClickListener {
            requestPermission()
            startActivity(Intent(this, WifiP2PActivity::class.java))
        }
        findViewById<Button>(R.id.wifi_scan).setOnClickListener {
            requestPermission()
            startActivity(Intent(this@MainActivity, WifiActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }

    private fun requestPermission() {
        DPermission.newInstance(this).setCallback(object : DPermission.DCallback {
            override fun onResult(permissionInfoList: ArrayList<DPermission.PermissionInfo>?) {

            }
        }).startRequest(permissionArray)
    }
}
