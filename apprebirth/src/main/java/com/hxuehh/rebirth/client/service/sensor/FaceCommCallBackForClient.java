package com.hxuehh.rebirth.client.service.sensor;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.app.ProApplication;
import com.hxuehh.rebirth.capacity.scene.Scene;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrderForDeviceCap_4;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;

/**
 * Created by suwg on 2015/9/24.
 */



//客户端处理传感器的
public class FaceCommCallBackForClient implements FaceCommCallBack {


    @Override
    public boolean callBack(Object[] t) {
        if (t != null) {
            final int type = (int) t[0];
            switch (type) {
                case DeviceCapacityBase.Type_Client_AccelerationSensor:
                    ThreadManager.getInstance().submitBackThread(new Runnable() {
                        @Override
                        public void run() {
                            sendMid(type);
                        }
                    });

                    break;
            }
        }
        return false;
    }

    private void sendMid(int type) {
        ClientService_TCPLongLink_ mClientService_TCPLongLink_ = getLinkShow();
        if (mClientService_TCPLongLink_ == null) return;
        try {
            BytesClassAidl mBytesClassAidl = SuApplication.getInstance().getAidlValue(AidlCacheKeys.MappingDeviceInfo_ShortCut);
            if (mBytesClassAidl == null) return;
            DeviceInfo mDeviceInfo = (DeviceInfo) mBytesClassAidl.getTrue();
            MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                    mDeviceInfo.getSU_UUID(),
                    DeviceCapacityBase.Type_Scene, new Scene.SceneParameter(Scene.SceneParameter.TypeCMDFrom, type));
            mClientService_TCPLongLink_.sendSyncAddCache(mid);
        } catch (FaceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ClientService_TCPLongLink_ getLinkShow() {
        ClientService_TCPLongLink_ mClientService_TCPLongLink_ = ProApplication.getInstance().getTCPLongLink_ClientService();
        if (mClientService_TCPLongLink_ == null) {
            return null;
        }
        if (mClientService_TCPLongLink_.getRunStatus()[2] != null) {
            return null;
        }
        return mClientService_TCPLongLink_;
    }

}
