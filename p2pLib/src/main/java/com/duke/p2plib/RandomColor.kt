package com.duke.p2plib

import android.graphics.Color
import android.text.TextUtils
import kotlin.random.Random

/**
 * @Author: duke
 * @DateTime: 2020-06-15 23:06
 * @Description:
 *
 */
object RandomColor {
    private const val SPLIT = "---"

    private val random = Random(16)
    private val stringArray =
        arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F")

    fun makeRandomColor(): String {
        var color = "$SPLIT#"
        for (i in 0..5) {
            color += stringArray[random.nextInt(stringArray.size)]
        }
        return color
    }

    fun parseRandomColorInt(msg: String?): Int {
        if (TextUtils.isEmpty(msg)) {
            return Color.WHITE
        }
        val arr = msg!!.split(SPLIT)
        if (arr.size != 2) {
            return Color.WHITE
        }
        return try {
            Color.parseColor(arr[1])
        } catch (e: Exception) {
            Color.WHITE
        }
    }
}
