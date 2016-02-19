package com.zhenmei.testandroidutils.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogUtils {

    /**
     * 是否需要打印bug，可以在application的onCreate函数里面初始化
     */
    public static boolean isDebug = true;

    /**
     * TAG字符串
     */
    private static final String TAG = "way";

    /**
     * 打印verbose日志
     */
    public static void verboseLog(String msg) {
        if (isDebug) {
            Log.v(TAG, msg);
        }
    }

    /**
     * 打印debug日志
     */
    public static void debugLog(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 打印info日志
     */
    public static void infoLog(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }

    /**
     * 打印warn日志
     */
    public static void warnLog(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }

    /**
     * 打印error日志
     */
    public static void errorLog(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    /**
     * 传入自定义tag，打印verbose日志
     */
    public static void verboseLog(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    /**
     * 传入自定义tag，打印debug日志
     */
    public static void debugLog(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    /**
     * 传入自定义tag，打印info日志
     */
    public static void infoLog(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    /**
     * 传入自定义tag，打印warn日志
     */
    public static void warnLog(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    /**
     * 传入自定义tag，打印error日志
     */
    public static void errorLog(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

}
