package com.hxuehh.appCore.faceFramework.faceDomain.abstractDomainimp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxuehh.appCore.develop.FaceTestforDlp;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain.BaseAllImp;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Parseable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.image.image13.Image13lLoader;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.FaceHttpLoadForAbsViewGetProducer;
import com.hxuehh.reuse_Process_Imp.staicUtil.parse.FaceParesManganer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2015/8/31.
 */

//BasePreLoad_3<GetOOForParse_Base,GetOOForParse_Container,LoadedSettingParameters, CacheK,CacheV,InViewShow_Params,InViewHit_Params>

public class BaseAllImpJson extends BaseAllImp<String,JSONObject,List,JSONObject, LoadCursorSetting,String,Object,Object,Object>{


    public BaseAllImpJson(int viewKey) {
        super(viewKey);
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, List allData, LoadCursorSetting mLoadCursorSetting,Object[] oos) {

    }
    @Override
    public View getView(Activity mContext, int i, View view, ViewGroup viewGroup, LayoutInflater inflater, int viewItemKey, Object... params) throws Exception {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

//==============================================================/
    @Override
    public Parseable getItemValue(LoadCursorSetting mLoadSetting, JSONObject mJsonObject) throws Exception {
        return null;
    }

    public void parseAll(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, String json, long key) {
        FaceParesManganer.parseAsJSONArrayByObject(json, this.getViewKey(), mLoadSetting, mFaceCallBack, null, key);
    }

    public Intent hit(FaceBaseActivity_1 mContext, Object... obj) {
        return null;
    }//������ת

    public static  void  loadImageForTest(final String imageUrl){
        Image13lLoader.getInstance().loadImageForTest(imageUrl);
    }

    @FaceTestforDlp
    public  void loadAllImage(){};


//===================================================================================load
    protected void loadSettingByJson(LoadCursorSetting mLoadSetting, Object data, FaceLoadCallBack mFaceCallBack, JSONObject json) {
        if (json != null) {
            try {
                mLoadSetting.isHasNext = json.getBoolean("has_next");
            } catch (JSONException e) {
                e.printStackTrace();
                mLoadSetting.isHasNext = false;
            }
        } else {
            mLoadSetting.isHasNext = false;
        }
    }




    //���ü�����һҳ��ʵ�ַ�ʽ

    @Override
    public void loadSetting(LoadCursorSetting mLoadSetting, List data, FaceLoadCallBack mFaceCallBack, JSONObject json) {

        if (mLoadSetting.MustLoadMoreType == LoadCursorSetting.MustLoadMoreType_Json_get) {
            loadSettingByJson(mLoadSetting, data, mFaceCallBack, json);
            Su.logPullView("isLoadAccurateDataStatus:" + mLoadSetting.isLoadAccurateDataStatus);
        } else if (mLoadSetting.MustLoadMoreType == LoadCursorSetting.MustLoadMoreType_NumberComp) {
            if (data != null) {
                try {
                    List list = (List) data;
                    mLoadSetting.isHasNext = mLoadSetting.MustEverTimeNumber == list.size();
                } catch (Exception e) {
                    LogUtil.e(e);
                }
            } else {
                mLoadSetting.isHasNext = false;
            }
        } else if (mLoadSetting.MustLoadMoreType == LoadCursorSetting.MustLoadMoreType_PageComp) {
            loadSettingByPageJson(mLoadSetting, mFaceCallBack, json);
        } else if (mLoadSetting.MustLoadMoreType == LoadCursorSetting.MustLoadMoreType_NULL) {
            mLoadSetting.isHasNext = false;
        } else {
            mFaceCallBack.onCallBackErr(FaceLoadCallBack.ERR_Load_setting, new RuntimeException("û������loadSetting�ļ��ط�ʽ"));
            mLoadSetting.isHasNext = false;
        }
    }


    private void loadSettingByPageJson(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, JSONObject json) {
        if (json != null) {
            try {
                mLoadSetting.isHasNext = !json.getBoolean("isLastPage");
            } catch (JSONException e) {
                e.printStackTrace();
                mLoadSetting.isHasNext = false;
            }
        } else {
            mLoadSetting.isHasNext = false;
        }
    }

    @Override
    public Future loadInWhich_Thread_Source(final LoadCursorSetting mLoadSetting, final FaceLoadCallBack mFaceCallBack) {
        final long key = new Date().getTime();
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if (loadTrueByMCache(mLoadSetting, mFaceCallBack, key)) {//��һ���ߵĿ���̫��  ˲����ɵĶ�����ò������߳���
                } else {
                    loadTrueByHttp(mLoadSetting, mFaceCallBack, key);
                }
            }
        };
        Future mFuture = ThreadManager.getInstance().submitUIThread(mRunnable);
        mLoadSetting.putKey(mFuture, key);
        return mFuture;
    }

    @Override
    public boolean loadTrueByMCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key) {
        if (mLoadSetting.isMustCaCheLoad && !mLoadSetting.isReLoadFromSart) {//���ּ��� ��ȡֵ
            if (mLoadSetting.isHasReload()) {
                String json = removeFromCacheByKey(mLoadSetting);
                if (!StringUtil.isEmpty(json)) {
                    mFaceCallBack.onCallBackData(FaceLoadCallBack.FROM_CACHE, null, json, null, key);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean loadTrueByHttp(final LoadCursorSetting mLoadSetting, final FaceLoadCallBack mFaceCallBack, long key) {
        return FaceHttpLoadForAbsViewGetProducer.getInstance().produce(mLoadSetting, mFaceCallBack, false, key);
    }

    @Override
    public boolean loadTrueByDBCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key) {
        return false;
    }

    @Override
    public boolean loadTrueByFileCache(LoadCursorSetting mLoadSetting, FaceLoadCallBack mFaceCallBack, long key) {
        return false;
    }


//    =================================cache

    @Override
    public String inToCache(LoadCursorSetting key1, String value) {
        key1.preLoadString = value;
        Su.log("�������");
        return value;
    }

    @Override
    public String getValueByKey(LoadCursorSetting key1) {
        Su.log("�������");
        return key1.preLoadString;
    }
    @Override
    public String removeFromCacheByKey(LoadCursorSetting key1) {
        String key = key1.preLoadString;
        if (StringUtil.isEmpty(key)) {
            key1.preLoadString = null;
            Su.log("������� ��� rem");
            return key;
        } else {
            return key;
        }
    }

    @Override
    public int getSize() {
        return 0;
    }


//    ========================================Ԥ����
@Override
public void preLoad() {
//        Su.log("Ԥ����...")
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

