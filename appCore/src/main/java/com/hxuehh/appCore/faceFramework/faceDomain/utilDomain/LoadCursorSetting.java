package com.hxuehh.appCore.faceFramework.faceDomain.utilDomain;


import com.hxuehh.appCore.faceFramework.faceDomain.abstractDomain.BaseAllImp;
import com.hxuehh.reuse_Process_Imp.appSetting.AppConfig;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IDSameable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.InViewGroupWithAdaper;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.ItemAnalysisable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.LoadSelfByUrlable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.NotFaceIDSameable;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.LoadForViewManager;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.reuse_Process_Imp.staicUtil.netUtil.netHttpDomain.FaceHttpParamBuilder;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.concurrent.Future;

/**
 * Created by suwg on 2014/5/20.
 * <p/>
 * Must是初始化项目
 * <p/>
 * 初始化
 */

public class LoadCursorSetting<LoadParameters, Mode extends BaseAllImp, LoadedParameters, LoadedObjects, AnalysisObjParm> implements LoadSelfByUrlable {
    public boolean isHaveCategoty=false;//品牌团是否有分类


    public static final int MAX_ERR_TIMES = 5;//最大尝试次数

    public static final int MustLoadParmType_Page = 1;//加载参数类型
    public static final int MustLoadParmType_Limit = 2;//加载参数类型
    public static final int MustLoadParmType_Im_Page = 3;//加载参数类型
    public static final int MustLoadParmType_Pram_Null = 4;//加载参数类型
    public static final int MustLoadParmType_HybridizationPage = 5;//加载参数类型



    public int MustLoadParmType = -1;//加载参数类型


    public static final int MustLoadMoreType_Json_get = 1;//json获取
    public static final int MustLoadMoreType_NumberComp = 2;//对比数量方式
    public static final int MustLoadMoreType_PageComp = 3;//接口返回page
    public static final int MustLoadMoreType_NULL = 4;//不返回下一页
    public int MustLoadMoreType = -1;//加载完毕解析是不是还存在继续的数据类型


    @Deprecated
    public static final int MustLoadedShowAll = 1;//加载之后保存所有数据

    public static final int MustLoadedShowCurrent = 2;//加载之后清除历史，只是展示现在的
    public static final int MustReLoadedShowCurrent = 3;//重新加载之后清除历史，只是展示现在的
    public static final int MustLoadedFirstTimeRemoveOldAll = 4;//第一次加载完成，删除之前的数据
    public int MustLoadedShowType = MustReLoadedShowCurrent;//加载之后保存所有数据


    //    策略模式
    public static final int MustHybridizationLoadType_ONLY_ONE_DATA_INTERFACE = 1;//一个数据接口直接吐出2种类型

    public static final int MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain = 2;
    //一个数据接口,需改参数，从0开始;解析出来一类对象
//    母婴
    public static final int MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains = 3;
    //一个数据接口,需改参数，从0开始;解析出来duo类对象

    public static final int MustHybridizationLoadType_SOME_DATA_INTERFACES = 4;//2个数据接口拉取

    public int MustHybridizationLoadType;//杂交的数据类型  不同于推荐类型  需要杂交解析 生成不同对象


    public int MustEverTimeNumber = -1;//每次加载的数量


    public boolean isMustPreLoadItem;//是不是自动预加载自己的元素

    public boolean isMustCaCheLoad;//是不是预加载
    public boolean isMustRemoveTheSame;//是不是需要去重  但是如果ID是null也是会被去掉的
    public boolean isMustNeedMaxErr;//是不是检测错误加载次数


    public boolean isMustNeedLoadData = true;//是不是需要加载load数据

    public boolean isMustLoadOnlyOneTimes;//只是加载一次  不能继续加载  搜索
    public boolean isMustGroup;//是不是需要进行排序  只要是设置排序 那么就不需要删除相同的 自己会删除的
    public boolean isMustLoadNextFromTop;//是不是从头加载如果是  list
    public boolean isReLoadFromSartCallBack;//一般情况下 刷新是不需要回调的


    private boolean isDataLoading;//是不是在加载中
    private boolean hasReload;//是不是预加载完成
    public String preLoadString;//预加载的json


    public boolean isReLoadFromSart;//从1 开始
    public boolean isReLoadPage;//从 hasPage 0 刚刚加载的一页开始  暂时没有作用
    public int hasPage = 0;
    private int loadingpage = 1;//预加载
    public boolean isHasNext = true;

    public int maxPage;//最大页面数量
    private int errTimes;//现在的错误次数
    public boolean isMustAnalysisItem;//是不是打点
    public boolean isNoDataChangeToRecommend;//如果没有数据，是不是服务器端推荐数据

    public boolean isMustOnTopSatausNoCore = true;//不关心是不是在前台 直接去做


    public boolean isNoLinkedStatus;


    public LoadCursorSetting(Mode obj) {
        this.obj = obj;
    }

    public LoadCursorSetting(Mode obj, boolean isNeedLoad) {
        isMustNeedLoadData = isNeedLoad;
        this.obj = obj;
    }

    public LoadCursorSetting(int MustLoadParmType, int MustLoadMoreType, int MustEverTimeLoadNumber,
                             boolean isMustCaCheLoad, boolean isMustPreLoadItem, boolean isMustRemoveTheSame,
                             String getSelfByUrl, Mode obj, LoadParameters loadParameters) throws NotFaceIDSameable {

        this.MustLoadMoreType = MustLoadMoreType;
        this.MustLoadParmType = MustLoadParmType;
        this.MustEverTimeNumber = MustEverTimeLoadNumber;
        this.isMustCaCheLoad = isMustCaCheLoad;

        if (!AppConfig.getInstance().LOG_CLOSED && DevRunningTime.isSuTestM_C) {
            this.MustEverTimeNumber = DevRunningTime.isMustEverTimeLoadNumber;
            this.isMustCaCheLoad = false;//这个导致线程出错，缓存的时候还启动线程，所以关闭 suwg
        }

        if (isMustCaCheLoad) ishungry = true;
        this.isMustPreLoadItem = isMustPreLoadItem;
        this.isMustRemoveTheSame = isMustRemoveTheSame;
        this.getSelfByUrl = getSelfByUrl;
        this.obj = obj;
        this.loadParameters = loadParameters;

        if (this.isMustRemoveTheSame) {
            if (!(obj instanceof IDSameable)) {
                throw new NotFaceIDSameable("Mode 必须实现去重接口");
            }
        }

    }


    public LoadCursorSetting(int MustLoadParmType, int MustLoadMoreType, int MustEverTimeLoadNumber,
                             boolean isMustCaCheLoad, boolean isMustPreLoadItem,
                             boolean isMustRemoveTheSame, boolean isMustAnalysis,
                             String getSelfByUrl, Mode obj, LoadParameters loadParameters) throws NotFaceIDSameable {

        this(MustLoadParmType, MustLoadMoreType, MustEverTimeLoadNumber,
                isMustCaCheLoad, isMustPreLoadItem, isMustRemoveTheSame,
                getSelfByUrl, obj, loadParameters);

        this.isMustAnalysisItem = isMustAnalysis;
        if (this.isMustAnalysisItem) {
            if (!(obj instanceof ItemAnalysisable)) {
                throw new NotFaceIDSameable("Mode 必须实现分析接口 ");
            }
        }
    }


    public synchronized boolean isDataLoading() {
        return isDataLoading;
    }

    public synchronized void setDataLoading(boolean isDataLoading) {
        this.isDataLoading = isDataLoading;
    }

    public synchronized boolean isHasReload() {
        return hasReload;
    }

    public synchronized void setHasReload(boolean hasReload) {
        this.hasReload = hasReload;
    }

    private boolean ishungry;//需要预加载 ，但是已经到了最下面 来了数据马上展示

    public synchronized boolean isIshungry() {
        return ishungry;
    }

    public synchronized void setIshungry(boolean ishungry) {
        this.ishungry = ishungry;
    }

    public synchronized int getLoadingpage() {
        return loadingpage;
    }

    public synchronized int getErrTimes() {
        return errTimes;
    }

    public synchronized void setErrTimes(int errTimes) {
        this.errTimes = errTimes;
    }

    public boolean isErrTimesOut() {
        return errTimes > MAX_ERR_TIMES;
    }

    public boolean hasMore() {//是不是还要继续加载
        if (isMustNeedMaxErr && isErrTimesOut()) return false;
        return isHasNext;
    }

    public void setMustGroup(boolean isMustGroup) {
        this.isMustGroup = isMustGroup;
        if (isMustGroup) {
            isMustRemoveTheSame = false;
        }
    }

    private void saxForLoadedParm(LoadedObjects data, LoadedParameters la, LoadCursorSetting tLoadPaLoadSetting, FaceLoadCallBack mFaceCallBack) {//这个过程可以抽象出来
//        if(tLoadPaLoadSetting.isLoadLoc)return;
        obj.loadSetting(tLoadPaLoadSetting, data, mFaceCallBack, la == null ? null : (JSONObject) la);
    }


    public void loadOK(LoadedObjects data, LoadedParameters la, FaceLoadCallBack mFaceCallBack) {

        saxForLoadedParm(data, la, this, mFaceCallBack);

        synchronized (this) {
            if (isReLoadFromSart) {//不要从头来了
                hasPage = 1;
                loadingpage = 2;
                isReLoadFromSart = false;//不要刷新了
                if (isMustCaCheLoad) {
                    hasReload = false;
                }
                setBaseAccurate();

                return;
            }
        }

        synchronized (this) {
            hasPage = loadingpage;
            loadingpage++;
        }

    }

    private Mode obj;//这个是原形 clone之后使用

    public Mode getObj() {
        return obj;
    }

    public void setObj(Mode obj) {
        this.obj = obj;
    }

    private LoadParameters loadParameters;

    public LoadParameters getLoadParameters() {
        return loadParameters;
    }

    public void setLoadParameters(LoadParameters loadParameters) {
        this.loadParameters = loadParameters;
    }

    protected String getSelfByUrl;

    @Override
    public String getGetSelfByUrl() {
        return getSelfByUrl;
    }

    @Override
    public void setGetSelfByUrl(String getSelfByUrl) {
        this.getSelfByUrl = getSelfByUrl;
    }

    AnalysisObjParm[] mAnalysisObjs;

    public AnalysisObjParm[] getmAnalysisParmObj() {
        return mAnalysisObjs;
    }

    public void setmAnalysisParmObj(AnalysisObjParm... mAnalysisParmObjs) {
        this.mAnalysisObjs = mAnalysisParmObjs;
    }

    LoadForViewManager mLoadForViewManager;

    public void setLoadForViewManger(LoadForViewManager mLoadForViewManger) {
        this.mLoadForViewManager = mLoadForViewManger;
    }


    //推荐 start====================================================
//    MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain  模式，只是修改参数

    String accurate[], trueStr[], falseStr[];//此处公用，可以是推荐的参数变量；也可以是不同的URL
    InViewGroupWithAdaper mAccurateObj;//展示下面的  ，有的只是展示作用，比如母婴；有的是存在点击的事件
    InViewGroupWithAdaper mAccurateNullObj;//补足上排空间
    public boolean isMidLoadAccurateStatus_Wait_Data;//是不是正在加载推荐的数据  中间状态
    public boolean isLoadAccurateDataStatus;//已经是推荐的数据状态
    public boolean isHasToAccurate;//网络下发的标签是不是存在加载的数据

    //推荐
    public void setAccurate(String[] accurate, String[] trueStr, String falseStr[],
                            InViewGroupWithAdaper mAccurateObj, InViewGroupWithAdaper mAccurateNullObj) {

        this.accurate = accurate;
        this.trueStr = trueStr;
        this.falseStr = falseStr;
        this.mAccurateObj = mAccurateObj;
        this.mAccurateNullObj = mAccurateNullObj;
        this.isMidLoadAccurateStatus_Wait_Data = false;
        if (this.MustHybridizationLoadType
                == LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain
                || this.MustHybridizationLoadType
                == LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains) {
            isMustOnTopSatausNoCore = false;
        }
        setBaseAccurate();
    }

    public InViewGroupWithAdaper getAccurateObj() {
        return mAccurateObj;
    }

    public InViewGroupWithAdaper getAccurateNullObj() {
        return mAccurateNullObj;
    }

    public void setBaseAccurate() {
        if (MustHybridizationLoadType == MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain) {//修改参数
            if (loadParameters != null)
                ((FaceHttpParamBuilder) loadParameters).putStrings(accurate, trueStr);
        } else if (MustHybridizationLoadType == MustHybridizationLoadType_SOME_DATA_INTERFACES) {//2个接口{
            this.setGetSelfByUrl(trueStr[0]);
        } else if (MustHybridizationLoadType == MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains) {
            if (loadParameters != null)
                ((FaceHttpParamBuilder) loadParameters).putStrings(accurate, trueStr);

        }

    }

    public void setNewAccurate() {
        if (MustHybridizationLoadType == MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain) {//修改参数
            if (loadParameters != null)
                ((FaceHttpParamBuilder) loadParameters).putStrings(accurate, falseStr);
        } else if (MustHybridizationLoadType == MustHybridizationLoadType_SOME_DATA_INTERFACES) {//2个接口{
            this.setGetSelfByUrl(falseStr[0]);
        } else if (MustHybridizationLoadType == MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains) {
            if (loadParameters != null)
                ((FaceHttpParamBuilder) loadParameters).putStrings(accurate, falseStr);
        }
    }


    //存在非精准类型  默认是没有的

    public void changeAccurateParAndLoad() {
        if (MustHybridizationLoadType == MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain
            ||   MustHybridizationLoadType == MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains
               ) {//修改参数
            isMidLoadAccurateStatus_Wait_Data = true;//中间态  步骤2
            if (!isLoadAccurateDataStatus) {
                isLoadAccurateDataStatus = true;//已经是推荐态了
                setNewAccurate();
                hasPage = 0;
                loadingpage = 1;
                isHasNext = true;
                mLoadForViewManager.loadNextPage(true);
            }
        }
    }


    LinkedHashMap<String,InViewGroupWithAdaper>  specialItemMap;
    public void setOtherItemView(String topic_view, InViewGroupWithAdaper mInViewGroupWithAdaper) {
        if(specialItemMap==null){
            specialItemMap=new LinkedHashMap();
        }
        specialItemMap.put(topic_view,mInViewGroupWithAdaper);
    }

    public InViewGroupWithAdaper getInViewGroupWithAdaper(String s) {
        if(specialItemMap==null)return null;
        return specialItemMap.get(s);
    }


    LinkedHashMap<Future,Long> map=new LinkedHashMap();
    public void putKey(Future mFuture, long key) {
        map.put(mFuture,key);
    }

    public long getFutrue(Future mFuture) {
        return map.get(mFuture);
    }

    //推荐 end====================================================


}
