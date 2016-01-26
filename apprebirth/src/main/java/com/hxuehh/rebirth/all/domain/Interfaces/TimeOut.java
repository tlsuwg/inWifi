package com.hxuehh.rebirth.all.domain.Interfaces;

/**
 * Created by suwg on 2015/8/19.
 */
public interface TimeOut {


//    transient long startTime;
//    transient long endTime;
//    transient long actionTime;
//    transient long timeOutLong;
//
//
//    public boolean isTimeOut(){
//        return actionTime-startTime>timeOutLong;
//    }
//
//    public boolean doTimeOut(){
//        return true;
//    }
//
//
//    public long getTimeOutLong() {
//        return timeOutLong;
//    }
//
//    public void setTimeOutLong(long timeOutLong) {
//        this.timeOutLong = timeOutLong;
//    }
//
//    public long getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime() {
//        this.endTime = new Date().getTime();
//    }
//
//    public long getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime() {
//        this.startTime = new Date().getTime();
//    }
//
//    public long getActionTime() {
//        return actionTime;
//    }
//
//    public void setActionTime() {
//        this.actionTime  = new Date().getTime();
//    }



    public boolean isTimeOut();
    public boolean doTimeOut();
    public long getTimeOutLong();
    public void setTimeOutLong(long timeOutLong);
    public long getEndTime();
    public void setEndTime();
    public long getStartTime();
    public void setStartTime();
    public long getActionTime();
    public void setActionTime();
}
