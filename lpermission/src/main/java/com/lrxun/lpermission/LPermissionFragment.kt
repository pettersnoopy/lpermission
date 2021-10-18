package com.lrxun.lpermission

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment

class LPermissionFragment: Fragment() {
    private val permissionRequestCode = 1032
    private val permissionsWaitRequest: MutableList<String> = ArrayList()
    private var permissionCallback: PermissionCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun setRequestPermissions(permissions: List<String>): LPermissionFragment {
        permissionsWaitRequest.addAll(permissions)
        return this
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissions(permissionCallback: PermissionCallback) {
        if (permissionsWaitRequest.isEmpty()) {
            permissionCallback.onResult(Permission("", granted = true, false))
            return
        }
        this.permissionCallback = permissionCallback
        requestPermissions(permissionsWaitRequest.toTypedArray(), permissionRequestCode)
//        permissionsWaitRequest.clear()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != permissionRequestCode) {
            return
        }

        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (permission in permissions) {
            shouldShowRequestPermissionRationale[permissions.indexOf(permission)] = shouldShowRequestPermissionRationale(permission)
        }

        onRequestPermissionsResult1(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    private fun onRequestPermissionsResult1(permissions: Array<out String>, grantResults: IntArray, shouldShowRequestPermissionRationale: BooleanArray) {
        val permissionRetList = permissions.mapIndexed { idx, permission -> Permission(permission, grantResults[idx] == PackageManager.PERMISSION_GRANTED, shouldShowRequestPermissionRationale[idx]) }
        permissionCallback?.onResult(Permission(permissionRetList))
        permissionCallback = null
    }

    fun isGranted(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun isRevoked(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().packageManager.isPermissionRevokedByPolicy(permission, requireActivity().packageName)
        } else {
            true
        }
    }
}
