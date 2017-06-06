package com.ndpmedia.rocketmq.tailer;

import com.ndpmedia.rocketmq.tailer.fs.FileEventListener;
import com.ndpmedia.rocketmq.tailer.fs.FileEventNotify;
import com.ndpmedia.rocketmq.tailer.util.NativeUtils;

/**
 * Created by holly on 9/18/16.
 */
public class Main {

    public static void main(String[] args) {
        FileEventNotify fileEventNotify = null;
        try {
            NativeUtils.loadLibraryFromJar("/" + System.mapLibraryName("filemonitor"));
            fileEventNotify = new FileEventNotify();
            System.out.println("start monitor");
            fileEventNotify.registerListener(new String[] {args[0]}, new
                FileEventListener() {

                    @Override
                    public void onCreate(String file) {
                        System.out.println("create new file: " + file);
                    }

                    @Override
                    public void onModify(String file) {
                        System.out.println("modify file: " + file);
                    }

                    @Override
                    public void onDelete(String file) {
                        System.out.println("delete file: " + file);
                    }

                    @Override
                    public void onRename(String oldFile, String newFile) {
                        System.out.println("rename file: " + oldFile + " to " + newFile);
                    }
                });

            while (true) {
                Thread.sleep(1000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileEventNotify != null) {
                fileEventNotify.stop();
            }
        }
    }
}
