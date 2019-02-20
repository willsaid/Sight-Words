package com.hearatale.sightwords.data.network.api.base.async;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BackgroundTask extends BackgroundResult {

    private static final int CORE_POOL_SIZE;
    private static final int CPU_COUNT;
    private static final int KEEP_ALIVE = 1;
    private static final int MAXIMUM_POOL_SIZE;
    private static final ScheduledExecutorService SCHEDULER;
    private static final Executor THREAD_POOL_EXECUTOR;
    private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());
    private static final BlockingQueue _poolWorkQueue;
    private static final ThreadFactory _threadFactory;

    public BackgroundTask() {
    }

    public void execute() {
        THREAD_POOL_EXECUTOR.execute(new TaskRunnable((byte) 0));
    }

    public abstract void run();

    public void schedule(long l) {
        SCHEDULER.schedule(new TaskRunnable((byte) 0), l, TimeUnit.MILLISECONDS);
    }

    static {
        int i = Runtime.getRuntime().availableProcessors();
        CPU_COUNT = i;
        CORE_POOL_SIZE = i + 1;
        MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1 + 1000;
        _threadFactory = new _cls1();
        _poolWorkQueue = new LinkedBlockingQueue(128);
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 1L, TimeUnit.SECONDS, _poolWorkQueue, _threadFactory);
        SCHEDULER = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
    }


    private class TaskRunnable
            implements Runnable {

        private BackgroundTask a;

        public void run() {
            if (a != null) {
                a.run();
                BackgroundTask.UI_HANDLER.post(a.new TaskFinishRunnable(a));
            }
        }

        private TaskRunnable() {
            a = BackgroundTask.this;
        }

        TaskRunnable(byte byte0) {
            this();
        }

        @SuppressWarnings("unused")
        private class TaskFinishRunnable
                implements Runnable {

            private BackgroundTask a;

            public void run() {
                if (a != null) {
                    a.onFinish();
                }
            }

            private TaskFinishRunnable() {
                a = BackgroundTask.this;
            }

            TaskFinishRunnable(byte byte0) {
                this();
            }
        }

    }


    @SuppressWarnings("unused")
    class TaskFinishRunnable
            implements Runnable {

        private BackgroundTask a;

        public void run() {
            if (a != null) {
                a.onFinish();
            }
        }

        private TaskFinishRunnable(BackgroundTask backgroundtask) {
            a = backgroundtask;
        }

        TaskFinishRunnable(BackgroundTask backgroundtask, byte byte0) {
            this(backgroundtask);
        }
    }


    private static class _cls1
            implements ThreadFactory {

        private final AtomicInteger a = new AtomicInteger(1);

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, (new StringBuilder("BackgroundTask #")).append(a.getAndIncrement()).toString());
        }

        _cls1() {
        }
    }
}
