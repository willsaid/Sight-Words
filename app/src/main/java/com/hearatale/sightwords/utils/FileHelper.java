package com.hearatale.sightwords.utils;

import com.hearatale.sightwords.Application;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.hearatale.sightwords.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    public static String getAppDir() {
        String path = Environment.getExternalStorageDirectory().toString() + "/" + Application.Context.getString(R.string.app_name);
        createOrExistsDir(path);
        return path;
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param dirPath The path of directory.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsDir(final String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * Create a directory if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param filePath The path of file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsFile(final String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * Create a file if it doesn't exist, otherwise do nothing.
     *
     * @param file The file.
     * @return {@code true}: exists or creates successfully<br>{@code false}: otherwise
     */
    public static boolean createOrExistsFile(final File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Return whether it is a directory.
     *
     * @param dirPath The path of directory.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDir(final String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    /**
     * Return whether it is a directory.
     *
     * @param file The file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDir(final File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * Return the file by path.
     *
     * @param filePath The path of file.
     * @return the file
     */
    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean writeNoMediaFile(String directoryPath) {
        String storageState = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(storageState)) {
            try {
                File noMedia = new File(directoryPath, ".nomedia");

                if (noMedia.exists()) {
                    return true;
                }

                FileOutputStream noMediaOutStream = new FileOutputStream(noMedia);
                noMediaOutStream.write(0);
                noMediaOutStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

        return true;

    }

    /**
     * Delete the file.
     *
     * @param srcFilePath The path of source file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean deleteFile(final String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    /**
     * Delete the file.
     *
     * @param file The file.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * Scan file use when add a file to store
     *
     * @param context App context
     * @param pathsArray list paths
     */
    public void updateMediaStore(Context context,MediaScannerConnection.OnScanCompletedListener listener , String... pathsArray){
        MediaScannerConnection.scanFile(context, pathsArray, null, listener);
    }



}
