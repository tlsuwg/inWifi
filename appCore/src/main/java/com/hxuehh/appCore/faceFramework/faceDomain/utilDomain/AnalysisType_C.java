package com.hxuehh.appCore.faceFramework.faceDomain.utilDomain;

import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2014/12/2.
 */

public class AnalysisType_C {
    public String typeName;
    @Deprecated
    public AnalysisType_C(String typeName) {
        this.typeName = typeName;
    }

    public AnalysisType_C(int aActivityViewKey) {
        switch (aActivityViewKey) {
            case ViewKeys.MuYingMainActivity://母婴列表
                this.typeName = "muyin";
                break;
            case ViewKeys.OneBrandGroupDetailActivity:
            case ViewKeys.OneMuYingBrandGroupGoodsActivity://单品牌列表
                this.typeName = "bdlst";
                break;
            default:

//                throw new DevException("20150228没有设置新的打点key");
        }

    }

    @Override
    public String toString() {
        return "AnalysisType{" +
                ", typeName='" + typeName + '\'' +
                '}';
    }


}
