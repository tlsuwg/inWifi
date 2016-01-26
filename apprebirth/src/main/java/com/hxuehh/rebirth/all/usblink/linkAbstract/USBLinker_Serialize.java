package com.hxuehh.rebirth.all.usblink.linkAbstract;

import android.hardware.usb.UsbAccessory;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;

/**
 * Created by suwg on 2015/10/15.
 */
public abstract class USBLinker_Serialize extends USBLinker_ {


    public USBLinker_Serialize(UsbAccessory accessory, FaceCommCallBack mcallBackForStatus) {
        super(accessory, mcallBackForStatus);
    }

    //	====================================串口
    public void getSerializDateThreadCall() {
        mReadThread = new ReadThreadBySuWay(inputstream);
        mReadThread.start();
    }
    public abstract void setSerializConfig(int baud, byte dataBits, byte stopBits, byte parity, byte flowControl);
    public abstract boolean sendSerializDataBySuWay(int numBytes, byte[] buffer);






}
