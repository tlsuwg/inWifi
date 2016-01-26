package com.hxuehh.appCore.faceFramework.faceEcxeption;

/**
 * Created by suwg on 2014/5/20.
 */
public class JsonParseException extends FaceException {


    public JsonParseException() {
    }

    public JsonParseException(String detailMessage) {
        super(detailMessage);
    }

    public JsonParseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public JsonParseException(Throwable throwable) {
        super(throwable);
    }

    public JsonParseException(Exception e) {
        super(e);
    }

    public JsonParseException(String s, Throwable throwable, boolean b, boolean b2) {
        super(s, throwable, b, b2);
    }
}
