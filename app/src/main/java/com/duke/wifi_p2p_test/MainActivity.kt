package com.duke.wifi_p2p_test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.duke.wifi_p2p_test.awaredemo.AwareActivity
import com.duke.wifi_p2p_test.p2pdemo.P2PActivity
import com.duke.wifi_p2p_test.wifidemo.WifiActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.wifi_aware).setOnClickListener {
            startActivity(Intent(MainActivity@ this, AwareActivity::class.java))
        }
        findViewById<Button>(R.id.wifi_p2p).setOnClickListener {
            startActivity(Intent(this, P2PActivity::class.java))
        }
        findViewById<Button>(R.id.wifi_scan).setOnClickListener {
            startActivity(Intent(this@MainActivity, WifiActivity::class.java))
        }
    }
}
