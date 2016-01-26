package com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Cacheable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.LoadProcess;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: suwg
 * To change this template use File | Settings | File Templates.
 */

//解析基础元素String，承载方式Json，承载对象list,加载参数的承载json，缓缓key,缓存value
abstract class BaseLoad_Cache_1<GetOOForParse_Base,GetOOForParse_Container,GetOOForDomain_Container,LoadedSettingParameters,CacheK,CacheV> extends
        BaseCloneParse_0<GetOOForParse_Base, GetOOForParse_Container>
        implements
        LoadProcess<LoadedSettingParameters, GetOOForDomain_Container>,//自动加载自己
        Cacheable<CacheK, CacheV>,//cache操作
        Serializable {
    protected BaseLoad_Cache_1(int viewKey) {
        super(viewKey);
    }
    //    这个也是可以继承的 但是必须是
}
