package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;

/**
 * Created by suwg on 2015/8/18.
 */
public class MidMessageBack_P2PErr_4 extends MidMessageBack_2 {
    public MidMessageBack_P2PErr_4(String message, MidMessageP2POrder_3 midMessage) {
        super(midMessage);
        this.putKeyValue(MidMessage.Key_ErrInfo, message);
        this.putKeyValue(MidMessage.Key_To_Which_DeviceID,midMessage.getFrom());
    }


}
