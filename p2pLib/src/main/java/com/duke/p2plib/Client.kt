package com.duke.p2plib

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

/**
 * author: duke
 * version: 1.0
 * dateTime: 2020-05-15 19:30
 * description:
 */
class Client(private val context: Context) {

    private var myServiceName:String?=null
    private var nsdManager:NsdManager?=null

    init {
        nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager?
    }

    fun registerService(context: Context, port: Int) {
        // Create the NsdServiceInfo object, and populate it.
        val serviceInfo = NsdServiceInfo().apply {
            // 对外公开的全网唯一的服务名称
            serviceName = "NsdChat"
            // 第二个参数设置服务类型，指定应用使用的协议和传输层。语法为“_<protocol>._<transportlayer>”。
            // 在代码段中，该服务使用通过 TCP 运行的 HTTP 协议。
            // 提供打印机服务（例如网络打印机）的应用会将服务类型设置为“_ipp._tcp”。
            serviceType = "_nsdchat._tcp"
            setPort(port)
        }

        nsdManager.apply {
            this?.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
        }
    }


    private val registrationListener = object : NsdManager.RegistrationListener {

        override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
            // Save the service name. Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            myServiceName = NsdServiceInfo.serviceName
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Registration failed! Put debugging code here to determine why.
        }

        override fun onServiceUnregistered(arg0: NsdServiceInfo) {
            // Service has been unregistered. This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Unregistration failed. Put debugging code here to determine why.
        }
    }
}