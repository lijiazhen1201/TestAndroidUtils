package com.zhenmei.testandroidutils.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 手机应用工具类
 */
public class AppUtils {

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AppInfo
     */
    public static class AppInfo {

        public AppInfo() {
        }

        private CharSequence appLable;
        private Drawable appIcon;
        private String appPackage;
        private String appClass;

        public CharSequence getAppLable() {
            return appLable;
        }

        public void setAppLable(CharSequence appLable) {
            this.appLable = appLable;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public void setAppIcon(Drawable appIcon) {
            this.appIcon = appIcon;
        }

        public String getAppPackage() {
            return appPackage;
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        public String getAppClass() {
            return appClass;
        }

        public void setAppClass(String appClass) {
            this.appClass = appClass;
        }
    }

    /**
     * 获取系统所有APP应用
     */
    public static ArrayList<AppInfo> getAllApp(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        for (ResolveInfo info : apps) {
            AppInfo appInfo = new AppInfo();

            appInfo.setAppLable(info.loadLabel(manager) + "");
            appInfo.setAppIcon(info.loadIcon(manager));
            appInfo.setAppPackage(info.activityInfo.packageName);
            appInfo.setAppClass(info.activityInfo.name);
            appList.add(appInfo);
            System.out.println("info.activityInfo.packageName=" + info.activityInfo.packageName);
            System.out.println("info.activityInfo.name=" + info.activityInfo.name);
        }
        return appList;
    }

    /**
     * 获取用户安装的APP应用
     */
    public static ArrayList<AppInfo> getUserApp(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        for (ResolveInfo info : apps) {
            AppInfo appInfo = new AppInfo();
            ApplicationInfo ainfo = info.activityInfo.applicationInfo;
            if ((ainfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                appInfo.setAppLable(info.loadLabel(manager) + "");
                appInfo.setAppIcon(info.loadIcon(manager));
                appInfo.setAppPackage(info.activityInfo.packageName);
                appInfo.setAppClass(info.activityInfo.name);
                appList.add(appInfo);
            }
        }
        return appList;
    }

    /**
     * 根据包名和Activity启动类查询应用信息
     */
    public static AppInfo getAppByClsPkg(Context context, String pkg, String cls) {
        AppInfo appInfo = new AppInfo();

        PackageManager pm = context.getPackageManager();
        Drawable icon;
        CharSequence label = "";
        ComponentName comp = new ComponentName(pkg, cls);
        try {
            ActivityInfo info = pm.getActivityInfo(comp, 0);
            icon = pm.getApplicationIcon(info.applicationInfo);
            label = pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0));
        } catch (PackageManager.NameNotFoundException e) {
            icon = pm.getDefaultActivityIcon();
        }
        appInfo.setAppClass(cls);
        appInfo.setAppIcon(icon);
        appInfo.setAppLable(label + "");
        appInfo.setAppPackage(pkg);

        return appInfo;
    }

    /**
     * 调节系统音量
     */
    public static void holdSystemAudio(Context context) {
        AudioManager audiomanage = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        // int maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量
        // int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_RING);
        // 设置音量
        // audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM, currentVolume, AudioManager.FLAG_PLAY_SOUND);

        // 调节音量
        // ADJUST_RAISE 增大音量，与音量键功能相同
        // ADJUST_LOWER 降低音量
        audiomanage.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

    }

    /**
     * 设置亮度（每30递增）
     */
    public static void setBrightness(Activity activity) {
        ContentResolver resolver = activity.getContentResolver();
        Uri uri = android.provider.Settings.System
                .getUriFor("screen_brightness");
        int nowScreenBri = getScreenBrightness(activity);
        nowScreenBri = nowScreenBri <= 225 ? nowScreenBri + 30 : 30;
        System.out.println("nowScreenBri==" + nowScreenBri);
        android.provider.Settings.System.putInt(resolver, "screen_brightness",
                nowScreenBri);
        resolver.notifyChange(uri, null);
    }

    /**
     * 获取屏幕的亮度
     */
    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 跳转到系统设置
     */
    public static void intentSetting(Context context) {
        String pkg = "com.android.settings";
        String cls = "com.android.settings.Settings";

        ComponentName component = new ComponentName(pkg, cls);
        Intent intent = new Intent();
        intent.setComponent(component);

        context.startActivity(intent);
    }


}
