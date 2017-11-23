package com.hai.mylog;

public interface Printer {

    void addAdapter(LogAdapter adapter);

    Printer t(String tag);

    void v(String tag, String msg, Throwable tr);

    void d(String msg);

    void d(String tag, String msg);

    void d(String tag, String msg, Throwable tr);

    void d(Throwable tr);

    void i(String msg);

    void i(String tag, String msg);

    void i(String tag, String msg, Throwable tr);

    void i(Throwable tr);
    void w(String tag, String msg, Throwable tr);

    void e(String msg);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable tr);

    void e(Throwable tr);

    void wtf(String msg);

    void wtf(String tag, String msg);

    void wtf(String tag, String msg, Throwable tr);

    void wtf(Throwable tr);

    /**
     * Formats the given json content and print it
     */
    void json(String json);

    /**
     * Formats the given xml content and print it
     */
    void xml(String xml);

    void log(int priority, String tag, String message, Throwable throwable);

    void clearLogAdapters();
}
