package com.duke.wifi_p2p_test.common

import android.content.Context
import android.widget.Toast

/**
 * @Author: duke
 * @DateTime: 2020-05-17 17:15
 * @Description:
 *
 */
object Util {

    fun toastPermission(context: Context) {
        Toast.makeText(context, "请手动开启 Wifi 相关权限", Toast.LENGTH_SHORT).show()
    }

    fun toast(context: Context, content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }
}