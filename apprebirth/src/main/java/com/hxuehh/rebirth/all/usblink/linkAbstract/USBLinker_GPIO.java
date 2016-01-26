package com.hxuehh.rebirth.all.usblink.linkAbstract;

import android.hardware.usb.UsbAccessory;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;

/**
 * Created by suwg on 2015/10/15.
 */
public abstract class USBLinker_GPIO extends USBLinker_ {


    public USBLinker_GPIO(UsbAccessory accessory, FaceCommCallBack mcallBackForStatus) {
        super(accessory, mcallBackForStatus);
    }

    //============================================GPIO
    public abstract void resetGPIOPort() ;

    public abstract void configGPIOPort(int configOutMap, int configINMap);

    public abstract void setGPIOPort(int portData) ;

    public abstract byte[] readGPIOPort() throws FaceException;


}
