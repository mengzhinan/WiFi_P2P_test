package com.duke.baselib

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.lang.ref.WeakReference
import java.util.*

/**
 * @Author: duke
 * @DateTime: 2019-03-12 13:33
 * @Description: 请求权限代理类
 */
class DPermission private constructor(fragmentManager: FragmentManager) {
    private val mFragmentWeakReference: WeakReference<DFragment?>?
    private var mDCallback: DCallback? = null
    fun setCallback(dCallback: DCallback?): DPermission {
        mDCallback = dCallback
        val fragment = currentFragment
        fragment?.mDCallback = dCallback
        return this
    }

    private val currentFragment: DFragment?
        private get() = if (mFragmentWeakReference?.get() != null &&
            mFragmentWeakReference.get()!!.isAdded
        ) {
            mFragmentWeakReference.get()
        } else null

    /**
     * 过滤无效的权限
     *
     * @param permissions 用户设置的权限
     * @return 去除重复、无效后的权限
     */
    private fun filterPermissions(permissions: Array<String?>?): Array<String?>? {
        if (permissions == null || permissions.isEmpty()) {
            return null
        }
        val set = HashSet<String?>()
        for (permission in permissions) {
            if (isEmpty(permission)) {
                continue
            }
            set.add(permission)
        }
        val permissionResult = arrayOfNulls<String>(set.size)
        val iterator: Iterator<String?> = set.iterator()
        var index = 0
        while (iterator.hasNext()) {
            val p = iterator.next()
            if (isEmpty(p)) {
                continue
            }
            permissionResult[index] = p
            index++
        }
        return permissionResult
    }

    private fun isEmpty(str: String?): Boolean {
        return TextUtils.isEmpty(str)
    }

    /**
     * 上层请求权限入口方法
     *
     * @param permissions 待申请的权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun startRequest(permissions: Array<String?>?) {
        // 过滤
        var permissions = permissions
        permissions = filterPermissions(permissions)!!
        if (permissions == null || permissions.isEmpty()) {
            return
        }
        val notRequestList = ArrayList<PermissionInfo>(permissions.size)
        val needRequestList = ArrayList<String?>(permissions.size)
        for (permission in permissions) {
            if (isEmpty(permission)) {
                continue
            }
            if (isGranted(permission)) {
                // isGranted = true
                notRequestList.add(
                    PermissionInfo(
                        permission,
                        true,
                        false
                    )
                )
                continue
            }
            if (isRevoked(permission)) {
                // isGranted = false
                notRequestList.add(
                    PermissionInfo(
                        permission,
                        false,
                        false
                    )
                )
                continue
            }
            needRequestList.add(permission)
        }

        //部分已经允许的权限，也可以在此提前返回
        if (!needRequestList.isEmpty()) {
            // 请求权限(有为允许且可能被用户允许的权限，需要向系统申请)
            // 由底层一层返回结果
            requestPermissionsFromFragment(
                notRequestList,
                needRequestList.toTypedArray()
            )
        } else {
            // 已经全部是不允许或者是被系统策略拒绝的权限
            // 就算调用 requestPermissions(string, int)，系统也不会回调 onRequestPermissionsResult() 函数
            // 此情况，只好自己向上层返回
            if (mDCallback != null && notRequestList.isNotEmpty()) {
                mDCallback!!.onResult(notRequestList)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun requestPermissionsFromFragment(
        notRequestList: ArrayList<PermissionInfo>,
        permissions: Array<String?>
    ) {
        val fragment = currentFragment ?: return
        // 保存已经允许或者系统拒绝的权限，底层一起返回
        fragment.notRequestList = notRequestList
        // 底层请求权限
        fragment.requestPermissions(permissions)
    }

    /**
     * 如果已经授权，则返回true。<br></br>
     * 如果 SDK < 23，则永远返回true。
     */
    private fun isGranted(permission: String?): Boolean {
        val fragment = currentFragment
        return !isMarshmallow ||
                fragment != null && fragment.isGranted(permission)
    }

    /**
     * 如果权限已被策略撤销，则返回true。<br></br>
     * 如果 SDK < 23 ，则永远返回false。
     */
    private fun isRevoked(permission: String?): Boolean {
        val fragment = currentFragment
        return isMarshmallow && fragment != null && fragment.isRevoked(permission)
    }

    /**
     * 是否是 >= 23
     *
     * @return 是否需要动态权限适配
     */
    private val isMarshmallow: Boolean
        private get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    //============================================================
    /**
     * @Author: duke
     * @DateTime: 2019-03-11 15:12
     * @Description: 请求权限fragment
     */
    class DFragment : Fragment() {
        var notRequestList: ArrayList<PermissionInfo>? = null
        var mDCallback: DCallback? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            retainInstance = true
        }

        @TargetApi(Build.VERSION_CODES.M)
        fun requestPermissions(permissions: Array<String?>) {
            // 底层请求权限的方法
            requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            // 系统权限回调
            if (requestCode != PERMISSIONS_REQUEST_CODE) {
                return
            }
            var permissionResultList = ArrayList<PermissionInfo>(permissions.size)
            for (permission in permissions) {
                permissionResultList.add(
                    PermissionInfo(
                        permission,
                        isGranted(permission),
                        shouldShowRequestPermissionRationale(permission)
                    )
                )
                // 如果想了解跟多该方法的含义，请查看 PermissionInfo 类对应属性说明
                // shouldShowRequestPermissionRationale()
            }
            if (notRequestList?.size ?: 0 > 0) {
                // 累加上层已经确定的权限
                permissionResultList.addAll(notRequestList!!)
            }
            if (mDCallback != null && permissionResultList.isNotEmpty()) {
                // 返回外层回调
                mDCallback?.onResult(permissionResultList)
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        fun isGranted(permission: String?): Boolean {
            val fragmentActivity = activity
                ?: throw NullPointerException("Exception caused by fragment detached from activity.")
            return fragmentActivity.checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
        }

        @TargetApi(Build.VERSION_CODES.M)
        fun isRevoked(permission: String?): Boolean {
            val fragmentActivity = activity
                ?: throw NullPointerException("Exception caused by fragment detached from activity.")
            if (fragmentActivity.packageManager == null) {
                throw NullPointerException("Exception caused by activity.getPackageManager() == null.")
            }
            if (TextUtils.isEmpty(activity!!.packageName)) {
                throw NullPointerException("Exception caused by activity.getPackageName() == null.")
            }
            return fragmentActivity.packageManager
                .isPermissionRevokedByPolicy(permission!!, activity!!.packageName)
        }

        companion object {
            private const val PERMISSIONS_REQUEST_CODE = 1111
        }
    }
    //============================================================
    /**
     * @Author: duke
     * @DateTime: 2019-03-11 15:10
     * @Description: 权限bean
     */
    class PermissionInfo(
        // 权限名称
        var name: String?,
        // 是否授权
        var isGranted: Boolean,
        /**
         * 是否需要向用户解释此权限功能。<br></br>
         * 官网说明：https://developer.android.google.cn/training/permissions/requesting.html <br></br>
         *
         *
         * 返回 true 情况： <br></br>
         * 当用户 仅仅只是 拒绝 某项权限时，此方法返回 true。but，看 false 情况。 <br></br>
         *
         *
         * 注意，返回 false 情况： <br></br>
         * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。 <br></br>
         * 如果设备规范禁止应用具有该权限，此方法也会返回 false。 <br></br>
         */
        var isShouldShowRequestPermissionRationale: Boolean
    ) {

        override fun equals(o: Any?): Boolean {
            if (this === o) {
                return true
            }
            if (o !is PermissionInfo
                || javaClass != o.javaClass
            ) {
                return false
            }
            val that =
                o
            return if (name != that.name
                || isGranted != that.isGranted || isShouldShowRequestPermissionRationale != that.isShouldShowRequestPermissionRationale
            ) {
                false
            } else true
        }

    }
    //============================================================
    /**
     * @Author: duke
     * @DateTime: 2019-03-11 15:02
     * @Description: 权限请求回调
     */
    interface DCallback {
        fun onResult(permissionInfoList: ArrayList<PermissionInfo>?)
    }

    companion object {
        private val TAG_FRAGMENT =
            DPermission::class.java.name.hashCode().toString()

        fun newInstance(activity: FragmentActivity): DPermission {
            return DPermission(activity.supportFragmentManager)
        }

        fun newInstance(fragment: Fragment): DPermission {
            return DPermission(fragment.childFragmentManager)
        }
    }

    init {
        var dFragment =
            fragmentManager.findFragmentByTag(TAG_FRAGMENT) as DFragment?
        if (dFragment == null) {
            dFragment = DFragment()
            fragmentManager
                .beginTransaction()
                .add(dFragment, TAG_FRAGMENT)
                .commitNow()
        }
        mFragmentWeakReference = WeakReference(dFragment)
    }
}