package com.duke.baselib

import android.text.TextUtils
import android.util.Log

/**
 * @Author: duke
 * @DateTime: 2019-05-26 08:44
 * @Description:
 */
object DLog {
    const val TAG = "DLogFlag"
    const val ID_DEBUG = true
    fun logD(msg: String?) {
        if (!ID_DEBUG || TextUtils.isEmpty(msg)) {
            return
        }
        Log.d(TAG, msg)
    }

    fun logV(msg: String?) {
        if (!ID_DEBUG || TextUtils.isEmpty(msg)) {
            return
        }
        Log.v(TAG, msg)
    }

    fun logE(msg: String?) {
        if (!ID_DEBUG || TextUtils.isEmpty(msg)) {
            return
        }
        Log.e(TAG, msg)
    }
}