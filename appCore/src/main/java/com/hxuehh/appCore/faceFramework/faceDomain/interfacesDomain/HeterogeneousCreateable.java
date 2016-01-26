package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

import com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain.BaseAllImp;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;

import org.json.JSONObject;

/**
 * Created by suwg on 2015/4/28.
 */
//异类生成
//    杂交生产

public interface HeterogeneousCreateable {

    void  setPracticals(BaseAllImp[] Practicals);//设置需要的异类

    BaseAllImp getPractical(LoadCursorSetting mLoadCursorSetting, JSONObject input);//根绝key生成实际的使用类
}
