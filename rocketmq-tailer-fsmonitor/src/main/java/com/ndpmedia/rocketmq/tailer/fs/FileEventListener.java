package com.ndpmedia.rocketmq.tailer.fs;

/**
 * Created by holly on 9/7/16.
 */
public interface FileEventListener {
  void onCreate(String file);
  void onModify(String file);
  void onDelete(String file);
  void onRename(String oldFile,String newFile);
}
