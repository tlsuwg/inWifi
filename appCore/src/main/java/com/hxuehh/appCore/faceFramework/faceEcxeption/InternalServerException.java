package com.hxuehh.appCore.faceFramework.faceEcxeption;

/**
 * Created by suwg on 2015/8/13.
 */
public class InternalServerException extends Exception {
    public InternalServerException() {
    }

    public InternalServerException(String detailMessage) {
        super(detailMessage);
    }

    public InternalServerException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InternalServerException(Throwable throwable) {
        super(throwable);
    }


}
