package com.hai.mylog;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.hai.mylog.Logger.ASSERT;
import static com.hai.mylog.Logger.DEBUG;
import static com.hai.mylog.Logger.ERROR;
import static com.hai.mylog.Logger.INFO;
import static com.hai.mylog.Logger.VERBOSE;
import static com.hai.mylog.Logger.WARN;

/**
 * priority 大于INFO时，包含一些app信息 和手机系统信息
 * Created by 黄海 on 2017/11/22.
 */

public class TxtFormatStrategy implements FormatStrategy {
    private static final String SEPARATOR = " ";
    private static final String NEW_LINE = System.getProperty("line.separator");

    private final Date date;
    private final SimpleDateFormat dateFormat;
    private final LogStrategy logStrategy;
    private final String tag;
    private final Map<String, String> systemInfo;

    private TxtFormatStrategy(Builder builder) {
        date = builder.date;
        dateFormat = builder.dateFormat;
        logStrategy = builder.logStrategy;
        tag = builder.tag;
        systemInfo = builder.systemInfo;
    }

    @Override
    public void log(int priority, String tag, String message) {
        String tTag = formatTag(tag);
        date.setTime(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder();

        builder.append(NEW_LINE);
        if (priority > INFO) {
            appendSystemInfo(builder, systemInfo);
        }
        // human-readable date/time
        builder.append(dateFormat.format(date));
        // level
        builder.append(SEPARATOR);
        builder.append(Utils.logLevel(priority));
        // tag
        builder.append("/");
        builder.append(tTag);
        builder.append("：  ");

        builder.append(SEPARATOR);
        builder.append(message);

        // new line
        builder.append(NEW_LINE);

        logStrategy.log(priority, tTag, builder.toString());

    }

    private void appendSystemInfo(StringBuilder builder, Map<String, String> systemInfo) {
        if (systemInfo != null && !systemInfo.isEmpty()) {
            for (Map.Entry<String, String> entry : systemInfo.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                builder.append(NEW_LINE);
            }
        }
    }


    private String formatTag(String tag) {
        if (!Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)) {
            return tag;
        }
        return this.tag;
    }

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public static final class Builder {
        LogStrategy logStrategy;
        String tag = "PRETTY_LOGGER";
        Date date;
        SimpleDateFormat dateFormat;
        Context context;
        Map<String, String> systemInfo;

        private Builder(Context context) {
            this.context = context;
        }

        public Builder setLogStrategy(LogStrategy logStrategy) {
            this.logStrategy = logStrategy;
            return this;
        }

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setSystemInfo(Map<String, String> systemInfo) {
            this.systemInfo = systemInfo;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setDateFormat(SimpleDateFormat dateFormat) {
            this.dateFormat = dateFormat;
            return this;
        }

        public TxtFormatStrategy build() {
            if (date == null) {
                date = new Date();
            }
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
            }
            if (systemInfo == null) {
                systemInfo = new HashMap<>();
                systemInfo.put("设备机型", SystemUtil.getSystemModel());
                systemInfo.put("系统版本", SystemUtil.getOsName());
//                systemInfo.put("ROM", SystemUtil.getSystemModel());
                systemInfo.put("CPU架构", SystemUtil.getDeviceCPU_ABI());
                systemInfo.put("App版本", Utils.getVersionName(context));
            }
            if (logStrategy == null) {
                File cacheDir = context.getExternalCacheDir();
                HandlerThread handlerThread = new HandlerThread("AndroidFileLogger");
                handlerThread.start();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Handler handler = new DiskLogStrategy.WriteHandler(handlerThread.getLooper(), cacheDir, sdf.format(date) + "-log.txt");
                logStrategy = new DiskLogStrategy(handler);
            }
            return new TxtFormatStrategy(this);
        }
    }
}
