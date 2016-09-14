package com.ndpmedia.rocketmq.tailer.fs.filemonitor;

import com.ndpmedia.rocketmq.tailer.fs.FileEventListener;
import com.ndpmedia.rocketmq.tailer.fs.FileMonitor;
import com.ndpmedia.rocketmq.tailer.util.NativeUtils;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by holly on 9/6/16.
 */
public class FileMonitorTest {

  {
    String osName = System.getProperty("os.name").toLowerCase();
    if (osName.equals("linux")) {
      try {
        NativeUtils.loadLibraryFromJar("");
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } else if (osName.startsWith("mac os x")) {
      try {
        NativeUtils.loadLibraryFromJar("/" + System.mapLibraryName("filemonitor"));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Test
  public void testFileMove() throws IOException, InterruptedException {
    NativeUtils.loadLibraryFromJar("/" + System.mapLibraryName("filemonitor"));
    FileMonitor fileMonitor = new FileMonitor();
    fileMonitor.startMonitor(new String[]{"/Users/holly/Downloads/test1"}, new
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

    Thread.sleep(30000);

    fileMonitor.stop();

  }
}
