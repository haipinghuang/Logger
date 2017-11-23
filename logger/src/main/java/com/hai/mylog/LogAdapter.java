package com.hai.mylog;
/**
 * Created by 黄海 on 2017/11/22.
 */
public interface LogAdapter {

    boolean isLoggable(int priority, String tag);

    void log(int priority, String tag, String message);
}