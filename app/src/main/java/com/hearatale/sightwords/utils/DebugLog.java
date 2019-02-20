package com.hearatale.sightwords.utils;

import android.support.annotation.IntDef;

import com.hearatale.sightwords.BuildConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public final class DebugLog {

    private static final String SOURCE_FILE = "SourceFile";
    private static int LEVEL = BuildConfig.DEBUG ? Level.DEBUG : Level.SUPPRESS;
//    private static boolean withMethodName;
//    private static boolean withLineNumber;

    private static boolean withMethodName = true;
    private static boolean withLineNumber = true;

    private DebugLog() {
    }

    /**
     * Annotation interface for log level:
     * {@link #VERBOSE}, {@link #DEBUG}, {@link #INFO},
     * {@link #WARN}, {@link #ERROR}, {@link #SUPPRESS}
     */
    @IntDef({Level.VERBOSE, Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.SUPPRESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
        int VERBOSE = android.util.Log.VERBOSE;
        int DEBUG = android.util.Log.DEBUG;
        int INFO = android.util.Log.INFO;
        int WARN = android.util.Log.WARN;
        int ERROR = android.util.Log.ERROR;
        int SUPPRESS = 10;
    }

    public static void setLevel(@Level int level) {
        LEVEL = level;
    }

    /**
     * Allows to log method name and line number from where the log is called.
     * <p>- With method name: {@code [method] msg}.
     * <br>- With line number: {@code [method:line] msg}.</p>
     * <b>Note:</b> Line number needs method name enabled.
     *
     * @param method true to print method name at the beginning of the message, false otherwise
     * @param line   true to print line number after the method name, false otherwise
     */
    public static void logMethodName(boolean method, boolean line) {
        withMethodName = method;
        withLineNumber = line;
    }

    public static boolean isVerboseEnabled() {
        return LEVEL <= Level.VERBOSE;
    }

    public static boolean isDebugEnabled() {
        return LEVEL <= Level.DEBUG;
    }

    public static boolean isInfoEnabled() {
        return LEVEL <= Level.INFO;
    }

    public static boolean isWarnEnabled() {
        return LEVEL <= Level.WARN;
    }

    public static boolean isErrorEnabled() {
        return LEVEL <= Level.ERROR;
    }

    /**
     * As {@link #v(String, String, Object...)} but with custom tag for one call only.
     *
     * @param tag  custom tag, used to identify the source of a log message
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void v(String tag, String msg, Object... args) {
        if (isInfoEnabled()) {
            android.util.Log.v(tag, formatMessage(msg, args));
        }
    }

    /**
     * Sends a {@link Level#VERBOSE} log message.
     *
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void v(String msg, Object... args) {
        if (isVerboseEnabled()) {
            android.util.Log.v(getTag(), formatMessage(msg, args));
        }
    }

    /**
     * As {@link #d(String, String, Object...)} but with custom tag for one call only.
     *
     * @param tag  custom tag, used to identify the source of a log message
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void d(String tag, String msg, Object... args) {
        if (isInfoEnabled()) {
            android.util.Log.d(tag, formatMessage(msg, args));
        }
    }

    /**
     * Sends a {@link Level#DEBUG} log message.
     *
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void d(String msg, Object... args) {
        if (isDebugEnabled()) {
            android.util.Log.d(getTag(), formatMessage(msg, args));
        }
    }

    /**
     * Sends an {@link Level#INFO} log message.
     *
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void i(String msg, Object... args) {
        if (isInfoEnabled()) {
            android.util.Log.i(getTag(), formatMessage(msg, args));
        }
    }

    /**
     * As {@link #i(String, Object...)} but with custom tag for one call only.
     *
     * @param tag  custom tag, used to identify the source of a log message
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void i(String tag, String msg, Object... args) {
        if (isInfoEnabled()) {
            android.util.Log.i(tag, formatMessage(msg, args));
        }
    }


    /**
     * As {@link #w(String, String, Object...)} but with custom tag for one call only.
     *
     * @param tag  custom tag, used to identify the source of a log message
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void w(String tag, String msg, Object... args) {
        if (isInfoEnabled()) {
            android.util.Log.w(tag, formatMessage(msg, args));
        }
    }

    /**
     * Sends a {@link Level#WARN} log message.
     *
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void w(String msg, Object... args) {
        if (isWarnEnabled()) {
            android.util.Log.w(getTag(), formatMessage(msg, args));
        }
    }

    /**
     * Sends a {@link Level#WARN} log message with the Exception at the end of the message.
     *
     * @param t    The exception to log
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void w(Throwable t, String msg, Object... args) {
        if (isWarnEnabled()) {
            android.util.Log.w(getTag(), formatMessage(msg, args), t);
        }
    }

    /**
     * As {@link #e(String, String, Object...)} but with custom tag for one call only.
     *
     * @param tag  custom tag, used to identify the source of a log message
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void e(String tag, String msg, Object... args) {
        if (isInfoEnabled()) {
            android.util.Log.e(tag, formatMessage(msg, args));
        }
    }

    /**
     * Sends an {@link Level#ERROR} log message.
     *
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void e(String msg, Object... args) {
        if (isErrorEnabled()) {
            android.util.Log.e(getTag(), formatMessage(msg, args));
        }
    }

    /**
     * Sends an {@link Level#ERROR} log message with the Exception at the end of the message.
     *
     * @param t    The exception to log
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void e(Throwable t, String msg, Object... args) {
        if (isErrorEnabled()) {
            android.util.Log.e(getTag(), formatMessage(msg, args), t);
        }
    }

    /**
     * As {@link #wtf(String, String, Object...)} but with custom tag for one call only.
     *
     * @param tag  custom tag, used to identify the source of a log message
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void wtf(String tag, String msg, Object... args) {
        if (isInfoEnabled()) {
            android.util.Log.w(tag, formatMessage(msg, args));
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen.
     *
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void wtf(String msg, Object... args) {
        if (isErrorEnabled()) {
            android.util.Log.wtf(getTag(), formatMessage(msg, args));
        }
    }

    /**
     * What a Terrible Failure: Report a condition that should never happen with the Exception
     * at the end of the message.
     *
     * @param t    The exception to log
     * @param msg  the message you would like logged
     * @param args the extra arguments for the message
     */
    public static void wtf(Throwable t, String msg, Object... args) {
        if (isErrorEnabled()) {
            android.util.Log.wtf(getTag(), formatMessage(msg, args), t);
        }
    }

    private static String getTag() {
        StackTraceElement traceElement = new Throwable().getStackTrace()[2];
        String fileName = traceElement.getFileName();
        if (fileName == null) return SOURCE_FILE;
        return fileName.split("[.]")[0];
    }

    private static String formatMessage(String msg, Object... args) {
        // In order to have the "null" values logged we need to pass args when null to the formatter
        // (This can still break depending on conversion of the formatter, see String.format)
        // else if there is no args, we return the message as-is, otherwise we pass args to formatting normally.
        return createLog(args != null && args.length == 0 ? msg : String.format(msg, args));
    }

    private static String createLog(String log) {
        if (withMethodName) {
            StackTraceElement traceElement = new Throwable().getStackTrace()[3];
            if (withLineNumber) {
                return String.format("[%s:%s] %s", traceElement.getMethodName(), traceElement.getLineNumber(), log);
            } else {
                return String.format("[%s] %s", traceElement.getMethodName(), log);
            }
        }
        return log;
    }

}
