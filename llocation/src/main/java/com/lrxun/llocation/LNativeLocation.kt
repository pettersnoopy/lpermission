package com.lrxun.llocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

interface InvalidConditionCallback {
    fun onFailed()
}

interface InvalidContextCallback {
    fun onFailed()
}

interface NativeLocationCallback {
    fun onLocation(location: Location)
}

class LNativeLocation {
    companion object {
        fun with(context: Context): LNativeLocation {
            return LNativeLocation(context)
        }
    }

    private var invalidConditionCallback: InvalidConditionCallback? = null
    private var invalidContextCallback: InvalidContextCallback? = null
    private var context: Context? = null
    private constructor(context: Context) {
        this.context = context
    }

    private var locationMgr: LocationManager? = null
    private fun getLocationManager(): LocationManager {
        if (locationMgr == null) {
            locationMgr = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        return locationMgr!!
    }

    fun setInvalidConditionCallback(callback: InvalidConditionCallback): LNativeLocation {
        this.invalidConditionCallback = callback
        return this
    }

    fun setInvalidContextCallback(callback: InvalidContextCallback): LNativeLocation {
        this.invalidContextCallback = callback
        return this
    }

    @SuppressLint("MissingPermission")
    fun requestLocation(callback: NativeLocationCallback) {
        if (context == null) {
            invalidContextCallback?.onFailed()
            return
        }
        if (!LocationManagerCompat.isLocationEnabled(getLocationManager())) {
            invalidConditionCallback?.onFailed()
            return
        }

        val provider = if (getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
        val location = getLocationManager().getLastKnownLocation(provider)
        if (location != null) {
            callback.onLocation(location)
            return
        }

        getLocationManager().requestLocationUpdates(provider, 3000, 1f, object : LocationListener {
            override fun onLocationChanged(p0: Location) {
                callback.onLocation(p0)
                getLocationManager().removeUpdates(this)
            }
        })
    }
}