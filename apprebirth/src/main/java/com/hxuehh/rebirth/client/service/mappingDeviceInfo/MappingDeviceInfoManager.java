package com.hxuehh.rebirth.client.service.mappingDeviceInfo;

import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suwg on 2015/11/9.
 */
public class MappingDeviceInfoManager {

    private static MappingDeviceInfoManager instance = null;
    private MappingDeviceInfoManager() {
    }
    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new MappingDeviceInfoManager();
        }
    }
    public static MappingDeviceInfoManager getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }


    private Map<String, DeviceInfo> mDeviceInfoMap = new HashMap();

    public void setNowDeviceInfo(List<DeviceInfo> list) {
        mDeviceInfoMap.clear();
        for (DeviceInfo m : list) {
            mDeviceInfoMap.put(m.getSU_UUID(), m);
        }
    }

    public DeviceInfo getDeviceInfoByUUID(String DeviceInfoUUID) {
        return mDeviceInfoMap.get(DeviceInfoUUID);
    }

    public void setOneDeviceInfo(DeviceInfo mDeviceInfo) {
        mDeviceInfoMap.put(mDeviceInfo.getSU_UUID(),mDeviceInfo);
    }

    public void clearAll() {
      for(DeviceInfo mDeviceInfo:  mDeviceInfoMap.values()){
          mDeviceInfo.clearAllCaps();
      }
    }
}
