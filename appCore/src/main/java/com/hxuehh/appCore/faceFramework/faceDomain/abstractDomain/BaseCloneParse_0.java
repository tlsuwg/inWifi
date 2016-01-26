package com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ViewKeyable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Parseable;

import java.io.Serializable;

/**
 * Created by suwg on 2014/5/20.
 */

//解析基础元素String，承载方式Json
abstract class BaseCloneParse_0<GetOOForParse_Base, GetOOForParse_Container> implements
        Cloneable, Parseable<GetOOForParse_Base, GetOOForParse_Container>, ViewKeyable, Serializable {

    protected int viewKey;
    protected BaseCloneParse_0(int viewKey) {
        this.viewKey = viewKey;
    }

    @Override
    public int getViewKey() {
        return viewKey;
    }

    @Override
    public void setViewKey(int key) {
        viewKey = key;
    }

    @Override
    public BaseCloneParse_0 clone() throws CloneNotSupportedException {
        return (BaseCloneParse_0) super.clone();
    }
}
