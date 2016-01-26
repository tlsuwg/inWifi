package com.hxuehh.rebirth.all.usblink.linkAbstract;

import android.annotation.TargetApi;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.capacity.DataListener;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.MathUtil;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


//此类不能传输较大数据流 只能作为一个 独立数据块 进行通信
//可序列化
public abstract class USBLinker_ {

    public final static int LinkStatus_Accessory_Open = 11;
    public final static int LinkStatus_Accessory_OpenErr = 12;
    public final static int LinkStatus_GetData = 13;
    public final static int LinkStatus_Link_Write_Read_Err = 14;
    public final static int LinkStatus_Close = 15;


    private UsbAccessory accessory;
    private ParcelFileDescriptor filedescriptor = null;
    protected FileInputStream inputstream = null;
    private FileOutputStream outputstream = null;
    protected DataListener mProUSBDataListener, mBaseUSBDataListener;


    protected byte[] readedData;
    protected byte[] writeData;

    int status;

    public int getStatus() {
        return status;
    }

    public String getStatusName() {
        return getStatusName(status);
    }

    protected void setStatus(int status) {
        this.status = status;
        if (mcallBackForStatus != null)
            mcallBackForStatus.callBack(status);
    }

    public static String getStatusName(int key) {
        switch (key) {
            case LinkStatus_Accessory_Open:
                return "已经获取链接";
            case LinkStatus_Accessory_OpenErr:
                return "获取链接失败";
            case LinkStatus_GetData:
                return "正在运行，读写可用";
            case LinkStatus_Link_Write_Read_Err:
                return "出现读写失误";
            case LinkStatus_Close:
                return "usb读写关闭";
            default:
                return "链接未知状态";
        }
    }


    FaceCommCallBack mcallBackForStatus;

    public USBLinker_(UsbAccessory accessory, FaceCommCallBack mcallBackForStatus) {
        this.mcallBackForStatus = mcallBackForStatus;
        this.accessory = accessory;
        readedData = new byte[1024];
        writeData = new byte[256];
    }

    public void setUSBDataListener(DataListener mBaseUSBDataListener, DataListener mProUSBDataListener) {
        this.mProUSBDataListener = mProUSBDataListener;
        if (mBaseUSBDataListener != null) {
            this.mBaseUSBDataListener = mBaseUSBDataListener;
        } else {
            this.mBaseUSBDataListener = mThisUSBDataListenerForBase;
        }
    }


    protected boolean writeIntoUSB(int numBytes) {
        try {
            if (outputstream != null) {
                outputstream.write(writeData, 0, numBytes);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            setStatus(LinkStatus_Link_Write_Read_Err);
            mProUSBDataListener.onErr("写入时出错");
        }
        return false;
    }

    public void sendByteNoCare(byte[] bs) throws IOException {
        if (outputstream != null) {
            outputstream.write(bs);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void openAccessory(UsbManager usbmanager) throws FaceException {
        try {
            filedescriptor = usbmanager.openAccessory(accessory);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (filedescriptor != null) {
            FileDescriptor fd = filedescriptor.getFileDescriptor();
            inputstream = new FileInputStream(fd);
            outputstream = new FileOutputStream(fd);
            /* check if any of them are null */
            if (inputstream == null || outputstream == null) {
                throw new FaceException("没有打开 inputstream  outputstream");
            }
            status = (LinkStatus_Accessory_Open);//不能使用set方法，防止回调
        } else {
            setStatus(LinkStatus_Accessory_OpenErr);
            throw new FaceException("没有打开 filedescriptor");
        }
    }

    protected ReadThreadBySuWay mReadThread;

    public class ReadThreadBySuWay extends Thread {
        FileInputStream instream;

        ReadThreadBySuWay(FileInputStream stream) {
            instream = stream;
            this.setPriority(Thread.MAX_PRIORITY);
        }

        public void run() {
            if (getStatus() == LinkStatus_Accessory_Open) {
                setStatus(LinkStatus_GetData);
            }
            while (!this.isInterrupted()) {
                try {
                    final int readOneTimeCount = instream.read(readedData, 0, 1024);
                    if (readOneTimeCount > 0) {
                        byte[] bs = new byte[readOneTimeCount];
                        System.arraycopy(readedData, 0, bs, 0, readOneTimeCount);
                        mBaseUSBDataListener.onDate(bs);
                        mProUSBDataListener.onErr("原始数据" + MathUtil.bytesToHexString(bs, bs.length));
                    } else {
                        mBaseUSBDataListener.onErr("读取返回量"
                                + readOneTimeCount);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    setStatus(LinkStatus_Link_Write_Read_Err);
                    if (!this.isInterrupted()) {
                        mProUSBDataListener.onErr("获取USB信息时候出现错误,终止获取");
                        break;
                    }
                }
            }
            destroyAccessory(false);
        }
    }


    public void destroyAccessory(boolean haslink) {
        setStatus(LinkStatus_Close);
        if (mReadThread != null) {
            mReadThread.interrupted();
        }
    }

    protected void CloseAccessory() {
        try {
            if (filedescriptor != null)
                filedescriptor.close();
        } catch (IOException e) {
        }
        try {
            if (inputstream != null)
                inputstream.close();
        } catch (IOException e) {
        }

        try {
            if (outputstream != null)
                outputstream.close();
        } catch (IOException e) {
        }

        filedescriptor = null;
        inputstream = null;
        outputstream = null;
    }

    DataListener mThisUSBDataListenerForBase = new DataListener() {
        int nendLength;
        boolean isWaitData;
        boolean isInit = true;
        boolean isWaitGetLength;
        byte[] bytes;
        int hasGet = 0;

        @Override
        public boolean onDate(final Object[] bs) {
            if (bs != null) {
                if (bs != null && bs.length >= 1) {
                    final byte[] bss = (byte[]) bs[0];
                    if (bss.length >= 1) {
                        if (isInit) {
                            for (int i = 0; i < bss.length; i++) {
                                if (bss[i] == 0x33) {
                                    isInit = false;
                                    isWaitGetLength = true;
                                    int left = bss.length - i - 1;//去掉33
                                    if (left >= 1) {
                                        byte[] leftbytes = new byte[left];
                                        System.arraycopy(bss, i + 1, leftbytes,
                                                0, left);
                                        mThisUSBDataListenerForBase.onDate(
                                                leftbytes, false);
                                    }
                                    break;
                                }
                            }
                        } else if (isWaitGetLength) {
                            nendLength = bss[0];
                            isWaitGetLength = false;
                            isWaitData = true;
                            hasGet = 0;
                            bytes = new byte[nendLength - 1];//去掉44
                            if (bss.length - 1 > 0) {
                                int length = bss.length - 1;
                                byte[] left = new byte[length];
                                System.arraycopy(bss, 1, left, 0, length);
                                mThisUSBDataListenerForBase.onDate(left, false);
                            }

                        } else if (isWaitData) {
                            int need = nendLength - hasGet;
                            if (bss.length >= need) {// 够了
                                System.arraycopy(bss, 0, bytes, hasGet, need - 1);//直接去掉44了
                                if(bss[need-1]==0x44){
                                    final byte[] bytes2 = bytes.clone();
                                    mProUSBDataListener.onDate(bytes2);//完成一批
                                }else{
                                    mProUSBDataListener.onErr("出错了 end不是44");
                                }
                                isInit = true;
                                isWaitData = false;
                                if (bss.length > need) {// 还有剩余
                                    int length = bss.length - need;
                                    byte[] left = new byte[length];
                                    System.arraycopy(bss, need, left, 0, length);
                                    mThisUSBDataListenerForBase.onDate(left, false);
                                }
                            } else {// 还是不足
                                // System.arraycopy(src, srcPos, dst, dstPos,
                                // length);
                                System.arraycopy(bss, 0, bytes, hasGet,
                                        bss.length);
                                hasGet += bss.length;
                            }
                        }
                    }
                }
            }

            return true;
        }

        @Override
        public void onErr(final Object err) {

        }
    };






}
