package com.hxuehh.rebirth.suMessage.domain.imp;

import com.hxuehh.rebirth.suMessage.domain.MidMessage;

/**
 * Created by suwg on 2015/8/18.
 */
public class MidMessage_OrderBack_3 extends MidMessageBack_2 {
    MidMessage to;
    public MidMessage_OrderBack_3(MidMessageP2POrder_3 mMidMessageP2POrder_3, MidMessage to) {
        super(mMidMessageP2POrder_3);
        this.to=to;
        if(to!=null){
            to.putKeyValue(MidMessage.Key_To_Which_DeviceID, mMidMessageP2POrder_3.getFrom());
            to.putKeyValue(MidMessage.Key_CMDkeyResquetID, mMidMessageP2POrder_3.getmResquetID());
        }
    }

    public MidMessage getTo() {
        return to;
    }


}
