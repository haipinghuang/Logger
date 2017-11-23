package com.hai.mylog;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Abstract class that takes care of background threading the file log operation on Android.
 * implementing classes are free to directly perform I/O operations there.
 */
public class DiskLogStrategy implements LogStrategy {

    private final Handler handler;

    public DiskLogStrategy(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void log(int level, String tag, String message) {
        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(level, message));
    }

    static class WriteHandler extends Handler {

        private final File folder;
        private final String fileName;

        WriteHandler(Looper looper, File folder, String fileName) {
            super(looper);
            this.folder = folder;
            this.fileName = fileName;
        }

        @Override
        public void handleMessage(Message msg) {
            String content = (String) msg.obj;

            FileWriter fileWriter = null;
            File logFile = getLogFile(folder, fileName);
            if (logFile != null) {
                try {
                    fileWriter = new FileWriter(logFile, true);
                    writeLog(fileWriter, content);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileWriter != null)
                        try {
                            fileWriter.flush();
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        }

        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an instance of FileWriter already initialised to the correct file
         */
        private void writeLog(FileWriter fileWriter, String content) throws IOException {
            fileWriter.append(content);
        }

        private File getLogFile(File cacheDir, String fileName) {
            if (cacheDir != null) {
                if (!cacheDir.exists()) cacheDir.mkdirs();
                File file = new File(cacheDir, fileName);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                return file;
            } else return null;
        }
    }
}