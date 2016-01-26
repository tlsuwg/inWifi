package com.hxuehh.rebirth.all.usblink;

import android.hardware.usb.UsbAccessory;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_GPIO;

import java.io.IOException;


//此类不能传输较大数据流 只能作为一个 独立数据块 进行通信
//可序列化
public  class WCH_USBLinker_GPIO extends USBLinker_GPIO {

    public WCH_USBLinker_GPIO(UsbAccessory accessory, FaceCommCallBack mcallBackForStatus) {
        super(accessory, mcallBackForStatus);
    }

    public void resetGPIOPort() {
        writeData[0] = 0x13;
        writeData[1] = 0x00;
        writeData[2] = 0x00;
        writeData[3] = 0x00;
        writeData[4] = 0x00;
        writeIntoUSB(5);
        if (getStatus() == WCH_USBLinker_GPIO.LinkStatus_Accessory_Open) {
            setStatus(LinkStatus_GetData);
        }
    }

    public void configGPIOPort(int configOutMap, int configINMap) {
        configOutMap |= 0x0000;
        configINMap &= 0xFFFF;
        writeData[0] = 0x10;
        writeData[1] = (byte) (configOutMap & 0xFF);
        writeData[2] = (byte) (configINMap & 0xFF);
        writeData[3] = (byte) ((configOutMap >> 8) & 0xFF);
        writeData[4] = (byte) ((configINMap >> 8) & 0xFF);
        writeIntoUSB(5);
    }

    public void setGPIOPort(int portData) {
        portData |= 0x0000;
        writeData[0] = 0x12;
        writeData[1] = (byte) (portData & 0xFF);
        writeData[2] = (byte) ((portData >> 8) & 0xFF);
        writeData[3] = 0x00;
        writeData[4] = 0x00;
        writeIntoUSB(5);
    }

    public byte[] readGPIOPort() throws FaceException {
        final int readOneTimeCount;
        try {
            readOneTimeCount = inputstream.read(readedData, 0, 1024);
            if (readOneTimeCount > 0) {
                if (readedData[0] == 0x11) {
                    byte[] bs = new byte[2];
                    System.arraycopy(readedData, 1, bs, 0, 2);
                    return bs;
                } else {
                    mBaseUSBDataListener.onErr("数据不合格"
                            + readOneTimeCount);
                    return null;
                }
            } else {
                mBaseUSBDataListener.onErr("读取返回量"
                        + readOneTimeCount);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(LinkStatus_Link_Write_Read_Err);
            mProUSBDataListener.onErr("获取USB信息时候出现错误,终止获取");
            throw new FaceException(e.getMessage());
        }
    }


}
