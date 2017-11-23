package com.hai.mylog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import static com.hai.mylog.Logger.ASSERT;
import static com.hai.mylog.Logger.DEBUG;
import static com.hai.mylog.Logger.ERROR;
import static com.hai.mylog.Logger.INFO;
import static com.hai.mylog.Logger.VERBOSE;
import static com.hai.mylog.Logger.WARN;

/**
 * Created by 黄海 on 2017/11/22.
 */

public class LoggerPrinter implements Printer {
    /**
     * It is used for json pretty print
     */
    private static final int JSON_INDENT = 2;
    /**
     * Provides one-time used tag for the log message
     */
    private final ThreadLocal<String> localTag = new ThreadLocal<>();

    private final List<LogAdapter> logAdapters = new ArrayList<>();

    @Override
    public void addAdapter(LogAdapter adapter) {
        logAdapters.add(adapter);
    }

    @Override
    public Printer t(String tag) {
        if (tag != null) {
            localTag.set(tag);
        }
        return this;
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        t(tag);
        log(VERBOSE, msg, tr);
    }

    @Override
    public void d(String msg) {
        log(DEBUG, msg, null);
    }

    @Override
    public void d(String tag, String msg) {
        t(tag);
        log(DEBUG, msg, null);
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        t(tag);
        log(DEBUG, msg, tr);
    }

    @Override
    public void d(Throwable tr) {
        log(DEBUG, null, tr);
    }

    @Override
    public void i(String msg) {
        log(INFO, msg, null);
    }

    @Override
    public void i(String tag, String msg) {
        t(tag);
        log(INFO, msg, null);
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        t(tag);
        log(INFO, msg, tr);
    }

    @Override
    public void i(Throwable tr) {
        log(INFO, null, tr);
    }

    @Override
    public void w(String tag, String msg, Throwable tr) {
        t(tag);
        log(WARN, msg, tr);
    }

    @Override
    public void e(String msg) {
        log(ERROR, msg, null);
    }

    @Override
    public void e(String tag, String msg) {
        t(tag);
        log(ERROR, msg, null);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        t(tag);
        log(ERROR, msg, tr);
    }

    @Override
    public void e(Throwable tr) {
        log(ERROR, null, tr);
    }

    @Override
    public void wtf(String msg) {
        log(ASSERT, msg, null);
    }

    @Override
    public void wtf(String tag, String msg) {
        t(tag);
        log(ASSERT, msg, null);
    }

    @Override
    public void wtf(String tag, String msg, Throwable tr) {
        t(tag);
        log(ASSERT, msg, tr);
    }

    @Override
    public void wtf(Throwable tr) {
        log(ASSERT, null, tr);
    }

    @Override
    public void json(String json) {
        if (Utils.isEmpty(json)) {
            i("Empty/Null json content");
            return;
        }
        try {
            json = json.trim();
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String message = jsonObject.toString(JSON_INDENT);
                i(message);
                return;
            }
            if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String message = jsonArray.toString(JSON_INDENT);
                i(message);
                return;
            }
            e("Invalid Json");
        } catch (JSONException e) {
            e("Invalid Json");
        }
    }

    @Override
    public void xml(String xml) {
        if (Utils.isEmpty(xml)) {
            i("Empty/Null xml content");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            i(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            i("Invalid xml");
        }
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private synchronized void log(int priority, String msg, Throwable throwable) {
        String tag = getTag();
        log(priority, tag, msg, throwable);
    }

    @Override
    public void log(int priority, String tag, String message, Throwable throwable) {
        if (throwable != null && message != null) {
            message += '\n' + Utils.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = Utils.getStackTraceString(throwable);
        }
        if (Utils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }
        for (LogAdapter adapter : logAdapters) {
            if (adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, message);
            }
        }
    }

    @Override
    public void clearLogAdapters() {
        logAdapters.clear();
    }

    /**
     * @return the appropriate tag based on local or global
     */
    private String getTag() {
        String tag = localTag.get();
        if (tag != null) {
            localTag.remove();
            return tag;
        }
        return null;
    }
}
