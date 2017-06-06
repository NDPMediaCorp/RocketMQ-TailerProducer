package com.ndpmedia.rocketmq.tailer.fs;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by holly on 06/06/2017.
 */
public class FileEventNotify {
    private static FileMonitor fileMonitor;
    private static FileEventListenerImpl fileEventListener;

    public FileEventNotify() {
        synchronized (fileMonitor) {
            if (fileMonitor == null) {
                fileMonitor = new FileMonitor();
                fileEventListener = new FileEventListenerImpl();
            }

        }
    }

    public void registerListener(String[] path, FileEventListener listener) {
        synchronized (fileEventListener) {
            fileEventListener.register(path, listener);
        }
    }


    public void stop(){
        fileMonitor.stop();
    }

    class FileEventListenerImpl implements FileEventListener {
        private Map<String, List<FileEventListener>> map = new ConcurrentHashMap<>();

        @Override public void onCreate(final String file) {
            onNotify(new Notify() {
                @Override public void onNotify(FileEventListener listener) {
                    listener.onCreate(file);
                }
            }, file);
        }

        @Override public void onModify(final String file) {
            onNotify(new Notify() {
                @Override public void onNotify(FileEventListener listener) {
                    listener.onModify(file);
                }
            }, file);
        }

        @Override public void onDelete(final String file) {
            onNotify(new Notify() {
                @Override public void onNotify(FileEventListener listener) {
                    listener.onDelete(file);
                }
            }, file);
        }

        @Override public void onRename(final String oldFile, final String newFile) {
            onNotify(new Notify() {
                @Override public void onNotify(FileEventListener listener) {
                    listener.onRename(oldFile, newFile);
                }
            }, oldFile);
        }


        private void onNotify(Notify notify,String file){
            for (Iterator<Map.Entry<String, List<FileEventListener>>> iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, List<FileEventListener>> entry = iterator.next();
                if(file.startsWith(entry.getKey())){
                    try{
                        for(FileEventListener listener:entry.getValue()){
                            notify.onNotify(listener);
                        }
                    }catch(Exception e){
                        //ignore
                    }
                }
            }
        }

        public void register(String[] paths, FileEventListener listener) {
            for (String path : paths) {
                if (map.get(path) == null) {
                    map.put(path, new CopyOnWriteArrayList<FileEventListener>());
                }
                map.get(path).add(listener);
            }

            if (map.size() > 0) {
                fileMonitor.startMonitor(paths, this);
            }
        }
    }

    interface Notify{
        void onNotify(FileEventListener listener);
    }


}
