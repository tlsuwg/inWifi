package com.hxuehh.rebirth.capacity.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class Battery extends DeviceCapacityBase {

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_Battery;
    }

    @Override
    public boolean testHardware_SDK() throws FaceException {
        return true;
    }


    @Override
    public boolean isDevEnable() {
        return true;
    }

    @Override
    public String getDevUnEnableInfo() {
        return null;
    }

    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult=   super.doChangeStatus(t);        if(mDeviceCapacityOutResult!=null)return mDeviceCapacityOutResult;
        if ( !(t instanceof BatteryParameter)) throw new FaceException("参数类型出错");
        final BatteryParameter tt = (BatteryParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.isAskStatus()) {
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, statusToString() );
            return commonDeviceCapacityOutResult;
        }
        return commonDeviceCapacityOutResult;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Battery_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }


    @Override
    public void onCreat() throws FaceException {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        SuApplication.getInstance().registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {
        stop();
    }

    @Override
    public boolean isShowStatus() {
        return true;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_battery_ac, false);
    }


    int intLevel;
    int intScale;
    int voltage;
    int temperature;

    String BatteryStatus, BatteryStatus_plugged, BatteryTemp_health;


    public String statusToString() {
        StringBuffer sb=new StringBuffer();
        sb.append("剩余电量：").append(intLevel).append(StringUtil.N);
        sb.append("电压(mv)：").append(voltage).append(StringUtil.N);
        sb.append("温度：").append(temperature/10f).append(StringUtil.N);
        sb.append("电池状态：").append(BatteryStatus).append(StringUtil.N);
        sb.append("充电类型：").append(BatteryStatus_plugged).append(StringUtil.N);
        sb.append("电池健康情况：").append(BatteryTemp_health).append(StringUtil.N);

        return sb.toString();
    }

    private transient BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /*
             * 如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()
             */
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                intLevel = intent.getIntExtra("level", 0);
                intScale = intent.getIntExtra("scale", 100);
                voltage = intent.getIntExtra("voltage", 0);
                temperature = intent.getIntExtra("temperature", 0);

                switch (intent.getIntExtra("status",
                        BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        BatteryStatus = "充电状态";
                        break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING:
                        BatteryStatus = "放电状态";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        BatteryStatus = "未充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        BatteryStatus = "充满电";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        BatteryStatus = "未知道状态";
                        break;
                }

                switch (intent.getIntExtra("plugged",
                        BatteryManager.BATTERY_PLUGGED_AC)) {
                    case BatteryManager.BATTERY_PLUGGED_AC:
                        BatteryStatus_plugged = "AC充电";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB:
                        BatteryStatus_plugged = "USB充电";
                        break;
                }

                switch (intent.getIntExtra("health",
                        BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        BatteryTemp_health = "未知错误";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        BatteryTemp_health = "状态良好";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        BatteryTemp_health = "电池没有电";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        BatteryTemp_health = "电池电压过高";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        BatteryTemp_health = "电池过热";
                        break;
                }

            }
        }

    };

    public boolean isCharging() {
        return "充电状态".equals(BatteryStatus);
    }


    public static class BatteryParameter extends DeviceCapacityInParameter implements Serializable {
        public BatteryParameter(boolean isask) {
            super(isask);
        }
    }


}
