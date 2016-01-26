package com.hxuehh.appCore.faceFramework.faceEcxeption;

/**
 * Created by suwg on 2014/5/20.
 */
public class FaceException extends  Exception {

    String key;

    protected FaceException() {
    }

    public FaceException(String detailMessage) {
        super(detailMessage);
    }

 public    int sensorErrType;
    public static final int sensorErrType_SameCMD=1;
    public static final int sensorErrType_=2;

    public FaceException(String detailMessage,  int sensorErrType) {
        super(detailMessage);
        this.sensorErrType=sensorErrType;
    }

    protected FaceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    protected FaceException(Throwable throwable) {
        super(throwable);
    }

    protected FaceException(Exception e) {
        super(new Throwable(e));
    }

    public FaceException(Exception e,String info) {
        super(new Throwable(e));
        this.key=info;
    }

    protected FaceException(String s, Throwable throwable, boolean b, boolean b2) {
        super(/*s, throwable, b, b2*/);
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public String toString() {
        return super.toString()+key;
    }
}
