package com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.PreLoadable;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: suwg
 * To change this template use File | Settings | File Templates.
 */
 abstract class BasePreLoad_3<
        GetOOForParse_Base,GetOOForParse_Container,
        GetOOForDomain_Container,LoadedSettingParameters, CacheK,CacheV,
        InViewShow_Params,ClickInMainView,InViewHit_Params>
        extends BaseInViewGroup_2<GetOOForParse_Base,GetOOForParse_Container,GetOOForDomain_Container,LoadedSettingParameters, CacheK,CacheV,InViewShow_Params,ClickInMainView,InViewHit_Params>
        implements
        PreLoadable,
        Serializable {
    protected BasePreLoad_3(int viewKey) {
        super(viewKey);
    }

    @Override
    public void preLoad() {
//        Su.log("预加载...")
//        ImageView view=null;
//        Image13lLoader.getInstance().loadImage(getImageUrl(),view);
        isPreLoad=true;
    }

    transient boolean isPreLoad;

    @Override
    public boolean isPreLoaded() {
        return isPreLoad;
    }

    @Override
    public int preSize() {
        return  3;
    }



}
