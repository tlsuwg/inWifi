package com.hxuehh.appCore.faceFramework.faceEcxeption;

/**
 * Created by suwg on 2014/8/13.
 */
public class ChangeToOtherActivityException extends  RuntimeException{

    public ChangeToOtherActivityException() {
        super();
    }

    public ChangeToOtherActivityException(String detailMessage) {
        super(detailMessage);
    }

    public ChangeToOtherActivityException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ChangeToOtherActivityException(Throwable throwable) {
        super(throwable);
    }

    protected ChangeToOtherActivityException(String s, Throwable throwable, boolean b, boolean b2) {
        // super(s, throwable, b, b2);
        super(s, throwable);
    }
}
