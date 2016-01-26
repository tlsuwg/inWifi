package com.hxuehh.rebirth.capacity.USBcommunication.USBserializ;

import android.annotation.TargetApi;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.os.Build;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.FaceTestforDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.all.usblink.TF_USBLinker_Serialize;
import com.hxuehh.rebirth.all.usblink.WCH_USBLinker_Serialize;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_Serialize;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.DataListener;
import com.hxuehh.rebirth.capacity.USBcommunication.SuUsbAccessory;
import com.hxuehh.rebirth.capacity.USBcommunication.USBSuper;
import com.hxuehh.rebirth.capacity.USBcommunication.USBcommunicationKeys;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.MathUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class USBSerializ extends USBSuper implements Serializable {

    transient int index = 0;
    transient static byte[] zu = new byte[]{1, 2, 3, 4, 5};//315
    transient static byte[] data = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};//315

    static final transient byte[] bytes120_All = new byte[]{
            0x33,
            0x0c,
            0x22,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            0x44};
    static final transient byte[] bytes120_get = new byte[]{0x33, 2, 0x27, 0x44};
    static final transient byte[] bytes120_coreOn = new byte[]{
            0x22,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
            (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
    static final transient byte[] bytes120_coreOff = new byte[]{
            0x22,
            00, 00, 00, 00, 00,
            00, 00, 00, 00, 00};

    static final transient byte[] WCH_Light_MachineSetColors = new byte[]{0x33, 5, 0x28, 1, 1, 1, 0x44};//设置颜色
    static final transient byte[] Type_315_1_0000_0000_0000 = new byte[]{0x33, 12, 0x21,
            1,//电阻
            0, 0, 0, 0,
            0, 0, 0, 0,//地址
            0,//数据位
            0x44};// 315设置
    static final transient byte[] Type_315_1000 = new byte[]{0x33, 4, 0x24, 1, 0x44};//315一直发送
    static final transient byte[] Type_WCH_Rotate_Setting = new byte[]{0x33, 3, 0x29, 1, 0x44};//设置旋转
    static final transient byte[] Type_WCH_Temperature_humidity_Sensor = new byte[]{0x33, 3, 0x2a, 0, 0x44};//获取温湿度
    static final transient byte[] Type_WCH_HongWai_Sensor = new byte[]{0x33, 3, 0x2b, 0, 0x44};//获取红外
    static final transient byte[] Type_WCH_IO2 = new byte[]{0x33, 4, 0x2c, 0, 0, 0x44};//设置 io


    transient USBLinker_Serialize mUSBLinker;

//    transient  private boolean isSettingClose;

    public USBLinker_Serialize getmUSBLinker() {
        return mUSBLinker;
    }

    public transient byte[] ReadBytes;//直接读取到的 已经删除 33 长度  44,只有核心数据

    public byte[] getReadBytes() {
        return ReadBytes;
    }


    public transient byte[] IOInOutBytes40_120;

    public transient byte[] IOInBytes40 = new byte[1];
    public transient byte[] IOOutBytes40 = new byte[4];
    public transient byte[] IOOutBinary40 = new byte[4 * 8];

    public transient byte[] IOInBytes120 = new byte[5];
    public transient byte[] IOOutBytes120 = new byte[10];
    public transient byte[] IOOutBinary120 = new byte[10 * 8];


    public byte[] getIOInBytes40_120() {
        if (this.mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
            return IOInBytes40;
        }
        return IOInBytes120;
    }

    public byte[] getIOOutBytes40_120() {
        return IOInOutBytes40_120;
    }

    public byte[] getIOOutBinary40_120() {
        if (this.mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
            return IOOutBinary40;
        }
        return IOOutBinary120;
    }

    public transient byte temperature, humidity, infraredForBody;//温度湿度人体感应


    @Override
    public boolean isHasTrueWorkerOnDuty() {
        if (mUSBLinker == null || mUSBLinker.getStatus() != USBLinker_.LinkStatus_GetData)
            return false;
        return super.isHasTrueWorkerOnDuty();
    }


    transient USBSerializ.USBSerializParameter USBSerializParameterON = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_ON);
    @Override
    public void onCreat() {
        super.onCreat();
        byte mRotate = (byte) SharedPreferencesUtils.getInteger(SharedPreferencesKeys.Type_WCH_Rotate_SettingTime);
        if (mRotate > 0) {
            this.Rotate = mRotate;
        }
        getInitRGB();
        USBSerializParameterON.setIsFromDoChange(true);

        boolean isMainTest = false;

        if (AppStaticSetting.isTest && isMainTest) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mPluginIOInSensor.onUsed(getmSuUsbAccessoryKey(), USBSerializ.this);
                    boolean is0 = false;
                    byte[] bs1 = new byte[]{0x26, (byte) 0xfe, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
                    byte[] bs2 = new byte[]{0x26, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
                    while (true) {
                        try {
                            Thread.sleep(1000 * 30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (is0) {
                            getmUSBChangeFoundDataListener().onDate(bs2);
                        } else {
                            getmUSBChangeFoundDataListener().onDate(bs1);
                        }
                        is0 = !is0;
                    }
                }
            }).start();
        }
    }

    void getInitRGB() {
        rc = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.WCH_Light_Setting_R);
        gc = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.WCH_Light_Setting_G);
        bc = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.WCH_Light_Setting_B);
    }


    public String getManufacturer, getDescription, getModel, getUri, getSerial, getVersion;
    int describeContents;
    transient DataListener mUSBChangeFoundDataListener;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected void linkUSB(SuUsbAccessory mSUaccessory) {
        UsbAccessory accessory = mSUaccessory.getmUsbAccessory();
        if (mUSBLinker != null && mUSBLinker.getStatus() == USBLinker_.LinkStatus_GetData) {
            Intent intent = new Intent(DeviceCapacityBase.USBLinkDateChange + getType());
            intent.putExtra("change", "已经存在链接的USB，且运行良好，不支持多个USB插入，或者重新插入");
            SuApplication.getInstance().sendBroadcast(intent);
            return;
        }

        FaceCommCallBack mSuCallBackStatus = new FaceCommCallBack() {
            @Override
            public boolean callBack(Object[] oo) {
                if (oo != null && oo.length > 0) {
                    int kk = (int) oo[0];
                    String status = USBLinker_.getStatusName(kk);
                    addDeviceStatus(new RunningStatus(RunningStatus.Running, status));
                }
                return false;
            }
        };


        getManufacturer = accessory.getManufacturer();
        getDescription = accessory.getDescription();
        getModel = accessory.getModel();
        getUri = accessory.getUri();
        getSerial = accessory.getSerial();
        getVersion = accessory.getVersion();
        describeContents = accessory.describeContents();
        if (describeContents <= 0) {
            mSuUsbAccessoryKey = mSUaccessory.getSuUsbAccessoryKey();
        } else {
            mSuUsbAccessoryKey = describeContents;
        }

        String info = "linkUSB  Description描述 " + accessory.getDescription() + ";"
                + "Manufacturer厂商 " + accessory.getManufacturer() + ";"
                + "Model模式 " + accessory.getModel() + ";" + "Serial "
                + accessory.getSerial() + ";" + "Uri " + accessory.getUri()
                + ";" + "Version " + accessory.getVersion() + ";"
                + "describeContents描述内容 " + accessory.describeContents();


//        Description描述 Vinculum Accessory Test;Manufacturer厂商 FTDI;Model模式 FTDIGPIODemo;Serial VinculumAccess
//        ory1;Uri http://www.ftdichip.com;Version 1.0;describeContents描述内容 0

//        linkUSB  Description描述 WCH Accessory Test;Manufacturer厂商 WCH;Model模式 WCHUARTDemo;Serial WCHAccessory1;U
//        ri http://wch.cn;Version 1.0;describeContents描述内容 0


        Su.log(info);
        if (mSuUsbAccessoryKey == USBcommunicationKeys.FT_315M || mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
            mUSBLinker = new TF_USBLinker_Serialize(accessory, mSuCallBackStatus);
        } else {
            mUSBLinker = new WCH_USBLinker_Serialize(accessory, mSuCallBackStatus);
        }

        if (mUSBLinker == null) {
            addDeviceStatus(new RunningStatus(RunningStatus.Running, "链接建立失败"));
            return;
        }

        try {
            mUSBLinker.openAccessory(mUsbManager);
        } catch (FaceException e) {
            e.printStackTrace();
            addDeviceStatus(new RunningStatus(RunningStatus.Running, "连接USB失败" + e.getMessage()));
            return;
        }
        mUSBChangeFoundDataListener = getmUSBChangeFoundDataListener();

        mUSBLinker.setUSBDataListener(null, mUSBChangeFoundDataListener);
        mUSBLinker.setSerializConfig(9600, (byte) 8, (byte) 1, (byte) 0, (byte) 0);
        mUSBLinker.getSerializDateThreadCall();

        super.linkUSB(mSUaccessory);
    }

    private DataListener getmUSBChangeFoundDataListener() {
        if (mUSBChangeFoundDataListener == null)
            mUSBChangeFoundDataListener = new DataListener() {//主动发现变化的
                @Override
                public boolean onDate(final Object[] oos) {
                    if (oos != null && oos.length > 0) {//用suway27命令会回来的 只有一个字节
                        byte getbytes[] = (byte[]) oos[0];
                        if (getbytes.length < 2) return false;
                        ReadBytes = getbytes;
                        byte bb = ReadBytes[0];
                        switch (bb) {
                            case 0x22: {//设置out
                                if (mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
                                    System.arraycopy(getbytes, 1, IOOutBytes40, 0, 4);
                                    IOOutBinary40 = MathUtil.bytesTobit(IOOutBytes40);
                                } else {
                                    System.arraycopy(getbytes, 1, IOOutBytes120, 0, 10);
                                    IOOutBinary120 = MathUtil.bytesTobit(IOOutBytes120);
                                }
                            }
                            break;
                            case 0x27: {//获取
                                IOInOutBytes40_120 = ReadBytes;
                                if (mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
                                    System.arraycopy(ReadBytes, 1, IOInBytes40, 0, 1);
                                    System.arraycopy(ReadBytes, 2, IOOutBytes40, 0, 4);
                                    IOOutBinary40 = MathUtil.bytesTobit(IOOutBytes40);
                                } else {
                                    System.arraycopy(ReadBytes, 1, IOInBytes120, 0, 5);
                                    System.arraycopy(ReadBytes, 6, IOOutBytes120, 0, 10);
                                    IOOutBinary120 = MathUtil.bytesTobit(IOOutBytes120);
                                }
                            }
                            break;
                            case 0x26: {
                                if (mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
                                    IOInBytes40[0] = ReadBytes[1];
                                    if (mPluginIOInSensor != null) {
                                        mPluginIOInSensor.onSensorDataChange(mSuUsbAccessoryKey, IOInBytes40);
                                    }
                                } else {
                                    System.arraycopy(ReadBytes, 1, IOInBytes120, 0, 5);
                                    if (mPluginIOInSensor != null) {
                                        mPluginIOInSensor.onSensorDataChange(mSuUsbAccessoryKey, IOInBytes120);
                                    }
                                }
                            }
                            break;
                            case 0x2a: {
                                temperature = ReadBytes[3];
                                humidity = ReadBytes[0];

                                if (mPluginHumiditySensor != null)
                                    mPluginHumiditySensor.onSensorDataChange(mSuUsbAccessoryKey, humidity);//

                                if (mPluginTemperatureSensor != null)
                                    mPluginTemperatureSensor.onSensorDataChange(mSuUsbAccessoryKey, temperature);//
                            }
                            break;
                            case 0x2b: {
                                infraredForBody = ReadBytes[1];
                                if (mInfrareBodySensor != null)
                                    mInfrareBodySensor.onSensorDataChange(mSuUsbAccessoryKey, infraredForBody);//
                            }
                            break;
                        }
                        if (AppStaticSetting.isTest)
                            addDeviceStatus(new RunningStatus(RunningStatus.Running, "发起" + sendTimes + "收到" + (++getTimes) + ":主动得到数据" + MathUtil.bytesToHexString(ReadBytes, ReadBytes.length)));

                    }
                    return true;
                }

                @Override
                public void onErr(final Object err) {
                    if (err != null) {
                        addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, err + ""));
                    }
                }
            };
        return mUSBChangeFoundDataListener;
    }

    @Override
    protected void onUSB_detached() {
        if (mUSBLinker == null) return;
        mUSBLinker.destroyAccessory(false);
        mAccelerationSensorUnLock100();
        super.onUSB_detached();
    }

    @Override
    protected void destroyAccessory() {
        if (mUSBLinker != null) {
            mUSBLinker.destroyAccessory(true);
        }
        super.destroyAccessory();
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_usb_serializ_ac, false);
    }


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_USB_Serializ;
    }

    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Type_USB_Ser_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }


    transient int sendTimes, getTimes;




    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        super.doChangeStatus(t);

        if (!(t instanceof USBSerializParameter)) throw new FaceException("参数类型出错");
        final USBSerializParameter tt = (USBSerializParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (mUSBLinker == null) throw new FaceException("没有找到usb设备，请重新插拔");

        switch (tt.getType()) {
            case USBSerializParameter.Type_GetUSBLinkStatus: {
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, mUSBLinker.getStatusName());
            }
            break;
            case USBSerializParameter.Type_ON: {
                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData) {
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                }
                if (this.mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
                    byte writeBuffer[] = new byte[]{
                            0x22, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
                    mLightSensorLockOneTimes();
                    mUSBLinker.sendSerializDataBySuWay(writeBuffer.length, writeBuffer);
                } else if (this.mSuUsbAccessoryKey == USBcommunicationKeys.WCH_120IO) {
                    try {
                        mLightSensorLockOneTimes();
                        System.arraycopy(bytes120_coreOn, 0, bytes120_All, 2, bytes120_coreOn.length);
                        mUSBLinker.sendByteNoCare(bytes120_All);
                        if (AppStaticSetting.isTest)
                            addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(bytes120_All, bytes120_All.length)));
                        mUSBChangeFoundDataListener.onDate(bytes120_coreOn);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new FaceException("出错" + e.getMessage());
                    }
                } else if (mSuUsbAccessoryKey == USBcommunicationKeys.WCH_Light_Machine) {
                    //红绿蓝配色
                    isType_WCH_Colour_automatic = false;
                    try {
                        byte[] bbb = WCH_Light_MachineSetColors;
                        if (!tt.isFromDoChange) {
                            getInitRGB();
                        }
                        if (rc == 0 && gc == 0 && bc == 00) {
                            setRGB(AppStaticSetting.RC, AppStaticSetting.GC, AppStaticSetting.BC, bbb);
                        } else {
                            setRGB(rc, gc, bc, bbb);
                        }
                        mLightSensorLockOneTimes();

                        mUSBLinker.sendByteNoCare(bbb);
                        if (AppStaticSetting.isTest)
                            addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(bbb, bbb.length)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new FaceException("出错" + e.getMessage());
                    }

                } else {
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                }
            }
            break;
            case USBSerializParameter.Type_OFF: {
//                if(tt.isCmdLevelLow()){//来自传感器
//                    USBSerializParameter mParameter= (USBSerializParameter) this.get_this_LastDeviceCapacityInParameter();
//                    if(     mParameter!=null
//                            &&(mParameter.getType()==USBSerializParameter.Type_ON
//                            ||mParameter.getType()==USBSerializParameter.Type_Set40IO
//                            ||(mParameter.getType()==USBSerializParameter.Type_WCH_Colour_setting)
//                            ||mParameter.getType()==USBSerializParameter.Type_WCH_Colour_automatic_1
//                            ||mParameter.getType()==USBSerializParameter.Type_WCH_Colour_automatic_2)
//                            && !mParameter.isCmdLevelLow()
//                            ){
//                        setmLastDeviceCapacityInParameter(mParameter);//设置回老的
//                        throw new FaceException("正在执行用户指令，传感器指令失效", FaceException.sensorErrType_SameCMD);
//                    }
//                }

                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData) {
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                }

                if (this.mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
                    byte writeBuffer[] = new byte[]{
                            0x22,
                            00, 00, 00, 00};
                    mLightSensorLockOneTimes();
                    mUSBLinker.sendSerializDataBySuWay(writeBuffer.length, writeBuffer);
                } else if (this.mSuUsbAccessoryKey == USBcommunicationKeys.WCH_120IO) {
                    try {
                        mLightSensorLockOneTimes();
                        System.arraycopy(bytes120_coreOff, 0, bytes120_All, 2, bytes120_coreOff.length);
                        mUSBLinker.sendByteNoCare(bytes120_All);
                        mUSBChangeFoundDataListener.onDate(bytes120_coreOff);
                        if (AppStaticSetting.isTest)
                            addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(bytes120_All, bytes120_All.length)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new FaceException("出错" + e.getMessage());
                    }
                } else if (mSuUsbAccessoryKey == USBcommunicationKeys.WCH_Light_Machine) {
                    isType_WCH_Colour_automatic = false;
                    try {
                        WCH_Light_MachineSetColors[3] = 0;
                        WCH_Light_MachineSetColors[4] = 0;
                        WCH_Light_MachineSetColors[5] = 0;
                        mLightSensorLockOneTimes();
                        mUSBLinker.sendByteNoCare(WCH_Light_MachineSetColors);
                        if (AppStaticSetting.isTest)
                            addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes +
                                    " nocare发送" + MathUtil.bytesToHexString(WCH_Light_MachineSetColors, WCH_Light_MachineSetColors.length)));
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new FaceException("出错" + e.getMessage());
                    }
                } else {
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                }
            }
            break;
//            ==============================================================================================================40
            case USBSerializParameter.Type_Set40IO: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.FT_40IO)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());

                byte[] bs = tt.bs;
                if (bs == null || bs.length == 0) throw new FaceException("传递USB参数出错");
                mLightSensorLockOneTimes();
                mUSBLinker.sendSerializDataBySuWay(bs.length, bs);
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, mUSBLinker.getStatusName());

            }
            case USBSerializParameter.Type_Set120IO: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_120IO)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");

                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());

                byte[] bs = tt.bs;
                if (bs == null || bs.length == 0) throw new FaceException("传递USB参数出错");
                try {
                    System.arraycopy(bs, 0, bytes120_All, 2, bs.length);
                    mLightSensorLockOneTimes();
                    mUSBLinker.sendByteNoCare(bytes120_All);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(bytes120_All, bytes120_All.length)));
                    mUSBChangeFoundDataListener.onDate(bs);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, mUSBLinker.getStatusName());
            }
            break;
            case USBSerializParameter.Type_Get_40_120IOGet: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.FT_40IO
                        && this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_120IO)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");

                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                    if (this.mSuUsbAccessoryKey == USBcommunicationKeys.FT_40IO) {
                        mUSBLinker.sendSerializDataBySuWay(1, new byte[]{0x27});
                    } else {
                        try {
                            mLightSensorLockOneTimes();
                            mUSBLinker.sendByteNoCare(bytes120_get);
                            if (AppStaticSetting.isTest)
                                addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(bytes120_get, bytes120_get.length)));
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new FaceException("出错" + e.getMessage());
                        }
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, IOInOutBytes40_120);
                } else {
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, IOInOutBytes40_120);
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, mUSBLinker.getStatusName());
                }
            }
            break;


            //=============================================================================================================================================315M

//            case USBSerializParameter.Type_Test315All: {
//                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
//                    try {
//                        index=0;
//                        byte[] bbb = new byte[]{0x33, 12, 0x21, 3, 1, 2, 3, 4, 5, 6, 7, 8, 16, 0x44};
//                        byte bs[] = new byte[8];
//                        for ( bs[0] = 0; bs[0]<= 2; bs[0]++) {
//                            for ( bs[1] = 0; bs[1]<= 2; bs[1]++) {
//                                for ( bs[2] = 0; bs[2]<= 2; bs[2]++) {
//                                    for ( bs[3] = 0; bs[3]<= 2; bs[3]++) {
//                                        for ( bs[4] = 0; bs[4]<= 2; bs[4]++) {
//                                            for ( bs[5] = 0; bs[5]<= 2; bs[5]++) {
//                                                for ( bs[6] = 0; bs[6]<= 2; bs[6]++) {
//                                                    for ( bs[7] = 0; bs[7]<= 2; bs[7]++) {
//
//                                                        int kk = 1;
//                                                        for (byte zub : zu) {
//                                                            for (byte datab : data) {
//
//                                                                bbb[2 + kk] = zub;
//                                                                System.arraycopy(bs, 0, bbb, 3 + kk, 8);
//                                                                bbb[11 + kk] = datab;
//                                                                mUSBLinker.sendByteNoCore(bbb);
//                                                                index++;
//
//                                                                try {
//                                                                    Thread.sleep(800);
//                                                                } catch (InterruptedException e) {
//                                                                    e.printStackTrace();
//                                                                }
//                                                            }
//
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, index+"" );
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        throw new FaceException("出错" + e.getMessage());
//                    }
//                } else {
//                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
//                }
//            }
//            break;


            case USBSerializParameter.Type_315_1_0000_0000_0000: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.FT_315M)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());

                try {
                    index = 0;
                    mUSBLinker.sendByteNoCare(Type_315_1_0000_0000_0000);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(Type_315_1_0000_0000_0000,
                                Type_315_1_0000_0000_0000.length)));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }

            }
            break;
            case USBSerializParameter.Type_315_1000: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.FT_315M)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                try {
                    index = 0;
                    mUSBLinker.sendByteNoCare(Type_315_1000);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(Type_315_1000, Type_315_1000.length)));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }
            }
            break;
//====================================================================================================================
            case USBSerializParameter.Type_WCH_Rotate_Setting: {    //旋转
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");

                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());

                try {
                    if (tt.Rotate <= 0) throw new FaceException("不能小于等于0");
                    Rotate = tt.Rotate;
                    SharedPreferencesUtils.putInteger(SharedPreferencesKeys.Type_WCH_Rotate_SettingTime, tt.Rotate);

                    Type_WCH_Rotate_Setting[3] = (byte) tt.Rotate;
                    // 加速度
                    if (tt.Rotate == 0) {
                        mAccelerationSensorUnLock100();
                    } else {
                        mAccelerationSensorLockLong();
                    }

                    mUSBLinker.sendByteNoCare(Type_WCH_Rotate_Setting);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes +
                                " nocare发送" + MathUtil.bytesToHexString(Type_WCH_Rotate_Setting, Type_WCH_Rotate_Setting.length)));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }

            }
            break;
            case USBSerializParameter.Type_WCH_Rotate_Start: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                try {

                    Type_WCH_Rotate_Setting[3] = (byte) Rotate;
                    if (Rotate == 0) {
                        mAccelerationSensorUnLock100();
                    } else {
                        mAccelerationSensorLockLong();
                    }
                    mUSBLinker.sendByteNoCare(Type_WCH_Rotate_Setting);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes +
                                " nocare发送" + MathUtil.bytesToHexString(Type_WCH_Rotate_Setting, Type_WCH_Rotate_Setting.length)));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }

            }
            break;
            case USBSerializParameter.Type_WCH_Rotate_End: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");

                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());

                try {
                    SharedPreferencesUtils.putInteger(SharedPreferencesKeys.Type_WCH_Rotate_SettingTime, tt.Rotate);
                    Type_WCH_Rotate_Setting[3] = 0;
                    mAccelerationSensorUnLock100();
                    mUSBLinker.sendByteNoCare(Type_WCH_Rotate_Setting);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(Type_WCH_Rotate_Setting, Type_WCH_Rotate_Setting.length)));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }

            }
            break;

            case USBSerializParameter.Type_WCH_Colour_setting: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                index = 0;
                if (tt.bs == null || tt.bs.length != 4) throw new FaceException("非法数据");
                rc = tt.bs[1] < 0 ? tt.bs[1] + 256 : tt.bs[1];
                gc = tt.bs[2] < 0 ? tt.bs[2] + 256 : tt.bs[2];
                bc = tt.bs[3] < 0 ? tt.bs[3] + 256 : tt.bs[3];
                if (rc == 0 && gc == 0 && bc == 0) {
                    throw new FaceException("错误的颜色0,0,0");
                }

                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.WCH_Light_Setting_R, rc);
                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.WCH_Light_Setting_G, gc);
                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.WCH_Light_Setting_B, bc);

                doChangeStatus(USBSerializParameterON);
            }
            break;

            case USBSerializParameter.Type_WCH_Temperature_humidity_Sensor: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");

                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                try {
                    mUSBLinker.sendByteNoCare(Type_WCH_Temperature_humidity_Sensor);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(Type_WCH_Temperature_humidity_Sensor, Type_WCH_Temperature_humidity_Sensor.length)));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, "温度" + temperature + "；湿度" + humidity + "%");
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }

            }
            break;
            case USBSerializParameter.Type_WCH_HongWai_Sensor: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");

                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());

                try {
                    mUSBLinker.sendByteNoCare(Type_WCH_HongWai_Sensor);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(Type_WCH_HongWai_Sensor, Type_WCH_HongWai_Sensor.length)));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, infraredForBody);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }

            }
            break;
            case USBSerializParameter.Type_WCH_IO2: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");

                if (mUSBLinker.getStatus() != mUSBLinker.LinkStatus_GetData)
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());

                try {
                    if (tt.bs == null || tt.bs.length != 2) throw new FaceException("非法数据");
                    Type_WCH_IO2[3] = tt.bs[0];
                    Type_WCH_IO2[4] = tt.bs[1];
                    mUSBLinker.sendByteNoCare(Type_WCH_IO2);
                    if (AppStaticSetting.isTest)
                        addDeviceStatus(new RunningStatus(RunningStatus.Running, ++sendTimes + " nocare发送" + MathUtil.bytesToHexString(Type_WCH_IO2, Type_WCH_IO2.length)));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FaceException("出错" + e.getMessage());
                }
            }
            break;

            case USBSerializParameter.Type_WCH_Colour_automatic_show: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                StringBuffer bs = new StringBuffer();
                for (byte b : ReadBytes) {
                    bs.append(b).append(" ");
                }
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, USBisType_WCH_Colour_automaticinfo
                                + StringUtil.N + "光：" + isAdding_R + " " + rc + " " + gc + " " + bc
                                + StringUtil.N + "温度:" + temperature + "；湿度:" + humidity + "%;红外:" + infraredForBody
                );
            }
            break;

            case USBSerializParameter.Type_WCH_Colour_automatic_1: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                if (isType_WCH_Colour_automatic) throw new FaceException("已经执行");
                isType_WCH_Colour_automatic = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] bbb = new byte[]{0x33, 5, 0x28, 1, 1, 1, 0x44};
                        int min = 0;
                        int max = 255;
                        int change = 15;
                        int time = 200;
                        mLightSensorLockLong();
                        Out:
                        while (isType_WCH_Colour_automatic) {
                            int b = min;
                            in1:
                            if (isAdding_R) {
                                for (int r = min; r < max - change; r += change) {
                                    for (int g = min; g < max - change; g += change) {
                                        if (b <= min) {
                                            isAdding_B = true;
                                        } else {
                                            isAdding_B = false;
                                        }
                                        if (isAdding_B) {
                                            for (b = min; b <= max - change; b += change) {
                                                if ((r >= max) && (g >= max) && (b >= max)) {
                                                    isAdding_R = false;
                                                    Su.log("转" + isAdding_R);
                                                    break in1;
                                                }
                                                setRGB(r, g, b, bbb);
                                                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                                                    try {
                                                        mUSBLinker.sendByteNoCare(bbb);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        USBisType_WCH_Colour_automaticinfo = e.getMessage();
                                                        break Out;
                                                    }
                                                } else {
                                                    USBisType_WCH_Colour_automaticinfo = "状态不对";
                                                    break Out;
                                                }

                                                try {
                                                    Thread.sleep(time);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                if (!isType_WCH_Colour_automatic) {
                                                    USBisType_WCH_Colour_automaticinfo = "end";
                                                    break Out;
                                                }
                                            }

                                        } else {
                                            for (b = max; b >= min + change; b -= change) {
                                                if ((r >= max) && (g >= max) && (b <= min)) {
                                                    isAdding_R = true;
                                                    Su.log("转" + isAdding_R);
                                                    break in1;
                                                }
                                                setRGB(r, g, b, bbb);
                                                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                                                    try {
                                                        mUSBLinker.sendByteNoCare(bbb);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        break Out;
                                                    }
                                                } else {
                                                    USBisType_WCH_Colour_automaticinfo = "状态不对";
                                                    break Out;
                                                }

                                                try {
                                                    Thread.sleep(time);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                if (!isType_WCH_Colour_automatic) {
                                                    USBisType_WCH_Colour_automaticinfo = "end";
                                                    break Out;
                                                }

                                            }
                                        }
                                    }
                                }
                            } else {

                                for (int r = max; r > min + change; r -= change) {
                                    for (int g = max; g > min + change; g -= change) {

                                        if (b <= min) {
                                            isAdding_B = true;
                                        } else {
                                            isAdding_B = false;
                                        }

                                        if (!isAdding_B) {
                                            for (b = max; b >= min + change; b -= change) {
                                                if ((r <= min) && (g <= min) && (b <= min)) {
                                                    isAdding_R = true;
                                                    Su.log("转" + isAdding_R);
                                                    break in1;
                                                }
                                                setRGB(r, g, b, bbb);
                                                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                                                    try {
                                                        mUSBLinker.sendByteNoCare(bbb);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        break Out;
                                                    }
                                                } else {
                                                    USBisType_WCH_Colour_automaticinfo = "状态不对";
                                                    break Out;
                                                }

                                                try {
                                                    Thread.sleep(time);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                if (!isType_WCH_Colour_automatic) {
                                                    USBisType_WCH_Colour_automaticinfo = "end";
                                                    break Out;
                                                }
                                            }

                                        } else {//b++
                                            for (b = min; b <= max - change; b += change) {
                                                if ((r <= min) && (g <= min) && (b >= max)) {
                                                    isAdding_R = true;
                                                    Su.log("转" + isAdding_R);
                                                    break in1;
                                                }
                                                setRGB(r, g, b, bbb);
                                                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                                                    try {
                                                        mUSBLinker.sendByteNoCare(bbb);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        break Out;
                                                    }
                                                } else {
                                                    USBisType_WCH_Colour_automaticinfo = "状态不对";
                                                    break Out;
                                                }

                                                try {
                                                    Thread.sleep(time);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }

                                                if (!isType_WCH_Colour_automatic) {
                                                    USBisType_WCH_Colour_automaticinfo = "end";
                                                    break Out;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        isType_WCH_Colour_automatic = false;
                        mLightSensorUnLock100();

                    }
                }).start();

            }
            break;

            case USBSerializParameter.Type_WCH_Colour_automatic_2: {
                if (this.mSuUsbAccessoryKey != USBcommunicationKeys.WCH_Light_Machine)
                    throw new FaceException("非操作类型设备，操作失败,ID错误？");
                if (isType_WCH_Colour_automatic) throw new FaceException("已经执行");
                isType_WCH_Colour_automatic = true;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] bbb = new byte[]{0x33, 5, 0x28, 1, 1, 1, 0x44};
                        int min = 1;
                        int max = 255;
                        byte change = 15;
                        int time = 400;
                        int r = rc;
                        mLightSensorLockLong();

                        Out:
                        while (isType_WCH_Colour_automatic) {
                            bbb[5] = bbb[4] = bbb[3] = (byte) r;
                            bc = gc = rc = r;

                            if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                                try {
                                    mUSBLinker.sendByteNoCare(bbb);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    USBisType_WCH_Colour_automaticinfo = e.getMessage();
                                    break Out;
                                }
                            } else {
                                USBisType_WCH_Colour_automaticinfo = "状态不对";
                                break Out;
                            }

                            try {
                                Thread.sleep(time);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (!isType_WCH_Colour_automatic) {
                                USBisType_WCH_Colour_automaticinfo = "end";
                                break Out;
                            }

                            if (isAdding_R) {
                                if ((r >= max - change)) {
                                    isAdding_R = false;
                                    r = max;
                                    Su.log("转" + isAdding_R);
                                    continue;
                                }
                                r += change;
                            } else {
                                r -= change;
                                if ((r <= min + change)) {
                                    isAdding_R = true;
                                    r = min;
                                    Su.log("转" + isAdding_R);
                                    continue;
                                }
                            }
                        }

                        isType_WCH_Colour_automatic = false;
                        mLightSensorUnLock100();

                    }
                }).start();

            }
        }


        return commonDeviceCapacityOutResult;
    }

    private void setRGB(int r, int g, int b, byte[] bbb) {
        Su.log(r + " " + g + " " + b);
        bbb[3] = (byte) r;
        bbb[4] = (byte) g;
        bbb[5] = (byte) b;
        rc = r;
        gc = g;
        bc = b;
    }

    transient boolean isType_WCH_Colour_automatic;//正在旋转
    transient String USBisType_WCH_Colour_automaticinfo;
    transient int rc = AppStaticSetting.RC, gc = AppStaticSetting.GC, bc = AppStaticSetting.BC;

    transient boolean isAdding_R = true;
    transient boolean isAdding_B = true;
    transient int Rotate = AppStaticSetting.WCH_Rotate_SettingTime;


    public static class USBSerializParameter extends DeviceCapacityInParameter implements Serializable {

        public USBSerializParameter(boolean isAskOn) {
            super(isAskOn);
        }

        transient boolean isFromDoChange;//方法里面调用的 直接可以rbg 不需要再去读取

        public void setIsFromDoChange(boolean isFromDoChange) {
            this.isFromDoChange = isFromDoChange;
        }

        private byte[] bs;
        public static final int Type_GetUSBLinkStatus = 1;
        public static final int Type_Set40IO = 2;
        public static final int Type_Set120IO = 19;
        public static final int Type_Get_40_120IOGet = 3;
        public static final int Type_ON = 4;
        public static final int Type_OFF = 5;
//        ===========================================

        @FaceTestforDlp
        public static final int Type_Test315All = 6;
        public static final int Type_315_1_0000_0000_0000 = 7;
        public static final int Type_315_1000 = 8;

        //===============================================
        public static final int Type_WCH_Rotate_Setting = 9;
        public static final int Type_WCH_Rotate_Start = 14;
        public static final int Type_WCH_Rotate_End = 15;

        public static final int Type_WCH_Colour_setting = 10;//设置定制
        public static final int Type_WCH_Colour_automatic_1 = 11;
        public static final int Type_WCH_Colour_automatic_2 = 12;

        public static final int Type_WCH_Colour_automatic_show = 13;

        //===============================================
        public static final int Type_WCH_Temperature_humidity_Sensor = 16;//温度湿度传感
        public static final int Type_WCH_IO2 = 17;//温度湿度传感
        public static final int Type_WCH_HongWai_Sensor = 18;//红外


        public USBSerializParameter(int type) {
            super(type);
        }

        public USBSerializParameter(int type, byte[] bs) {
            super(type);
            this.bs = bs;
        }

        int Rotate;

        public USBSerializParameter(int type, int Rotate) {
            super(type);
            this.Rotate = Rotate;
        }
    }


}
