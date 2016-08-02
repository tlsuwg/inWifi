package com.hxuehh.rebirth.push.MiPush.handerMessage;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessage_OrderBack_3;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageP2POrder_3;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler;
import com.hxuehh.rebirth.suMessage.pro.handler.MidMessageHandler_Imp;

/**
 * Created by suwg on 2015/8/16.
 */


public class MiPushP2PMessageHandler extends MidMessageHandler_Imp {

    public MiPushP2PMessageHandler(MidMessageHandler father) {
        super(father);
    }


    @Override
    public MidMessage_OrderBack_3 handlerMessage(MidMessageOrder_2 m) throws FaceException {
        MidMessageP2POrder_3 mMidMessageP2POrder_3 = (MidMessageP2POrder_3) m;
        MidMessage res333 = null;
        switch (mMidMessageP2POrder_3.getCmd()) {


        }

        return new MidMessage_OrderBack_3(mMidMessageP2POrder_3, res333);
    }


    @Override
    public boolean isCanConsume(MidMessageOrder_2 midMessage) {
        int kk = midMessage.getCmd();
        return MidMessageCMDKeys.isP2PCom(kk);
    }
}
