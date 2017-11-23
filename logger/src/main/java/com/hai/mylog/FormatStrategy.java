package com.hai.mylog;

public interface FormatStrategy {

  void log(int priority, String tag, String message);
}
