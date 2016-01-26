package com.hxuehh.appCore.faceFramework.faceEcxeption;

/**
 * Created by suwg on 2015/5/4.
 */
public class NoAddException extends Exception {

    public NoAddException() {
    }

    public NoAddException(String detailMessage) {
        super(detailMessage);
    }

    public NoAddException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoAddException(Throwable throwable) {
        super(throwable);
    }

}
