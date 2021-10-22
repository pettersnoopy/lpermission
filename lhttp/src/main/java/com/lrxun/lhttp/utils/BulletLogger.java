package com.lrxun.lhttp.utils;

import android.util.Log;

/**
 * Created by luopeng on 2019-11-30.
 * from Qidianyun company
 */
public class BulletLogger {
    private static boolean mIsLogEnabled = true;

    private static String tag = "BulletHttp";

    public static void debug(boolean isEnable) {
        debug(tag, isEnable);
    }

    public static void debug(String logTag, boolean isEnable) {
        tag = logTag;
        mIsLogEnabled = isEnable;
    }

    public static void v(String msg) {
        v(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (mIsLogEnabled) {
            Log.v(tag, msg);
        }
    }

    public static void d(String msg) {
        d(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (mIsLogEnabled) {
            Log.d(tag, msg);
        }
    }

    public static void i(String msg) {
        i(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (mIsLogEnabled) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        w(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (mIsLogEnabled) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg) {
        e(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (mIsLogEnabled) {
            Log.e(tag, msg);
        }
    }

    public static void printStackTrace(Throwable t) {
        if (mIsLogEnabled && t != null) {
            t.printStackTrace();
        }
    }
}
