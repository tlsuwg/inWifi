package com.hxuehh.rebirth.device.domain;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.TypeIDable;

import java.io.Serializable;

/**
 * Created by suwg on 2015/9/11.
 */

//输入参数
public  class DeviceCapacityInParameter implements TypeIDable, Serializable {

    public static final int Type_ShowHis = 1102;
    public static final int Type_ShowLastHis = 1103;
    public static final int Type_ClearHis = 1107;
    public static final int Type_Sleep = 1101;
    public static final int Type_AskStatus = 1100;

    public static final int Type_DeviceCapacityStatusChange = 1104;

    public static final int Type_SetDeviceCapacityBaseSenserUsed_0 = 1105;//是不是能够使用
    public static final int Type_SetDeviceCapacityBaseSenserUsed_1 = 1106;//是不是能够使用





    int type;
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }


    public boolean isAskStatus() {
        return getType() == Type_AskStatus;
    }

    public String[] getAllNames() {
        return null;
    }

    public String getTypeName() {
        if (type == Type_AskStatus) {
            return "查询";
        } else if (type == Type_Sleep) {
            return "时延" + getSleepTime();
        } else if (type == Type_Sleep) {
            return "操作日志";
        }
        return null;
    }

    protected int sleepTime;

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public DeviceCapacityInParameter() {

    }

    public DeviceCapacityInParameter(int type) {
        this.type = type;
    }

    @Deprecated
    public DeviceCapacityInParameter(boolean isask) {
        this.type = Type_AskStatus;
    }


    public  static  final int DeviceCapacityInParameter_CommonCmdLevel=10;//一般指令
    public  static  final int DeviceCapacityInParameter_SensorCmdLevel=9;//传感器指令

    protected int cmdLevel=DeviceCapacityInParameter_CommonCmdLevel;//正常级别，动作

    public int getCmdLevel() {
        return cmdLevel;
    }

    public void setCmdLevel(int cmdLevel) {
        this.cmdLevel = cmdLevel;
    }


    public boolean isCmdLevelLow() {
        return cmdLevel<DeviceCapacityInParameter_CommonCmdLevel;
    }

    //    ======================================

    DeviceCapacityInParameter mNextDeviceCapacityInParameter;//责任链  继续执行

    public DeviceCapacityInParameter getmNextDeviceCapacityInParameter() {
        return mNextDeviceCapacityInParameter;
    }

    public void setmNextDeviceCapacityInParameter(DeviceCapacityInParameter mNextDeviceCapacityInParameter) {
        this.mNextDeviceCapacityInParameter = mNextDeviceCapacityInParameter;
    }


    //    ==============================================
   private long tagTime;//和 对象进行对比，看看tag是不是一致
    public long getTagTime() {
        return tagTime;
    }

    public void setTagTime(long tagTime) {
        this.tagTime = tagTime;
    }

    public boolean isTheSameTag(long key) {
        return tagTime == key;
    }

    //    ==============================================

}
