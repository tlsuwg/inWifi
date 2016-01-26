package com.hxuehh.rebirth.all.usblink;

import android.hardware.usb.UsbAccessory;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_Serialize;


//此类不能传输较大数据流 只能作为一个 独立数据块 进行通信
//可序列化
public class TF_USBLinker_Serialize extends USBLinker_Serialize {

    public TF_USBLinker_Serialize(UsbAccessory accessory, FaceCommCallBack mcallBackForStatus) {
        super(accessory, mcallBackForStatus);
    }

    @Override
    public void destroyAccessory(boolean haslink) {
        super.destroyAccessory(haslink);
        if (haslink) { // 貌似是发个命令
            writeData[0] = 0; // send dummy data for instream.read going
            writeIntoUSB(1);
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
        CloseAccessory();
    }


    public void setSerializConfig(int baud, byte dataBits, byte stopBits, byte parity, byte flowControl) {
        writeData[0] = (byte) baud;
        writeData[1] = (byte) (baud >> 8);
        writeData[2] = (byte) (baud >> 16);
        writeData[3] = (byte) (baud >> 24);
        writeData[4] = dataBits;
        writeData[5] = stopBits;
        writeData[6] = parity;
        writeData[7] = flowControl;
        writeIntoUSB((int) 8);
    }


    public boolean sendSerializDataBySuWay(int numBytes, byte[] buffer) {
//        if (numBytes < 1)
//            return false;
//        if (numBytes > 256) {
//            numBytes = 256;
//        }
        writeData[0] = 0x33;
        writeData[1] = (byte) (buffer.length + 1);
        System.arraycopy(buffer, 0, writeData, 2, numBytes);
        writeData[numBytes + 2] = 0x44;
//        boolean isSendOK=false;
//        if (numBytes != 64) {// 不能直接发64
//            isSendOK = writeIntoUSB(numBytes+2);
//        } else {
//            byte temp = writeData[63];
//            isSendOK = writeIntoUSB(63);
//            writeData[0] = temp;
//            isSendOK = writeIntoUSB(1);
//        }
        boolean isSendOK = writeIntoUSB(numBytes + 3);
        if (!isSendOK) return false;
        mProUSBDataListener.onDate(buffer, true);
        return isSendOK;
    }

}
