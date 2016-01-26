package com.hxuehh.rebirth.capacity.USBcommunication.USBGPIO;

import android.content.Intent;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.all.usblink.WCH_USBLinker_GPIO;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_GPIO;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.DataListener;
import com.hxuehh.rebirth.capacity.USBcommunication.SuUsbAccessory;
import com.hxuehh.rebirth.capacity.USBcommunication.USBSuper;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class USBGPIO extends USBSuper {

    transient USBLinker_GPIO mUSBLinker;

    public USBLinker_ getmUSBLinker() {
        return mUSBLinker;
    }

    transient byte[] IObs;

    public byte[] getIObs() {
        return IObs;
    }


    @Override
    public boolean isHasTrueWorkerOnDuty() {
        if (mUSBLinker == null||mUSBLinker.getStatus()!=USBLinker_.LinkStatus_GetData) return false;
        return super.isHasTrueWorkerOnDuty();
    }

    @Override
    public String getDevUnEnableInfo() {
        return isDevEnable?"设备已经插入，未知原因":"没有插入USB配件,可免费获取";
    }

    @Override
    protected void onUSB_detached() {
        if(mUSBLinker==null)return;
        mUSBLinker.destroyAccessory(false);
        mUSBLinker=null;
    }

    protected void linkUSB(SuUsbAccessory accessory) {
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

        mUSBLinker = new WCH_USBLinker_GPIO(accessory.getmUsbAccessory(), mSuCallBackStatus);
        try {
            mUSBLinker.openAccessory(mUsbManager);
        } catch (FaceException e) {
            e.printStackTrace();
            addDeviceStatus(new RunningStatus(RunningStatus.Running, "连接USB失败" + e.getMessage()));
            return;
        }

        DataListener mUSBDataListener = new DataListener() {
            @Override
            public boolean onDate(final Object[] bs) {
                return true;
            }

            @Override
            public void onErr(final Object err) {
                if (err != null) {
                    addDeviceStatus(new RunningStatus(RunningStatus.RunTimeErr, err + ""));
                }
            }
        };
        mUSBLinker.setUSBDataListener(null, mUSBDataListener);
//        mUSBLinker.setSerializConfig(9600, (byte) 8, (byte) 1, (byte) 0, (byte) 0);
//        mUSBLinker.getSerializDateThreadCall();

        mUSBLinker.resetGPIOPort();
        mUSBLinker.configGPIOPort(0xe8, 0xff17);
        int outData = 0xe8;
        mUSBLinker.setGPIOPort(outData);

    }

    protected void destroyAccessory() {
        if (mUSBLinker != null) {
            mUSBLinker.destroyAccessory(true);
        }
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_usb_GPIO_ac, false);
    }


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_USB_GPIO;
    }

    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Type_USB_GPIO_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        super.doChangeStatus(t);
        if (!(t instanceof USBGPIOParameter)) throw new FaceException("参数类型出错");
        final USBGPIOParameter tt = (USBGPIOParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (mUSBLinker == null) throw new FaceException("没有找到usb设备，请重新插拔");
        switch (tt.getType()) {
            case USBGPIOParameter.Type_GetStatus: {
                commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, mUSBLinker.getStatusName());
            }
            break;
            case USBGPIOParameter.Type_SetIO: {
                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                    mLightSensorLockOneTimes();
                    mUSBLinker.setGPIOPort(tt.setIO);
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, mUSBLinker.getStatusName());
                } else {
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                }
            }
            break;
            case USBGPIOParameter.Type_GetIO: {
                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                    byte bs[] = mUSBLinker.readGPIOPort();
                    if (bs != null) {
                        this.IObs = bs;
                        commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, IObs);
                    } else {
                        throw new FaceException("没有获取到当前状态");
                    }
                } else {
                    if (IObs != null) {
                        commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, IObs);
                    } else {
                        throw new FaceException("没有获取到当前IO");
                    }
                    commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, mUSBLinker.getStatusName());
                }
            }
            break;
            case USBGPIOParameter.Type_ON: {
                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                    int outData = 0x00;
                    outData &= 0xe8;
                    mLightSensorLockOneTimes();
                    mUSBLinker.setGPIOPort(outData);
                } else {
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                }
            }
            break;
            case USBGPIOParameter.Type_OFF: {
//                if(tt.isCmdLevelLow()){//来自传感器
//                    USBGPIOParameter mParameterlast= (USBGPIOParameter) this.get_this_LastDeviceCapacityInParameter();
//                    if(     mParameterlast!=null
//                            &&(mParameterlast.getType()==USBGPIOParameter.Type_ON||mParameterlast.getType()==USBGPIOParameter.Type_SetIO)
//                            && !mParameterlast.isCmdLevelLow()
//                            ){
//                        setmLastDeviceCapacityInParameter(mParameterlast);//设置回老的
//                        throw new FaceException("正在执行用户指令，传感器指令失效", FaceException.sensorErrType_SameCMD);
//
//                    }
//                }

                if (mUSBLinker.getStatus() == mUSBLinker.LinkStatus_GetData) {
                    int outData = 0xe8;
                    outData &= 0xe8;
                    mLightSensorLockOneTimes();
                    mUSBLinker.setGPIOPort(outData);
                } else {
                    throw new FaceException("USB当前状态:" + mUSBLinker.getStatusName());
                }
            }
            break;
        }

        return commonDeviceCapacityOutResult;
    }




    public static class USBGPIOParameter extends DeviceCapacityInParameter implements Serializable {
        public USBGPIOParameter(boolean isAskOn) {
            super(isAskOn);
        }

        private int setIO;
        public static final int Type_GetStatus = 1;
        public static final int Type_SetIO = 2;
        public static final int Type_GetIO = 3;
        public static final int Type_ON = 4;
        public static final int Type_OFF = 5;

        String names[] = new String[]{"获取运行状态", "设置IO", "获取IO情况", "全亮", "全暗"};

        @Override
        public String[] getAllNames() {
            return names;
        }

        public USBGPIOParameter(int type) {
            super(type);
        }

        public USBGPIOParameter(int type, int setIO) {
            super(type);
            this.setIO = setIO;
        }

        @Override
        public String getTypeName() {
            int type = getType() + 1;
            if (type > 0 && names.length > type) {
                return names[type];
            }
            return super.getTypeName();
        }
    }


}
