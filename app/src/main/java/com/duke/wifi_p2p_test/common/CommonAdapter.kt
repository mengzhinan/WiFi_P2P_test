package com.duke.wifi_p2p_test.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duke.wifi_p2p_test.R

/**
 * @Author: duke
 * @DateTime: 2020-05-17 16:04
 * @Description:
 *
 */
open abstract class CommonAdapter<out E>(private val list: MutableList<E>) :
    RecyclerView.Adapter<CommonAdapter.CHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CHolder {
        return CHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
    }

    override fun getItemCount(): Int = list.size

    class CHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemText = itemView.findViewById<TextView>(R.id.item_text)
    }
}