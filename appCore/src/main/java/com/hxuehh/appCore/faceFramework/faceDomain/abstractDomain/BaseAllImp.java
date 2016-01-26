package com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain;

/**
 * Created by suwg on 2015/8/31.
 */
public abstract class BaseAllImp<
        GetOOForParse_Base,GetOOForParse_Container,
        GetOOForDomain_Container,LoadedSettingParameters, CacheK,CacheV,
        InViewShow_Params,ClickInMainView,InViewHit_Params>
        extends BasePreLoad_3<GetOOForParse_Base,GetOOForParse_Container,GetOOForDomain_Container,LoadedSettingParameters, CacheK,CacheV,InViewShow_Params,ClickInMainView,InViewHit_Params> {

    public BaseAllImp(int viewKey) {
        super(viewKey);
    }

}
