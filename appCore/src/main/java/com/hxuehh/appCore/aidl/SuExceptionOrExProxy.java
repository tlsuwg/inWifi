package com.hxuehh.appCore.aidl;

/**
 * Created by suwg on 2015/8/14.
 */
public class SuExceptionOrExProxy extends Exception {
    public SuExceptionOrExProxy() {
    }

    public SuExceptionOrExProxy(String detailMessage) {
        super(detailMessage);
    }

    public SuExceptionOrExProxy(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SuExceptionOrExProxy(Throwable throwable) {
        super(throwable);
    }


}
