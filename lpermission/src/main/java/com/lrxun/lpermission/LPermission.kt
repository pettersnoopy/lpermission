package com.lrxun.lpermission

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

const val LPERMISSION_TAG = "LPERMISSION"

interface PermissionCallback {
    fun onResult(permission: Permission)
}

class LPermission {
    companion object {
        fun with(activity: FragmentActivity): LPermission {
            return LPermission(activity)
        }

        fun with(fragment: Fragment): LPermission {
            return LPermission(fragment)
        }
    }

    private var permissionFragment: Lazy<LPermissionFragment>
    private constructor(activity: FragmentActivity) {
        permissionFragment = getLazySingleton(activity.supportFragmentManager)
    }

    private constructor(fragment: Fragment) {
        permissionFragment = getLazySingleton(fragment.childFragmentManager)
    }

    private fun getLazySingleton(fm: FragmentManager): Lazy<LPermissionFragment> {
        return object : Lazy<LPermissionFragment>{
            private var fragment: LPermissionFragment? = null
            @Synchronized
            override fun get(): LPermissionFragment {
                if (fragment == null) {
                    fragment = getPermissionsFragment(fm)
                }
                return fragment!!
            }
        }
    }

    private fun getPermissionsFragment(fm: FragmentManager): LPermissionFragment {
        var fragment = fm.findFragmentByTag(LPERMISSION_TAG)
        if (fragment == null) {
            fragment = LPermissionFragment()
            try {
                fm
                    .beginTransaction()
                    .add(fragment, LPERMISSION_TAG)
                    .commitNow()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return fragment as LPermissionFragment
    }

    fun request(vararg permissions: String): LPermission {
        if (permissions.isNullOrEmpty()) {
            throw IllegalArgumentException("LPermission requires at least one permission")
        }
        val unrequestedPermissions = ArrayList<String>()
        for (permission in permissions) {
            if (!isGranted(permission)) {
                unrequestedPermissions.add(permission)
            }
        }

        permissionFragment.get()
            .setRequestPermissions(unrequestedPermissions)
        return this
    }

    fun subscribe(callback: PermissionCallback) {
        permissionFragment.get()
            .requestPermissions(callback)
    }

    private fun isGranted(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissionFragment.get().isGranted(permission)
    }
}

private interface Lazy<T> {
    fun get(): T
}
