package com.hxuehh.rebirth.device.domain.imp;

import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;

/**
 * Created by suwg on 2015/9/13.
 */
public class CommonDeviceCapacityOutResult extends DeviceCapacityOutResult_3 {
   transient boolean isOK;
    public CommonDeviceCapacityOutResult(boolean isOK) {
        this.isOK = isOK;
        if(isOK){
            setOK();
        }else{
            setUNOK();
        }
    }
}
