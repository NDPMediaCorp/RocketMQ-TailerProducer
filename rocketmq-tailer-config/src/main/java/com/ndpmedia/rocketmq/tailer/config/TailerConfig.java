package com.ndpmedia.rocketmq.tailer.config;

/**
 * Created by holly on 9/8/16.
 */
public class TailerConfig {
  public static final String BACKOFF_SLEEP_INCREMENT = "backoffSleepIncrement";
  public static final String MAX_BACKOFF_SLEEP = "maxBackoffSleep";
  public static final long DEFAULT_BACKOFF_SLEEP_INCREMENT = 1000;
  public static final long DEFAULT_MAX_BACKOFF_SLEEP = 5000;
}
