package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp;

import android.util.Log;

import com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain.BaseAllImp;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Cacheable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.PreLoadable;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.ViewCallBackDispenser_A;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.image.image13.Image13lLoader;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.LoadProcess;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ItemAnalysisable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Parseable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.FaceAutoLoadDataView;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.develop.LogUtil;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by suwg on 2014/5/23.
 */
//就是个中间件
// 消息分发

public class LoadForViewManager<Err extends Exception> implements FaceAutoLoadDataView {

    FaceContextWrapImp mConText;

    ViewCallBackDispenser_A mViewCallBack;
    LoadCursorSetting mLoadSetting;//此处是游标
    Future mFuture;//运行的线程
    int preLoadSize;//图片预加载数量

    public List<BaseAllImp> allData;

    public void setApterData(List allData) {
        this.allData = allData;
    }

    FaceLoadCallBack mFaceHandler = new FaceLoadCallBack() {
        @Override
        public boolean onCallBackErr(int key, Object params) {
            Su.logE("view err == " + key + (params == null ? " ； null" : params.toString()) + " ;alldata size == " + (allData == null ? "null" : allData.size()));

            if (mLoadSetting.isReLoadFromSart) {
                if ((mLoadSetting.MustHybridizationLoadType
                        ==LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain
                        ||mLoadSetting.MustHybridizationLoadType
                        ==LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains)
                        &&mLoadSetting.isLoadAccurateDataStatus) {
//                    还原回去
                    mLoadSetting.setNewAccurate();
                }
                mLoadSetting.isReLoadFromSart = false;
                loadErrForView(key, true, (Err) params);
                return true;
            }

            mLoadSetting.setErrTimes(mLoadSetting.getErrTimes() + 1);//加载完成
            mLoadSetting.setDataLoading(false);
//            mFuture = null;
//                if(mLoadSetting.hasMore()){
//                    loadNextPage(false);
//                }else{
            loadErrForView(key, false, (Err) params);
//                }
            return true;
        }


        @Override
        public boolean onCallBackData(int from,Object re, Object data, JSONObject params,long key) {
           Long kk= mLoadSetting.getFutrue(mFuture);

            if(key!=kk){
                return false;
            }

            Su.log("view data:" + FaceLoadCallBack.fromNames[from - 1] + "  " + (data == null ? "data null" : "data:" + (data instanceof List ? "list" : (DevRunningTime.isShowHttPData ? data : "!http"))) + "   " + (params == null ? "params null" : "params:" + params));

            switch (from) {
                case FaceLoadCallBack.FROM_CACHE: {
                    mLoadSetting.setErrTimes(0);//加载完成
                    mLoadSetting.setDataLoading(false);
                    mLoadSetting.setHasReload(false);//拿走了
                    parse(data,key);
                }
                break;
                case FaceLoadCallBack.FROM_DOMAIN: {//解析来的
                    Log.i("ybj","FROM_DOMAIN");
                    if (mLoadSetting.isMidLoadAccurateStatus_Wait_Data ||
                            (mLoadSetting.isMustOnTopSatausNoCore&& mConText.isOnTop())
                            ||!mLoadSetting.isMustOnTopSatausNoCore) {
                        loadedForView(data, params);
                        if (mLoadSetting.isMustCaCheLoad) {
                            if (mLoadSetting.isIshungry() && mLoadSetting.hasMore()) {
                                Su.log("饿死ing");
                                mLoadSetting.setIshungry(false);
                                loadNextPage(false);
                                return true;
                            }
                            if (!mLoadSetting.isHasReload() && mLoadSetting.hasMore()) {
                                loadNextPage(false);
                                return true;
                            }
                        } else {
//                            mFuture = null;//这个是不预加载的死
                        }
                    } else {
//                            donothing

                    }
                }
                break;
                case FaceLoadCallBack.FROM_HTTP: {
                    mLoadSetting.setErrTimes(0);//加载完成
                    mLoadSetting.setDataLoading(false);
//                    mFuture = null;
                    if (mLoadSetting.isReLoadFromSart) {//立马解析 从头来
                        parse(data, key);
                        Log.i("ybj","立马解析 从头来");
                        return true;
                    }
                    if (mLoadSetting.isMustCaCheLoad) {
                        if (mLoadSetting.isMidLoadAccurateStatus_Wait_Data
                                ||mLoadSetting.isIshungry()||
                                ((mLoadSetting.isMidLoadAccurateStatus_Wait_Data && mConText.isOnTop())
                                        ||!mLoadSetting.isMustOnTopSatausNoCore)) {
                            parse(data, key);
                            Log.i("ybj","立马解析 从头来2");
                        } else {
                            toCache((String) data);
                            Log.i("ybj","立马解析 从头来3");
                            mLoadSetting.setHasReload(true);
//                            mFuture = null;////预加载之后再死
                        }
                    } else {
                        parse(data, key);
                    }
                }
                break;
                default: {

                }
            }

            return true;
        }

    };


    public LoadForViewManager(ViewCallBackDispenser_A mViewCallBack, LoadCursorSetting mLoadSetting, FaceContextWrapImp mConText) {
        this.mViewCallBack = mViewCallBack;
        this.mLoadSetting = mLoadSetting;
        this.mConText = mConText;
        if (mLoadSetting.isMustPreLoadItem) {
         //   preLoadSize = ((PreLoadable) mLoadSetting.getObj()).preSize();
        }
    }


    public int getPageSize() {
        return mLoadSetting.hasPage;
    }

    public int getLoadingPage() {
        return mLoadSetting.getLoadingpage();
    }


    public void reSet() {
        if (mLoadSetting.isMustNeedMaxErr) {
            mLoadSetting.setErrTimes(0);
        }

        if (this.allData != null && allData.size() == 0) {
            mLoadSetting.isHasNext = true;
        }
    }


//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================

//    回调页面status

    //出错
    public void loadErrForView(final int errkey, final boolean isReload, final Err... err) {

        mConText.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mViewCallBack.loadErr(errkey, mLoadSetting.getErrTimes(), isReload, new Object[]{err});
            }
        });

    }


    //完成
    public void loadedForView(final Object data, JSONObject la) {

        final int loadingpage = mLoadSetting.getLoadingpage();
        final boolean isReLoadFromSart = mLoadSetting.isReLoadFromSart;
        mLoadSetting.loadOK(data, la, mFaceHandler);//其实这个没有必要再UI线程做 但是

        mConText.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mViewCallBack.loaded(new Date().getTime(),
                        loadingpage,
                        isReLoadFromSart,
                        mLoadSetting.hasMore(),
                        data);//是不是需要删除原来数据

            }
        });

        if (isReLoadFromSart && data != null) {
            clearCache();
        }
    }


    //    正在加载
    public void loading() {
        mViewCallBack.loading();
    }


//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//    推进


    //界面 杀死进程  不再加载
    public boolean OnKeyBack() {
        if (mLoadSetting.isDataLoading()) {
            mFaceHandler.onCallBackErr(FaceLoadCallBack.ERR_key_back, null);
            if (mFuture != null && !mFuture.isDone()) mFuture.cancel(true);//直接中断
            mLoadSetting.setDataLoading(false);
            return true;
        }
        return false;
    }


    //    加载更多  在提交数据的时候已经判断还有没有
    public boolean loadNextPage(boolean from_UI) {

        Su.log("加载更多>>" + (from_UI ? "来自 UI" : "来自 预加载"));
        if (!mLoadSetting.isMustNeedLoadData || mLoadSetting.isDataLoading() || mViewCallBack.isViewLoading()) {
            return false;
        }
        Su.log("加载更多 true  pageing:" + mLoadSetting.getLoadingpage());

        if (from_UI) {
            mViewCallBack.loading();
            if (mLoadSetting.isMustCaCheLoad && !mLoadSetting.isHasReload()) {//需要预加载
                mLoadSetting.setIshungry(true);//设置饿死状态
            }
        }

        if (!mLoadSetting.hasMore()) {
            mFaceHandler.onCallBackErr(FaceLoadCallBack.ERR_loaded_all, null);

            LogUtil.d("不再有");
            return false;//这个就不需要加载了
        } else {
            mLoadSetting.setDataLoading(true);//这个是界面的展示 一定要展示
            LoadProcess oo = mLoadSetting.getObj();
            mFuture = oo.loadInWhich_Thread_Source(mLoadSetting, mFaceHandler);
            return true;
        }
    }


    //    从头加载  //刷新数据
    public boolean reLoadFromStart() {
        Su.log("加载更多 reLoadFromStart");

     /*  if (!mLoadSetting.isMustNeedLoadData || mLoadSetting.isReLoadFromSart) return false;

       if (mLoadSetting.isDataLoading()) {
            if (mFuture != null && !mFuture.isDone()) mFuture.cancel(true);//直接中断
//            return false; 这个是做安全的操作 直接返回 不做操作
        }*/
        mLoadSetting.isReLoadFromSart = true;
        mViewCallBack.loading();
        if (mFuture != null ) mFuture.cancel(true);//直接中断
        LoadProcess oo = mLoadSetting.getObj();
        mFuture=oo.loadInWhich_Thread_Source(mLoadSetting, mFaceHandler);

        return true;
    }

    //    重新加载刚才的一页  （不怎么可能出现）杀死杀死
    public boolean reload() {
        if (!mLoadSetting.isMustNeedLoadData || mLoadSetting.isDataLoading()) return false;
        mLoadSetting.isReLoadPage = true;
        mViewCallBack.loading();
        LoadProcess oo = mLoadSetting.getObj();
        oo.loadInWhich_Thread_Source(mLoadSetting, mFaceHandler);
        return true;
    }

    @Override
    public void stopLoadData() {
        if(!mLoadSetting.isMustOnTopSatausNoCore)return;
        if (mFuture != null && !mFuture.isDone()) mFuture.cancel(true);//直接中断
        mLoadSetting.setDataLoading(false);
        mViewCallBack.setViewLoading(false);
    }


    //    图片机制
    @Override
    public void stopLoadImage() {
        Image13lLoader.getInstance().stop();
    }

    @Override
    public void pauseLoadImage() {
        Image13lLoader.getInstance().pause();
    }

    @Override
    public void startLoadImage() {
        Image13lLoader.getInstance().resume();
    }

    @Override
    public void clearLoadImage() {
        Image13lLoader.getInstance().clearMemoryCache();
    }


    public boolean onScrollShow(int firstVisibleItem, int lastVisibleItem, boolean toBottom, int mVisibleItemCount) {

        if (this.isMustPreLoadItem()) {//预加载 元素数据
            if (toBottom) {
                preLoad(lastVisibleItem, lastVisibleItem + preLoadSize);
            } else {
                preLoad(firstVisibleItem - preLoadSize, firstVisibleItem);
            }
        }

        if (this.isMustAnalysisItem()) { //打点
            analysis(firstVisibleItem, lastVisibleItem, mVisibleItemCount,toBottom);
        }

        return true;
    }


//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//=========================================================
//    机制处理  加载 入池  加载池数据  预加载数据....

    private void toCache(String data) {
        Cacheable mCacheable = mLoadSetting.getObj();
        mCacheable.inToCache(mLoadSetting, data);
    }

    private void clearCache() {
        Cacheable mCacheable = mLoadSetting.getObj();
        mCacheable.removeFromCacheByKey(mLoadSetting);
    }


    private void parse(Object data, long key) {
        Su.log("parse解析");
        Parseable mFaceParseable = mLoadSetting.getObj();
        mFaceParseable.parseAll(mLoadSetting, mFaceHandler, (String) data, key);
    }


    private void preLoad(int from, int to) {
        if (allData == null) return;
//        Su.log("pre load"+from+" "+to);
        if (from < 0) return;
        for (int i = from; i <= to; i++) {
            if (allData.size() > i) {
                PreLoadable mPreLoadable = allData.get(i);
                if (mPreLoadable != null) {
                    if (!mPreLoadable.isPreLoaded())
                        mPreLoadable.preLoad();
                }
            }
        }
    }


    Set<ItemAnalysisable> sameIDCon=new HashSet<ItemAnalysisable>();//去重使用
    Set<ItemAnalysisable> nowList=new HashSet<ItemAnalysisable>();

    private void analysis(int from, int to, int mVisibleItemCount, boolean toBottom) {
        if (allData == null || from < 0) return;

        Su.logPullView("analysis"+from+" "+to+" "+mVisibleItemCount);
        nowList.clear();
        for (int i = from; i <= to; i++) {
            if (allData.size() > i) {
                ItemAnalysisable mAnalysisable = (ItemAnalysisable)allData.get(i);
                if (mAnalysisable != null) {
                    nowList.add(mAnalysisable);
                    if (sameIDCon!=null&&sameIDCon.contains(mAnalysisable)) {
//                            Su.logPullView("已经存在" + i + "mVisibleItemCount==" + mVisibleItemCount);
                        continue;
                    }
                    mAnalysisable.analysis_A(i, mLoadSetting.getmAnalysisParmObj());
                }
            }
        }

        sameIDCon.clear();
        sameIDCon.addAll(nowList);

    }

    public boolean isMustPreLoadItem() {
        return mLoadSetting.isMustPreLoadItem;
    }

    public boolean isLoading() {
        return mLoadSetting.isDataLoading();
    }

    public boolean isReloadFromStart() {
        return mLoadSetting.isReLoadFromSart;
    }

    public boolean isMustAnalysisItem() {
        return mLoadSetting.isMustAnalysisItem;
    }

    public LoadCursorSetting getmLoadSetting(){
        return  mLoadSetting;
    }

}
