package com.hai.mylog;

public interface LogStrategy {

  void log(int priority, String tag, String message);
}
