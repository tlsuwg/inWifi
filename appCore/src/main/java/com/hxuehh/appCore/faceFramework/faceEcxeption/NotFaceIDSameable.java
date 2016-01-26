package com.hxuehh.appCore.faceFramework.faceEcxeption;

/**
 * Created by suwg on 2014/6/30.
 */
public class NotFaceIDSameable extends RuntimeException{


    public NotFaceIDSameable() {
        super();
    }

    public NotFaceIDSameable(String detailMessage) {
        super(detailMessage);
    }

    public NotFaceIDSameable(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NotFaceIDSameable(Throwable throwable) {
        super(throwable);
    }
}
