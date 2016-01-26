package com.hxuehh.rebirth.device.domain;

import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;

import java.io.Serializable;

/**
 * Created by suwg on 2015/9/11.
 */

//只是正常的结果输出

public abstract class DeviceCapacityOutResult_3 extends MidMessageBack_2 implements Serializable {
    @Deprecated
    public DeviceCapacityOutResult_3(MidMessageOrder_2 requestID_From) {
        super(requestID_From);
    }


    public DeviceCapacityOutResult_3() {
    }
}
