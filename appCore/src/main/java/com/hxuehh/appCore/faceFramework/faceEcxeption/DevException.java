package com.hxuehh.appCore.faceFramework.faceEcxeption;

/**
 * Created by suwg on 2014/6/30.
 */
public class DevException extends RuntimeException{

//    大多数是没有设置正确的URL  key 等


    public DevException() {
        super();
    }

    public DevException(String detailMessage) {
        super(detailMessage);
    }

    public DevException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DevException(Throwable throwable) {
        super(throwable);
    }
}
