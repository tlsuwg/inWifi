package com.hxuehh.appCore.faceFramework.faceDomain.abstractDomainimp;

import com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain.BaseAllImp;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.HeterogeneousCreateable;

import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by suwg on 2015/4/28.
 */
public abstract class BaseHeterogeneousCreateableImp_3 extends BaseAllImp implements HeterogeneousCreateable {

    public BaseHeterogeneousCreateableImp_3(int viewKey) {
        super(viewKey);
    }



    protected  BaseAllImp[] Practicals;
    protected  Map<BaseAllImp, Integer> PracticalsMap = new Hashtable<BaseAllImp, Integer>();
    @Override
    public void setPracticals(BaseAllImp[] Practicals) {
        this. Practicals=Practicals;
        int i=0;
        if(Practicals!=null) {
            for (BaseAllImp mBaseCloneParse_0 : Practicals){
                PracticalsMap.put(mBaseCloneParse_0,i++);
            }
        }
    }

    public abstract BaseAllImp getPractical(LoadCursorSetting mLoadCursorSetting,JSONObject  input);

    public abstract int getViewTypeCount();

    public  int getItemViewType(BaseAllImp position) {
        return PracticalsMap.get(position);
    }
}
