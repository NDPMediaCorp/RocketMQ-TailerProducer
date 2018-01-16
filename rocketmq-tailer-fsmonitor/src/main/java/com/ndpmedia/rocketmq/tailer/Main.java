package com.ndpmedia.rocketmq.tailer;

import com.ndpmedia.rocketmq.tailer.fs.FileEventListener;
import com.ndpmedia.rocketmq.tailer.fs.FileEventNotify;
import com.ndpmedia.rocketmq.tailer.util.NativeUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by holly on 9/18/16.
 */
public class Main {

    public static void main(String[] args) {
        try {
            NativeUtils.loadLibraryFromJar("/" + System.mapLibraryName("filemonitor"));
            System.out.println("start monitor");
            List<FileEventNotify> list = new ArrayList<>();
            for(String arg:args){
                System.out.println("start to init file notify "+ arg);
                FileEventNotify fileEventNotify = new FileEventNotify();
                list.add(fileEventNotify);
                fileEventNotify.registerListener(new String[] {arg}, new
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
            }



            while (true) {
                Thread.sleep(1000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
