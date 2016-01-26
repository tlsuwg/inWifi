package com.hxuehh.rebirth.client.service.sensor.interfaces;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.TypeIDable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ViewKeyable;

/**
 * Created by suwg on 2015/9/24.
 */
public abstract class ClientSensor implements TypeIDable {



    int type;
    @Override
    public void setType(int type) {
        this.type = type;
    }

    public abstract void startLinstener();

    public abstract void endLinstener();

}
