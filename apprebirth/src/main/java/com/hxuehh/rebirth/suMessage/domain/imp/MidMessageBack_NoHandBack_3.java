package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;

/**
 * Created by suwg on 2015/8/18.
 */
public class MidMessageBack_NoHandBack_3 extends MidMessageBack_2 {
    public MidMessageBack_NoHandBack_3( MidMessageOrder_2 midMessage) {
        super(midMessage);
        this.putKeyValue(MidMessage.Key_NoHand_Back, "1");
    }
}
