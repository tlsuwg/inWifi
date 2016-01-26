package com.hxuehh.reuse_Process_Imp.app.threadManager;

import android.os.Process;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Created by suwg on 2014/12/5.
 */

public class ThreadManager {

    public static final int imageThread=3;//其实是 *2的  load 和cache
    public static final int UIThread=2;
    public static final int BackThread=5;
    public static int TCPMainResponseThread =3;
    public static int TCPDeviceResponseThread =1;

    public static int getImageThread() {
        return imageThread;
    }

    static ThreadManager instance = null;

    public static synchronized ThreadManager getInstance() {
        if (instance == null)
            instance = new ThreadManager();
        return instance;
    }

    private  ExecutorService threadPoolForLoadUI;
    private  synchronized ExecutorService getUIhreadPool() {
        if (threadPoolForLoadUI == null) {
            threadPoolForLoadUI = Executors.newFixedThreadPool(UIThread, new ThreadFactory() {
                int i=0;
                @Override
                public Thread newThread(Runnable r) {
                    Thread mThread = new Thread(r," su ui thread" + i++);
                    mThread.setPriority(Thread.MAX_PRIORITY);
                    Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                    return mThread;
                }
            });
        }
        return threadPoolForLoadUI;
    }


    private  ExecutorService threadPoolForBack;
    private ExecutorService getBackThreadPool() {
        if (threadPoolForBack == null) {
            threadPoolForBack = Executors.newFixedThreadPool(BackThread, new ThreadFactory() {
                int i=0;
                @Override
                public Thread newThread(Runnable r) {
                    Thread mThread = new Thread(r," su back thread" + i++);
                    mThread.setPriority(Thread.NORM_PRIORITY);
                    Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                    return mThread;
                }
            });
        }
        return threadPoolForBack;
    }


    public Future submitUIThread(Runnable run) {
        return getUIhreadPool().submit(run);
    }


    @Deprecated
    public void executeUIThread(Runnable run) {
        getUIhreadPool().execute(run);
    }



    public Future submitBackThread(Runnable run) {
        return getBackThreadPool().submit(run);
    }


    @Deprecated
    public void executeBackThread(Runnable run) {
        getBackThreadPool().execute(run);
    }



    public Thread getNewThread(String name,Runnable run){
        Thread mThread=   new Thread(run,name);
        return  mThread;
    }


    public Thread getNewThread(String name,int threadLeave,Runnable run){
        Thread mThread=   new Thread(run,name);
        mThread.setPriority(threadLeave);
        if(threadLeave>8) {
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
        }
        return  mThread;
    }



}
