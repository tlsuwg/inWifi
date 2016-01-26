package com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.AdapterViewOnItemClickListener;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Hitable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.InViewGroupWithAdaper;

import java.io.Serializable;

/**
 * Created by suwg on 2014/5/24.
 * abs里面  甄别 展示  点击  To
 */
//解析基础元素String，承载方式Json，承载对象list,加载参数的承载json，缓缓key,缓存value,视图传参数，点击传参数
 abstract class BaseInViewGroup_2<
        GetOOForParse_Base,GetOOForParse_Container,
        GetOOForDomain_Container,LoadedSettingParameters, CacheK,CacheV,
        InViewShow_Params,ClickInMainView,InViewHit_Params>
        extends
        BaseLoad_Cache_1<GetOOForParse_Base,GetOOForParse_Container,GetOOForDomain_Container,LoadedSettingParameters, CacheK,CacheV> //自动复制
        implements
        InViewGroupWithAdaper<InViewShow_Params>,//在abs里面view
        AdapterViewOnItemClickListener<GetOOForDomain_Container,ClickInMainView>,//ads里面点击
        Hitable<InViewHit_Params>,//命中
        Serializable {
    protected BaseInViewGroup_2(int viewKey) {
        super(viewKey);
    }
}
