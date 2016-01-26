package com.hxuehh.rebirth.all.domain;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.all.domain.Interfaces.TimeOut;

import java.util.Date;

/**
 * Created by suwg on 2015/8/19.
 */

//超时记录器
public abstract class LinkTimeFaceCall implements FaceCommCallBack,TimeOut {


    transient long startTime;
    transient long endTime;
    transient long actionTime;
    transient long timeOutLong;


    protected LinkTimeFaceCall(long timeOutLong) {
        this.timeOutLong = timeOutLong;
    }

    public boolean isTimeOut(){
        return actionTime-startTime>timeOutLong;
    }

    public boolean doTimeOut(){
        return true;
    }


    public long getTimeOutLong() {
        return timeOutLong;
    }

    public void setTimeOutLong(long timeOutLong) {
        this.timeOutLong = timeOutLong;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime() {
        this.endTime = new Date().getTime();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        this.startTime = new Date().getTime();
    }

    public long getActionTime() {
        return actionTime;
    }

    public void setActionTime() {
        this.actionTime  = new Date().getTime();
    }



    public void setinitStartTime() {
        this.startTime = 0;
    }



}
