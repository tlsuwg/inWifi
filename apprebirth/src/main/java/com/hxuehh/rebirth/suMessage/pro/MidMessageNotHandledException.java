package com.hxuehh.rebirth.suMessage.pro;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;

/**
 * Created by suwg on 2015/8/16.
 */
public class MidMessageNotHandledException extends FaceException {
    public MidMessageNotHandledException() {
    }

    public MidMessageNotHandledException(String detailMessage) {
        super(detailMessage);
    }

    public MidMessageNotHandledException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MidMessageNotHandledException(Throwable throwable) {
        super(throwable);
    }

    public MidMessageNotHandledException(Exception e) {
        super(e);
    }

    public MidMessageNotHandledException(Exception e, String info) {
        super(e, info);
    }

    public MidMessageNotHandledException(String s, Throwable throwable, boolean b, boolean b2) {
        super(s, throwable, b, b2);
    }
}
