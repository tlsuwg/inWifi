package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;

import java.util.Date;

public class MidMessageOrder_2 extends MidMessage {

    String mResquetID;

    public MidMessageOrder_2(int cmd) {
        super(cmd);
        mResquetID = DeviceInfo.getInstens().getSU_UUID() + "^^" + new Date().getTime();
        this.putKeyValue(Key_CMDkeyResquetID, mResquetID);
    }

    public String getmResquetID() {
        return mResquetID;
    }

    public void setmResquetID(String mResquetID) {
        this.mResquetID = mResquetID;
    }

    public MidMessageOrder_2() {
        super();
    }


    public Object getCMDkeyResquetID() {
        if (mResquetID == null) getmResquetID();
        return mResquetID;
    }

    void getmResquetIDInMap() {
        mResquetID = (String) getByKey(MidMessage.Key_CMDkeyResquetID);
    }


    @Override
    public MidMessage getTrue(byte[] bs) throws FaceException {
        super.getTrue(bs);
        getmResquetIDInMap();
        return this;
    }


    transient MidMessageBack_2 res;

    public MidMessageBack_2 getRes() {
        return res;
    }

    public void setRes(MidMessageBack_2 res) {
        this.res = res;
    }
}