package com.ndpmedia.rocketmq.tailer.fs.filemonitor;

import com.ndpmedia.rocketmq.tailer.fs.FileEventListener;
import com.ndpmedia.rocketmq.tailer.fs.FileEventNotify;
import com.ndpmedia.rocketmq.tailer.util.NativeUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;

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
        FileEventNotify fileEventNotify = new FileEventNotify();
        String root = System.getProperty("java.io.tmpdir");
        Path path = Paths.get(root, "test");

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        fileEventNotify.registerListener(new String[] {path.toAbsolutePath().toString()}, new
            FileEventListener() {

                @Override
                public void onCreate(String file) {
                    System.out.println("create new file: " + file);
                    countDownLatch.countDown();
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

        File folder = new File(path.toAbsolutePath().toString());
        if(!folder.exists()){
            folder.mkdirs();
        }
        folder.deleteOnExit();
        File file = new File(path.toAbsolutePath().toString(), "test1");
        file.createNewFile();
        file.deleteOnExit();
        countDownLatch.await();
        fileEventNotify.stop();

    }
}
