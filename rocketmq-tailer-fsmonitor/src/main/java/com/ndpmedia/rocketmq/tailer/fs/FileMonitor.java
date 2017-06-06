package com.ndpmedia.rocketmq.tailer.fs;

import com.ndpmedia.rocketmq.tailer.util.NativeUtils;

import java.io.IOException;

/**
 * Created by holly on 9/7/16.
 */
class FileMonitor {
  static {
    //System.loadLibrary("filemonitor"); // Load native library at runtime
    try {
      NativeUtils.loadLibraryFromJar("/" + System.mapLibraryName("filemonitor"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public native void startMonitor(String[] path, FileEventListener listener);

  public native void stop();


}
