package com.duke.wifi_p2p_test.wifidemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duke.wifi_p2p_test.R

class WifiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)
        supportActionBar?.title = "Wifi 扫描"


    }
}
