package org.telegram;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AutoStartHelper {

    public static void openAutoStart(Context context) {
        String manufacturer = Build.MANUFACTURER.toLowerCase();

        switch (manufacturer) {
            case "xiaomi":
                openMiui(context);
                break;

            case "huawei":
            case "honor":
                openHuawei(context);
                break;

            case "oppo":
                openOppo(context);
                break;

            case "vivo":
                openVivo(context);
                break;

            case "samsung":
                openSamsung(context);
                break;

            default:
                openDefault(context);
                break;
        }
    }

    private static void openMiui(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.autostart.AutoStartManagementActivity"
            ));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            openDefault(context);
        }
    }

    private static void openHuawei(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity"
            ));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            openDefault(context);
        }
    }

    private static void openOppo(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.coloros.safecenter",
                    "com.coloros.safecenter.startupapp.StartupAppListActivity"
            ));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            openDefault(context);
        }
    }

    private static void openVivo(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.vivo.permissionmanager",
                    "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
            ));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            openDefault(context);
        }
    }

    private static void openSamsung(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.samsung.android.lool",
                    "com.samsung.android.sm.ui.battery.BatteryActivity"
            ));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable e) {
            openDefault(context);
        }
    }

    private static void openDefault(Context context) {
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Throwable ignore) {}
    }
}
