package com.hxuehh.rebirth.capacity.USBcommunication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.capacity.pluginHumiditySensor.PluginHumiditySensor;
import com.hxuehh.rebirth.capacity.pluginIOInSensor.PluginIOInSensor;
import com.hxuehh.rebirth.capacity.pluginInfrareBodySensor.PluginInfrareBodySensor;
import com.hxuehh.rebirth.capacity.pluginTemperatureSensor.PluginTemperatureSensor;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacitySensorBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suwg on 2015/8/15.
 */
//等待接受 链接，然后连接

public abstract class USBSuper extends DeviceCapacityBase implements Serializable {


    protected transient UsbManager mUsbManager;
    transient boolean isLog;
    transient BroadcastReceiver mUsbDestroyReceiver_out;
    transient BroadcastReceiver mBroadcastReceiver_In;

   protected int mSuUsbAccessoryKey;//标示
    public int getmSuUsbAccessoryKey() {
        return mSuUsbAccessoryKey;
    }

    public void setmSuUsbAccessoryKey(int mSuUsbAccessoryKey) {
        this.mSuUsbAccessoryKey = mSuUsbAccessoryKey;
    }

    private void setReviceLink() {
        if (mBroadcastReceiver_In == null) {
            mBroadcastReceiver_In = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    UsbAccessory accessory = intent.getParcelableExtra("accessory");
                    int mSuUsbAccessoryKey = intent.getIntExtra("SuUsbAccessoryKey", -1);
                    SuUsbAccessory mSuUsbAccessory = new SuUsbAccessory(accessory, mSuUsbAccessoryKey);
                    isLog = intent.getBooleanExtra("log", false);
                    linkUSB(mSuUsbAccessory);
                }
            };
            SuApplication.getInstance().registerReceiver(mBroadcastReceiver_In, new IntentFilter(DeviceCapacityBase.USBLinkAction + getType()));
        }
    }


    @Override
    public boolean isShowStatus() {
        return false;
    }

    @Override
    public void addDeviceStatus(RunningStatus mDeviceStatus) {
        super.addDeviceStatus(mDeviceStatus);
        if (isLog) {
            Intent intent = new Intent(DeviceCapacityBase.USBLinkDateChange + getType());
            intent.putExtra("change", mDeviceStatus.getInfo());
            SuApplication.getInstance().sendBroadcast(intent);
        }
    }

    @Override
    public boolean testHardware_SDK() {
        try {
            mUsbManager = (UsbManager) SuApplication.getInstance().getSystemService(Context.USB_SERVICE);
        } catch (Exception e) {
            return false;
        }
        return true;
    }//必须是true


    private void addUSBDetachListener() {
        if (mUsbDestroyReceiver_out == null) {
            mUsbDestroyReceiver_out = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                        addDeviceStatus(new RunningStatus(RunningStatus.End, "设备拔出"));
                        onUSB_detached();
                    }
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);// 监听拔出
            SuApplication.getInstance().registerReceiver(mUsbDestroyReceiver_out, filter);
        }
    }


    private void setReviceLog() {
        if (mUsbDestroyReceiver_log == null) {
            mUsbDestroyReceiver_log = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    isLog = intent.getBooleanExtra("log", false);
                }
            };
            SuApplication.getInstance().registerReceiver(mUsbDestroyReceiver_log, new IntentFilter(DeviceCapacityBase.USBLinkLog + getType()));
        }
    }

    transient BroadcastReceiver mUsbDestroyReceiver_log;


    @Override
    public void onCreat() {
        setReviceLink();
        addUSBDetachListener();
        setReviceLog();
    }

    @Override
    public void onDestry() {
        stop();
    }


    @Override
    public void stop() {
        SuApplication.getInstance().unregisterReceiver(mBroadcastReceiver_In);
        SuApplication.getInstance().unregisterReceiver(mUsbDestroyReceiver_log);
        SuApplication.getInstance().unregisterReceiver(mUsbDestroyReceiver_out);
        destroyAccessory();
    }

    protected boolean isDevEnable;

    @Override
    public boolean isDevEnable() {
        return isDevEnable;
    }

    @Override
    public String getDevUnEnableInfo() {
        return isDevEnable ? "设备已经插入，未知原因" : "没有插入USB配件";
    }

    protected void onUSB_detached() {
        isDevEnable = false;
        notifyStatusChange();
        if (listPlugins != null && listPlugins.size() > 0) {
            for (DeviceCapacitySensorBase mDeviceCapacitySensorBase : listPlugins) {
                mDeviceCapacitySensorBase.onUnused();
            }
        }
    }

    ;

    protected void linkUSB(SuUsbAccessory accessory) {//这个需要放在最后
        isDevEnable = true;
        notifyStatusChange();
        if (listPlugins != null && listPlugins.size() > 0)
            for (DeviceCapacitySensorBase mDeviceCapacitySensorBase : listPlugins) {
                mDeviceCapacitySensorBase.onUsed(getmSuUsbAccessoryKey(),this);
            }
    }

    protected void destroyAccessory() {
        isDevEnable = false;
        notifyStatusChange();
        if (listPlugins != null && listPlugins.size() > 0) {
            for (DeviceCapacitySensorBase mDeviceCapacitySensorBase : listPlugins) {
                mDeviceCapacitySensorBase.onUnused();
            }
        }
    }




    transient protected DeviceCapacitySensorBase mInfrareBodySensor;
    transient protected DeviceCapacitySensorBase mPluginHumiditySensor;
    transient protected DeviceCapacitySensorBase mPluginTemperatureSensor;
    transient protected DeviceCapacitySensorBase mPluginIOInSensor;

    transient protected List<DeviceCapacitySensorBase> listPlugins = new ArrayList();
    public List<DeviceCapacitySensorBase> getListPlugins() {
        return listPlugins;
    }

    public void setProductionSensor(PluginInfrareBodySensor mInfrareBodySensor,
                                    PluginHumiditySensor mPluginHumiditySensor, PluginTemperatureSensor mPluginTemperatureSensor,
                                    PluginIOInSensor mPluginIOInSensor) {//可以生产出来的传感

        this.mInfrareBodySensor = mInfrareBodySensor;
        this.mPluginHumiditySensor = mPluginHumiditySensor;
        this.mPluginTemperatureSensor = mPluginTemperatureSensor;
        this.mPluginIOInSensor = mPluginIOInSensor;

        listPlugins = new ArrayList();
        listPlugins.add(mInfrareBodySensor);
        listPlugins.add(mPluginHumiditySensor);
        listPlugins.add(mPluginTemperatureSensor);
        listPlugins.add(mPluginIOInSensor);
    }

}
