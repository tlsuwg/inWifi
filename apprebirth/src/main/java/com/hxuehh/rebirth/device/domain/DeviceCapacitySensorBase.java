package com.hxuehh.rebirth.device.domain;

/**
 * Created by suwg on 2015/11/4.
 */

//一般都是USB类型带来的
//    比如温湿度，IO类型处理
public abstract class DeviceCapacitySensorBase extends DeviceCapacityBase {

    public abstract void onSensorDataChange(int type, byte... bytes);//数据来了变化了

    transient protected DeviceCapacityBase mDeviceCapacityBase;//这个是依赖的，也可以说是父类传感
//    设置可用 不可用状态  拔出就必须设置null


    boolean isDevEnable;

    @Override
    public boolean isDevEnable() {
        return isDevEnable;
    }

    @Override
    public boolean isShowInClient() {
        return isDevEnable;
    }

    @Override
    public String getDevUnEnableInfo() {
        return isDevEnable ? "设备已经插入，未知原因" : "没有插入USB配件";
    }


    public abstract boolean getAdaptiveByFatherUSBSuUsbAccessoryKey(int fatherUSBSuUsbAccessoryKey);


    public void onUsed(int fatherUSBSuUsbAccessoryKey, DeviceCapacityBase mDeviceCapacityBase) {
        this.mDeviceCapacityBase = mDeviceCapacityBase;
        isDevEnable = getAdaptiveByFatherUSBSuUsbAccessoryKey(fatherUSBSuUsbAccessoryKey);
        if (isDevEnable) {
            notifyStatusChange();
        }
    }

    public void onUnused() {
        this.mDeviceCapacityBase = null;
        isDevEnable = false;
        notifyStatusChange();
    }


}
