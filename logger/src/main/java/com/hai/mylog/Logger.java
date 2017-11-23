package com.hai.mylog;

/**
 * Created by 黄海 on 2017/11/22.
 */

public final class Logger {
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private static Printer printer = new LoggerPrinter();

    private Logger() {
    }

    public static void printer(Printer printer) {
        Logger.printer = printer;
    }

    public static void addLogAdapter(LogAdapter adapter) {
        printer.addAdapter(adapter);
    }

    public static void clearLogAdapters() {
        printer.clearLogAdapters();
    }

    /**
     * Given tag will be used as tag only once for this method call regardless of the tag that's been
     * set during initialization. After this invocation, the general tag that's been set will
     * be used for the subsequent log calls
     */
    public static Printer t(String tag) {
        return printer.t(tag);
    }

    public static void v(String tag, String msg, Throwable tr) {
        printer.v(tag, msg, tr);
    }

    public static void d(String msg) {
        printer.d(msg);
    }

    public static void d(String tag, String msg) {
        printer.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        printer.d(tag, msg, tr);
    }

    public static void d(Throwable tr) {
        printer.d(null, null, tr);
    }

    public static void i(String msg) {
        printer.i(msg);
    }

    public static void i(String tag, String msg) {
        printer.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        printer.i(tag, msg, tr);
    }

    public static void i(Throwable tr) {
        printer.i(null, null, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        printer.w(tag, msg, tr);
    }

    public static void e(String msg) {
        printer.e(msg);
    }

    public static void e(String tag, String msg) {
        printer.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        printer.e(tag, msg, tr);
    }

    public static void e(Throwable tr) {
        printer.e(null, null, tr);
    }

    public static void wtf(String msg) {
        printer.wtf(msg);
    }

    public static void wtf(String tag, String msg) {
        printer.wtf(tag, msg);
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        printer.wtf(tag, msg, tr);
    }

    public static void wtf(Throwable tr) {
        printer.wtf(null, null, tr);
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    public static void log(int priority, String tag, String message, Throwable throwable) {
        printer.log(priority, tag, message, throwable);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(String json) {
        printer.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(String xml) {
        printer.xml(xml);
    }
}
