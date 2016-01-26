package com.hxuehh.rebirth.suMessage.pro.handler;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;

/**
 * Created by suwg on 2015/8/16.
 */
public abstract class MidMessageHandler_Imp implements MidMessageHandler {

    MidMessageHandler father;

    public MidMessageHandler_Imp(MidMessageHandler father) {
        this.father = father;
    }

    @Override
    public MidMessageBack_2 handlerMessage(MidMessageOrder_2 midMessage) throws FaceException {

        if(midMessage.isDiscard())throw  new FaceException("超时，丢弃的命令");

        if (father != null) {
            if (father.isCanConsume(midMessage)) {
                    return father.handlerMessage(midMessage);
            }
        }
//this 处理
        return null;
    }

    public abstract boolean isCanConsume(MidMessageOrder_2 midMessage);
}
