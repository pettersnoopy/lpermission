package com.lrxun.lpermission

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.content.res.ResourcesCompat

class Permission {
    var name: String? = null
    var granted: Boolean = false
    var shouldShowRequestPermissionRationale: Boolean = false
    constructor(name: String, granted: Boolean): this(name, granted, false)
    constructor(name: String, granted: Boolean, shouldShowRequestPermissionRationale: Boolean) {
        this.name = name
        this.granted = granted
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale
    }
    constructor(permissions: List<Permission>) {
        name = combineName(permissions)
        granted = combineGranted(permissions)
        shouldShowRequestPermissionRationale = combineShouldShowRequestPermissionRationale(permissions)
    }

    private fun combineName(permissions: List<Permission>): String {
        return permissions.map { it.name }.reduce { acc, s -> "$acc,$s" }?:""
    }

    private fun combineGranted(permissions: List<Permission>): Boolean {
        return permissions.map { it.granted }.reduce { acc, b -> acc && b }?:false
    }

    private fun combineShouldShowRequestPermissionRationale(permissions: List<Permission>): Boolean {
        return permissions.map { it.shouldShowRequestPermissionRationale }.reduce { acc, b -> acc && b }?:false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val that: Permission = other as Permission
        if (granted != that.granted) {
            return false
        }
        if (shouldShowRequestPermissionRationale != that.shouldShowRequestPermissionRationale) {
            return false
        }
        return name.equals(that.name)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (granted!!) 1 else 0
        result = 31 * result + if (shouldShowRequestPermissionRationale) 1 else 0
        return result
    }

    override fun toString(): String {
        return "Permission{" +
            "name='" + name + '\'' +
            ", granted=" + granted +
            ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
            '}'
    }

    private fun mergeName(permissions: List<Permission>) : String {
        return permissions.map { permission -> permission.name?:"" }.reduce {acc, s -> "$acc,$s" }
    }

    private fun mergeGranted(permissions: List<Permission>) : Boolean {
        return permissions.map { permission -> permission.granted?:false }.reduce {acc, b -> acc && b }
    }

    private fun mergeShouldShowRequestPermissionRationale(permissions: List<Permission>) : Boolean {
        return permissions.map { it -> it.shouldShowRequestPermissionRationale?: false }.reduce { acc, b -> acc && b }
    }

    companion object {
        fun transformPermissionText(context: Context, permissions: List<String>): List<String> {
            val ret = ArrayList<String>()
            for (permission in permissions) {
                val resId = when (permission) {
                    Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR -> R.string.lpermission__name_calendar
                    Manifest.permission.CAMERA -> R.string.lpermission__name_camera
                    Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS -> R.string.lpermission__name_contacts
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION -> R.string.lpermission__name_location
                    Manifest.permission.RECORD_AUDIO -> R.string.lpermission__name_microphone
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
                        Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP,
                        Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.ANSWER_PHONE_CALLS -> R.string.lpermission__name_phone
                    Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.KILL_BACKGROUND_PROCESSES
                        -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) R.string.lpermission__name_call_log else R.string.lpermission__name_phone
                    Manifest.permission.BODY_SENSORS -> R.string.lpermission__name_sensors
                    Manifest.permission.ACTIVITY_RECOGNITION -> R.string.lpermission__name_activity_recognition
                    Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_WAP_PUSH
                        -> R.string.lpermission__name_sms
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE -> R.string.lpermission__name_storage
                    else -> 0
                }
                val resName = context.resources.getString(resId)
                if (!ret.contains(resName)) {
                    ret.add(resName)
                }
            }
            return ret
        }
    }
}
