package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;

/**
 * Created by suwg on 2015/9/11.
 */
public class MidMessageOrderForDevice_3 extends MidMessageOrder_2 {
    public MidMessageOrderForDevice_3(int midMessageCMD_device_info, String su_uuid) {
        super(midMessageCMD_device_info);
        this.putKeyValue(MidMessage.Key_To_Which_DeviceID,su_uuid);
    }
}
