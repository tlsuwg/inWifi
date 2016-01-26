package com.hxuehh.rebirth.all.domain;

import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;

import java.io.Serializable;
import java.util.Date;


//运行状态
public class RunningStatus implements Serializable {

    public static final String [] Info=new String[]{"未知状态","初始化","初始化错误","正在运行","运行出错","已经暂停","已经结束"};
    public static final int Init = 1;
    public static final int InitErr = 2;
    public static final int Running = 3;
    public static final int RunTimeErr = 4;
    public static final int Stop = 5;
    public static final int End = 6;

    int status;
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    long time;
    String info;
    public RunningStatus() {
        this.status = Init;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public RunningStatus(int status, String info) {
        this.status = status;
        this.time = new Date().getTime();
        this.info = info;
    }

    public  void setStatus(int status, String info){
       this.status = status;
       this.time = new Date().getTime();
       this.info = info;
   }



    public String toStatusString() {
        int i=status;
        if(i>=Info.length||i<0){
            i=0;
        }
        String timeinfo= DateUtil.getMDHSTime(time);
        String all=timeinfo+":"+Info[i]+(info==null?"":" "+info+" ;");
        return all;
    }
}