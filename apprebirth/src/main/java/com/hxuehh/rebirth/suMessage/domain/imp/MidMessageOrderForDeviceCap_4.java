package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.SerializeUtil;

/**
 * Created by suwg on 2015/9/11.
 */
public class MidMessageOrderForDeviceCap_4 extends MidMessageOrderForDevice_3 {
    public MidMessageOrderForDeviceCap_4(int midMessageCMD_device_info, String su_uuid, int Key_DeviceCapaci_ID, DeviceCapacityInParameter mDeviceCapacityParameter) {
        super(midMessageCMD_device_info,su_uuid);
        this.putKeyValue(MidMessage.Key_DeviceCapaci_ID, Key_DeviceCapaci_ID+"");
        this.setBytes(SerializeUtil.serialize(mDeviceCapacityParameter));
    }
}
