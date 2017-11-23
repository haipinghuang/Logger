package com.hai.mylog;

import android.content.Context;

/**
 *
 */
public class DiskLogAdapter implements LogAdapter {
    private final FormatStrategy formatStrategy;

    public DiskLogAdapter(Context context) {
        formatStrategy = TxtFormatStrategy.newBuilder(context.getApplicationContext()).build();
    }

    public DiskLogAdapter(FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return true;
    }

    @Override
    public void log(int priority, String tag, String message) {
        formatStrategy.log(priority, tag, message);
    }
}