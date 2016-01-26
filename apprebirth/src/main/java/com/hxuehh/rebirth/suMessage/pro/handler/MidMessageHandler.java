package com.hxuehh.rebirth.suMessage.pro.handler;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;

/**
 * Created by suwg on 2015/8/16.
 */
public interface MidMessageHandler<T extends MidMessageOrder_2> {
    MidMessageBack_2 handlerMessage(T t) throws FaceException;
    boolean isCanConsume(T t);
}
