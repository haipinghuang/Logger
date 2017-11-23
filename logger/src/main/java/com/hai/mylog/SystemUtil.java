package com.hai.mylog;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by 黄海 on 2017/11/22.
 */

public class SystemUtil {
    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号,如6.0.1
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * SDK 版本号
     *
     * @return
     */
    public static String getSystemSDK_INT() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * 系统版本name
     *
     * @return
     */
    public static String getOsName() {
        String name = "Android " + getSystemVersion() + ",Level " + getSystemSDK_INT();
        return name;
    }

    /**
     * 获取手机型号，如OPPO A57
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备cpu架构
     *
     * @return 手机厂商
     */
    public static String getDeviceCPU_ABI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_ABIS[0].toString();
        } else {
            return Build.CPU_ABI;
        }
    }

    /**
     * 通过反射获取所有的字段信息
     *
     * @return
     */
    public String getDeviceInfo() {
        StringBuilder sbBuilder = new StringBuilder();
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                sbBuilder.append("\n" + field.getName() + "=" + field.get(null).toString());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sbBuilder.toString();
    }
}
