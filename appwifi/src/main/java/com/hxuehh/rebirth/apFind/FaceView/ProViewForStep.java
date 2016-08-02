package com.hxuehh.rebirth.apFind.FaceView;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;

/**
 * Created by suwg on 2015/8/13.
 */
public interface ProViewForStep<T extends FaceCommCallBack> {
    int setStatus(int status);

    void setLoadingView(String info);

    void onErr();

    void setDoSucceed(T t);

    void onOk(String info);
}
