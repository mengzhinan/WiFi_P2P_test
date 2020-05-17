package com.duke.wifi_p2p_test.wifidemo

import android.net.wifi.ScanResult
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duke.wifi_p2p_test.R
import com.duke.wifi_p2p_test.common.CommonAdapter
import com.duke.wifi_p2p_test.common.Util
import com.duke.wifilib.WifiReceiver
import com.duke.wifilib.WifiUtil
import java.util.*

class WifiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var commonAdapter: CommonAdapter<ScanResult>? = null
    private var list = mutableListOf<ScanResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)
        supportActionBar?.title = "Wifi 扫描"

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        commonAdapter = object : CommonAdapter<ScanResult>(list) {
            override fun onBindViewHolder(holder: CHolder, position: Int) {
                list.get(position)?.run {
                    val text =
                        "名称：${this.SSID}，Mac 地址：${this.BSSID.toUpperCase(Locale.getDefault())}"
                    holder.itemText.text = text
                }
            }
        }
        recyclerView.adapter = commonAdapter

        WifiUtil.startScan(this)
    }

    override fun onResume() {
        super.onResume()
        WifiUtil.registerReceiver(this, object : WifiReceiver.OnScanCompletedListener {
            override fun onScanCompleted(isSuccess: Boolean) {
                val scanResult = WifiUtil.getScanResults(this@WifiActivity)
                if (scanResult == null || scanResult.isEmpty()) {
                    Util.toast(this@WifiActivity, "没有扫描到")
                    return
                }
                list.clear()
                list.addAll(scanResult)
                commonAdapter?.notifyDataSetChanged()
                Util.toast(this@WifiActivity, "扫描成功")
            }
        })
    }

    override fun onPause() {
        super.onPause()
        WifiUtil.unRegisterReceiver(this)
    }
}
