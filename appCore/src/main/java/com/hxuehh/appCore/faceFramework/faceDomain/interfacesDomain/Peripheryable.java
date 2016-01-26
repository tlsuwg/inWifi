package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

/**
 * Created by suwg on 2014/8/13.
 *
 * 外围事件
 * push 小部件 浮层小火箭
 */
public interface Peripheryable {

    public static final String KeyName="periphery_from";
    public static final int KeyPush=1;
    public static final int KeyWidget=2;
    public static final int KeyF=3;

    void setFrom(int fromType);
    int getFrom();


}
