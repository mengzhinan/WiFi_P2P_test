package com.duke.wifi_p2p_test.awaredemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.duke.wifi_p2p_test.R

class AwareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aware)
        supportActionBar?.title = "网络感知"


    }
}
