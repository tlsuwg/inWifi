package com.hxuehh.rebirth.all.usblink;

import android.hardware.usb.UsbAccessory;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_Serialize;


//此类不能传输较大数据流 只能作为一个 独立数据块 进行通信
//可序列化
public class WCH_USBLinker_Serialize extends USBLinker_Serialize {

    public WCH_USBLinker_Serialize(UsbAccessory accessory, FaceCommCallBack mcallBackForStatus) {
        super(accessory, mcallBackForStatus);
    }

    @Override
    public void destroyAccessory(boolean haslink) {
        super.destroyAccessory(haslink);
        if (haslink) { // 貌似是发个命令
            setSerializConfig(9600, (byte) 8, (byte) 1, (byte) 0, (byte) 0);  // send default setting data for config
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
        CloseAccessory();
    }


    public void setSerializConfig(int baud, byte dataBits, byte stopBits, byte parity, byte flowControl) {
        byte tmp = 0x00;
        byte baudRate_byte = 0x00;

        writeData[0] = 0x30;

        switch (baud) {
            case 300:
                baudRate_byte = 0x00;
                break;
            case 600:
                baudRate_byte = 0x01;
                break;
            case 1200:
                baudRate_byte = 0x02;
                break;
            case 2400:
                baudRate_byte = 0x03;
                break;
            case 4800:
                baudRate_byte = 0x04;
                break;
            case 9600:
                baudRate_byte = 0x05;
                break;
            case 19200:
                baudRate_byte = 0x06;
                break;
            case 38400:
                baudRate_byte = 0x07;
                break;
            case 57600:
                baudRate_byte = 0x08;
                break;
            case 115200:
                baudRate_byte = 0x09;
                break;
            case 230400:
                baudRate_byte = 0x0A;
                break;
            case 460800:
                baudRate_byte = 0x0B;
                break;
            case 921600:
                baudRate_byte = 0x0C;
                break;
            default:
                baudRate_byte = 0x05;
                break; // default baudRate "9600"
        }
        // prepare the baud rate buffer
        writeData[1] = baudRate_byte;

        switch (dataBits) {
            case 5:
                tmp |= 0x00;
                break;  //reserve
            case 6:
                tmp |= 0x01;
                break;  //reserve
            case 7:
                tmp |= 0x02;
                break;
            case 8:
                tmp |= 0x03;
                break;
            default:
                tmp |= 0x03;
                break; // default data bit "8"
        }

        switch (stopBits) {
            case 1:
                tmp &= ~(1 << 2);
                break;
            case 2:
                tmp |= (1 << 2);
                break;
            default:
                tmp &= ~(1 << 2);
                break; // default stop bit "1"
        }

        switch (parity) {
            case 0:
                tmp &= ~((1 << 3) | (1 << 4) | (1 << 5));
                break; //none
            case 1:
                tmp |= (1 << 3);
                break; //odd
            case 2:
                tmp |= ((1 << 3) | (1 << 4));
                break; //event
            case 3:
                tmp |= ((1 << 3) | (1 << 5));
                break; //mark
            case 4:
                tmp |= ((1 << 3) | (1 << 4) | (1 << 5));
                break; //space
            default:
                tmp &= ~((1 << 3) | (1 << 4) | (1 << 5));
                break;//default parity "NONE"
        }

        switch (flowControl) {
            case 0:
                tmp &= ~(1 << 6);
                break;
            case 1:
                tmp |= (1 << 6);
                break;
            default:
                tmp &= ~(1 << 6);
                break; //default flowControl "NONE"
        }
        // dataBits, stopBits, parity, flowControl
        writeData[2] = tmp;

        writeData[3] = 0x00;
        writeData[4] = 0x00;

        writeIntoUSB((int) 5);
    }


    public boolean sendSerializDataBySuWay(int numBytes, byte[] buffer) {

        int have_send = 0;

		/*
         * if num bytes are more than maximum limit
		 */
        if (numBytes < 1) {
			/*return the status with the error in the command*/
            return false;
        }

		/*check for maximum limit*/
        if (numBytes > 252) {
            numBytes = 252;
        }

        boolean isSendOK = false;
        if (numBytes >= 64) {
            int retval = (numBytes / 63);

            for (int i = 0; i < retval; i++) {
                for (int count_x = 0; count_x < 63; count_x++) {
                    writeData[count_x] = buffer[have_send++];
                }
                isSendOK = writeIntoUSB(63);
            }

            if ((numBytes - (retval * 63)) > 0) {
                for (int count_y = 0; count_y < (numBytes - (retval * 63)); count_y++) {
                    writeData[count_y] = buffer[have_send++];
                }
                isSendOK = writeIntoUSB(numBytes - (retval * 63));
            }
        } else if (numBytes < 64) {
			/*prepare the packet to be sent*/
            for (int count_z = 0; count_z < numBytes; count_z++) {
                writeData[count_z] = buffer[count_z];
            }
//            isSendOK = writeIntoUSB(numBytes);
            isSendOK = writeIntoUSB(numBytes);
        }

        if (!isSendOK) return false;
        mProUSBDataListener.onDate(buffer, true);
        return isSendOK;
    }

}
