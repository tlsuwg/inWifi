package com.hxuehh.rebirth.capacity.storage;

import android.os.RemoteException;
import android.os.StatFs;
import android.view.View;

import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
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
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class Storage extends DeviceCapacityBase {

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_storage;
    }

    @Override
    public boolean testHardware_SDK() {

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
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Storage_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    @Override
    public void onCreat() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {

    }

    @Override
    public boolean isShowStatus() {
        return false;
    }


    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if ( !(t instanceof StorageParameter)) throw new FaceException("参数类型出错");
        final StorageParameter tt = (StorageParameter) t;
        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        if (tt.isAskStatus()) {
            StatFs sd = FileUtil.SDCardSizeStatFs();
            String info = "本机：" + FileUtil.getInfo(FileUtil.readSystem()) + (sd == null ? "" : StringUtil.N + "SD" + FileUtil.getInfo(sd));
            commonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, info);
            return commonDeviceCapacityOutResult;
        } else {
            switch (tt.type) {

            }
        }

        return commonDeviceCapacityOutResult;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_storage_ac, false);
    }



    @Face_UnsolvedForDlp
    public static class StorageParameter extends DeviceCapacityInParameter implements Serializable {
        public static final int Type_delete_file = 1;
        public static final int Type_delete_dir = 2;
        public static final int Type_show_dir_files = 3;
        int type;
        String path;

        public StorageParameter(boolean isAskOn) {
            super(isAskOn);
        }

        public StorageParameter(int type, String info) {
            this.type = type;
            this.path = info;
        }
    }


}
