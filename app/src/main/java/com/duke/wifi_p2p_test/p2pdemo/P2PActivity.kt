package com.duke.wifi_p2p_test.p2pdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.duke.wifi_p2p_test.R

class P2PActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2_p)
        supportActionBar?.title = "Wifi P2P"

    }
}
