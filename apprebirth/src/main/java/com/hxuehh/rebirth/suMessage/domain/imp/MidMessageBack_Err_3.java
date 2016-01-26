package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;

/**
 * Created by suwg on 2015/8/18.
 */
public class MidMessageBack_Err_3 extends MidMessageBack_2 {
    public MidMessageBack_Err_3(String message, MidMessageOrder_2 midMessage) {
        super(midMessage);
        this.putKeyValue(MidMessage.Key_ErrInfo, message);
    }

    public MidMessageBack_Err_3() {
    }
}
