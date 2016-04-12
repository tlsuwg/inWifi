package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hxuehh.com.R;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.IDSameable;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.InViewGroupWithAdaper;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.ViewCallBackDispenser_A;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content.LoadListView;
import com.hxuehh.appCore.develop.FaceEventViewINforDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.ScreenUtil;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.fragments.FaceBaseFragment;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.LoadView;
import com.hxuehh.appCore.faceFramework.faceUI.viewAdapter.FaceListAdapter;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content.LoadHeaderGridView;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content.LoadStaggeredGridView;
import com.hxuehh.appCore.develop.DevRunningTime;
import com.hxuehh.appCore.develop.FaceEventCallBackINforDlp;
import com.hxuehh.appCore.develop.FaceEventInforDlp;
import com.hxuehh.reuse_Process_Imp.staicUtil.utils.TaoCCUtil;

//import com.hxuehh.rebirth.stickygrid.StickyGridHeadersSimpleArrayAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by suwg on 2014/5/24.
 * 是一个加载逻辑？
 * 是一个事件的传递？
 * 是一个视图！？
 * 是一个视图
 */

public class AutoLoadContainerView extends FaceGetMainViewImp {
    private  boolean  isNotRefession=false;
    private static final String TAG = AutoLoadContainerView.class.getSimpleName();

    public boolean isMustNeedEndTag = true;//是不是展示尾部 加载完成

    @Deprecated
    public boolean isMustAutoListenerNetStatus = false;//是不是自动监听连接情况并加载数据

    private FaceContextWrapImp  mFaceContextWrap;
    private FaceBaseFragment mFragmentContext;


    private boolean isOnTop;
    // 内容视图

    @FaceEventViewINforDlp
    private LoadView mContentView;

    @FaceEventInforDlp
    private LoadForViewManager mLoadForViewManger;

    @FaceEventCallBackINforDlp
    private ViewCallBackDispenser_A<List, Exception> mViewCallBackDispenser_A;

    @FaceEventViewINforDlp
    @FaceEventCallBackINforDlp
    private WarnView mWarnView;


    private HashSet<String> removeSameSet;
    private volatile List mAllData;
    private FaceListAdapter mFaceListAdapter;


//    private StickyGridHeadersSimpleArrayAdapter stickyGridHeadersSimpleArrayAdapter;

    LoadCursorSetting mLoadCursorSetting;
    private String mBannerImgUrl;
    private boolean isHasMoreToLoad = true;

    int viewType = -1;

    private boolean isRecommended = false;//是否有推荐商品
    private boolean isAutoLoadData;

    private boolean showNoDataView = true;
    private EditText editText;
    private int mPositin;//品牌图tab跳转位置

    private TextView mBackTopView; // 返回顶部的view
    public boolean noDataViewGone;
    boolean isLoadedData;

    /**
     * 设置是否支持自动加载
     * @param autoLoadData
     */
    public void setAutoLoadData(boolean autoLoadData) {
        this.mContentView.setAutoLoadData(autoLoadData);
    }

    /**
     * 设置滑动模式
     * @param mode
     * @see com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullToRefreshBase_1
     * <p/>
     * setPullToRefreshMode(PullToRefreshBase_1. MODE_DISABLE );
     */
    public void setPullToRefreshMode(int mode) {
        this.mContentView.setPullToRefreshMode(mode);
    }

    public List getAllData() {
        return mAllData;
    }

    public boolean isShowNoDataView() {
        return showNoDataView;
    }

    public void setShowNoDataView(boolean showNoDataView) {
        this.showNoDataView = showNoDataView;
    }




    @Deprecated
    public AutoLoadContainerView(int viewType, FaceContextWrapImp context, LoadCursorSetting mLoadCursorSetting,
                                 boolean isMustNeedEndTag, boolean isMustAutoListenerNetStatus, EditText editText) {
        this(viewType, context, mLoadCursorSetting, isMustNeedEndTag, isMustAutoListenerNetStatus);
        this.editText = editText;
    }

    public AutoLoadContainerView(int viewType, FaceContextWrapImp context , LoadCursorSetting mLoadCursorSetting,
                                 boolean isMustNeedEndTag, boolean isMustAutoListenerNetStatus, int position) {
        super(context.getFaceContext());
        this.mPositin = position;
        this.viewType = viewType;
        this.mFaceContextWrap = context;
        this.isMustNeedEndTag = isMustNeedEndTag;
        this.isMustAutoListenerNetStatus = isMustAutoListenerNetStatus;
        this.isMustAutoListenerNetStatus = false;

        this.mLoadCursorSetting = mLoadCursorSetting;
        if (mLoadCursorSetting.isMustNeedLoadData) {
            initCallBackDispenser();
        }
        mLoadForViewManger = new LoadForViewManager(mViewCallBackDispenser_A, mLoadCursorSetting, mFaceContextWrap);
        mLoadCursorSetting.setLoadForViewManger(mLoadForViewManger);
        initData();
        linkViewData();
        initViews();
        registerListeners();
        setAutoLoadData(mLoadCursorSetting.isMustNeedLoadData);

    }


    //推荐使用
    public AutoLoadContainerView(int viewType, FaceContextWrapImp context, LoadCursorSetting mLoadCursorSetting,
                                 boolean isMustNeedEndTag, boolean isMustAutoListenerNetStatus) {
        super(context.getFaceContext());
        this.viewType = viewType;
        this.mFaceContextWrap = context;
        this.isMustNeedEndTag = isMustNeedEndTag;
        this.isMustAutoListenerNetStatus = isMustAutoListenerNetStatus;
        this.isMustAutoListenerNetStatus = false;

        this.mLoadCursorSetting = mLoadCursorSetting;
        if (mLoadCursorSetting.isMustNeedLoadData) {
            initCallBackDispenser();
        }
        mLoadForViewManger = new LoadForViewManager(mViewCallBackDispenser_A, mLoadCursorSetting, mFaceContextWrap);
        mLoadCursorSetting.setLoadForViewManger(mLoadForViewManger);
        initData();
        linkViewData();
        initViews();
        registerListeners();
        setAutoLoadData(mLoadCursorSetting.isMustNeedLoadData);
    }

    public View getHeadView() {
        return mContentView.getHeadView();
    }

    public void setHeadView(View headView) {
        mContentView.setHeadView(headView);

    }

    int oldSize;

    private void initCallBackDispenser() {

        mViewCallBackDispenser_A = new ViewCallBackDispenser_A<List, Exception>() {

            private boolean isViewLoading;


            @Override
            public synchronized boolean isViewLoading() {
                return isViewLoading;
            }

            @Override
            public synchronized void setViewLoading(boolean isViewLoading) {
                this.isViewLoading = isViewLoading;
            }

            @Override
            @FaceEventCallBackINforDlp
            public void loading() {
                setViewLoading(true);
//                boolean isReloadRemove= (mLoadCursorSetting.MustLoadedShowType==LoadCursorSetting.MustReLoadedShowCurrent)&&mLoadCursorSetting.isReLoadFromSart;
//                boolean showPro=false;
//                if(isReloadRemove){
//                    if (mAllData != null && mAllData.size() >0) {
//                        mAllData.clear();
//                        if(mFaceListAdapter!=null)
//                        mFaceListAdapter.notifyDataSetChanged();
//                        showPro=true;
//                    }
//                }

                if (!isLoadedData && (mAllData == null || mAllData.size() == 0)) {
                    // setLoadStats(WarnView.LOADING_PROGRESS);
                    mWarnView.setLoadProgressStatus();
                }
                // 内容视图执行loading
                mContentView.loading(isHasMoreToLoad, isMustNeedEndTag);
            }

            @Override
            @FaceEventCallBackINforDlp
            public void loaded(Long loadTime, int thisPageNumber, boolean isReLoad, boolean isHasMoreToload, List data) {
                if((getContentView()instanceof LoadListView))
                {
                     ((LoadListView)getContentView()).getDataListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
                }



                {//原子操作  不可以分割啊  数据操作
                    boolean isDeleteAll = false;
                    boolean isShowAll = (mLoadCursorSetting.MustLoadedShowType == mLoadCursorSetting.MustLoadedShowAll);
                    if (isShowAll) {
                        isDeleteAll = (isReLoad && data != null && !data.isEmpty());
                    } else {
                        boolean isLoadedShowCurrent = mLoadCursorSetting.MustLoadedShowType == mLoadCursorSetting.MustLoadedShowCurrent;
                        boolean isReloadRemove = mLoadCursorSetting.MustLoadedShowType == mLoadCursorSetting.MustReLoadedShowCurrent;
                        boolean isFirstTimeRemoveOldAll = mLoadCursorSetting.MustLoadedShowType == mLoadCursorSetting.MustLoadedFirstTimeRemoveOldAll;

                        if (isLoadedShowCurrent || (isReLoad && isReloadRemove) ||
                                (!isFaceCommCallBackOneTimes && isFirstTimeRemoveOldAll)) {
                            isDeleteAll = true;
                        }
                    }

                    if (isDeleteAll) {
                        if (removeSameSet != null) removeSameSet.clear();
                        mAllData.clear();
//                        if (loadHeaderListView != null) loadHeaderListView.clearAllDateNotifi();
                    }

                    //推荐信息  只是添加一个  更改视图  对齐视图
                    {
                        if (mLoadCursorSetting.getAccurateObj() != null && isReLoad) {
                            mLoadCursorSetting.isMidLoadAccurateStatus_Wait_Data = false;
                            mLoadCursorSetting.isLoadAccurateDataStatus = false;
                        }
                    }

                    if (data != null) {
                        List list = data;
                        if (!list.isEmpty()) {
                            boolean isLoadedData2 = addInToCache(loadTime, thisPageNumber, isReLoad, isHasMoreToload, data);
                            if(isLoadedData2) {
                                isLoadedData =isLoadedData2;
                            }
                        } else {
                            noDataCallBack();
                        }
                    } else {
                        noDataCallBack();
                    }

                    if (mFaceListAdapter != null) {
                        mFaceListAdapter.notifyDataSetChanged();
                    }
//                    if(mFaceListAdapterHeader!=null){  //listview头视图
//                        mFaceListAdapterHeader.notifyDataSetChanged();
//                        ScreenUtil.setListViewHeightBasedOnChildren(linearListView);
//                    }

//                    if (stickyGridHeadersSimpleArrayAdapter != null) {
//                        stickyGridHeadersSimpleArrayAdapter.notifyDataSetChanged();
//                    }

                    if (oldSize == 0) {
                        mContentView.callReportScrollStateChange();
                    }
                }


                {//标签更改
                    if (isReLoad && !mLoadCursorSetting.isMustLoadNextFromTop) {
                        if (mLoadCursorSetting.isReLoadFromSartCallBack) {
                            isFaceCommCallBackOneTimes = false;
                        } else {
                            isFaceCommCallBackOneTimes = true;
                        }
                        if (mLoadCursorSetting.isNoDataChangeToRecommend) {
                            Su.log("推荐来的呢");
                        }
                    }

                    //一次
                    if (faceCommCallBackOneTimes != null && !isFaceCommCallBackOneTimes && mAllData.size() != 0) {
                        isFaceCommCallBackOneTimes = true;
                        faceCommCallBackOneTimes.callBack(mAllData);
                        if (mLoadCursorSetting.isMustLoadOnlyOneTimes) {//只是加载一次
                            setAutoLoadData(false);//不能加载
                            mLoadCursorSetting.isHasNext = false;//没有下一条
                            isHasMoreToload = false;
                        }
                    }

                    if (faceCommCallBackForAllTimes != null) {
                        faceCommCallBackForAllTimes.callBack(mAllData);
                    }
                    AutoLoadContainerView.this.isHasMoreToLoad = isHasMoreToload;
                }


                boolean isAutoLoad = false;
                {////自动加载推荐 实际判断   步骤1  判断到的时机
                    switch (mLoadCursorSetting.MustHybridizationLoadType) {
                        case LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain: {
                            if (!mLoadCursorSetting.isMidLoadAccurateStatus_Wait_Data//不是中间态
                                    && !mLoadCursorSetting.isLoadAccurateDataStatus//不是完成状态
                                    && mLoadCursorSetting.isHasToAccurate//存在
                                    && !mLoadCursorSetting.isHasNext//是否存在
                                    ) {
                                isAutoLoad = true;
                            }
                        }
                        break;
                        case LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains: {
                            if (!mLoadCursorSetting.isMidLoadAccurateStatus_Wait_Data//不是中间态
                                    && !mLoadCursorSetting.isLoadAccurateDataStatus//不是完成状态
                                    ) {
                                isAutoLoad = true;
                            }
                        }
                        break;
                    }

                }


                {//view更新
                    boolean isisHasMoreToload = isHasMoreToload;
                    if (isAutoLoad) {
                        isisHasMoreToload = true;
                    }
                    mContentView.loaded(loadTime, thisPageNumber, isReLoad, isisHasMoreToload, data);       // 内容视图执行loaded
                    if (isReLoad && thisPageNumber != 1&&!isNotRefession) {
                        showRefreshSuccess();
                    }
                    if(isReLoad && thisPageNumber != 1&&isNotRefession){
                        mWarnView.setLoadedOk();
                    }
                    //setLoadStats(WarnView.LOADED_OK);
                    if (!isLoadedData && (TaoCCUtil.isEmpty(mAllData) && !noDataViewGone)) {
                        if (showNoDataView) {
                            //setLoadStats(WarnView.LOADED_NO_DATA);
                            if (viewType == ViewKeys.AutoView_HeadGViewOnlyListImHis) {
                                mWarnView.setLoadedNoDataIMCon();
                            } else if(mLoadCursorSetting.isNoLinkedStatus){
                                mWarnView.setLoadedNoDataIMNoLink();
                            }
                            else {
                                mWarnView.setLoadedNoData();
                            }
                        }
                    } else {
                        mWarnView.setLoadedOk();
                    }



                    setViewLoading(false);
                }

                {//自动加载图片 测试
                    if (DevRunningTime.isCacheImage) {
                        for (Object oo : data) {
//                            ((BaseAllImp) oo).loadAllImage();//两次
//                            ((BaseAllImp) oo).loadAllImage();//两次
                        }
                    }
                }

                if (isAutoLoad) {
                    mLoadCursorSetting.changeAccurateParAndLoad();//步骤2
                    return;
                }


                {//自动加载
                    boolean isGoOn = DevRunningTime.isGoonLoadByRunningTime(mAllData, mFaceContextWrap.getFaceContext());
                    if (isGoOn && isHasMoreToload) {
                        mLoadForViewManger.loadNextPage(true);
                        mContentView.setLast(mAllData.size());
                    }

                    if (DevRunningTime.isShowEnd) {
                        mContentView.setLast(mAllData.size());
                    }
                }

              /*  if(SuApplication.isNoLinkedStatus){
                    mWarnView.setLoadRefreshSessionMuyingBrand(1, isRecommended);
                }*/

            }

            @Override
            @FaceEventCallBackINforDlp
            public void loadErr(int errKey, int errTimes, boolean isReload, Exception... err) {


                // 内容视图执行loadErr
                if(!isLoadedData||isReload) {
                    mContentView.loadErr(errKey, errTimes, isReload, err);
                    // 浮层执行setErrKey
                }
                // if()

                if (mAllData != null&&mAllData.size()>0) {
                    setErrKey(errKey, mAllData.size());
                } else {
                    //mContentView.loadErr(errKey, errTimes, isReload, err);
                    if (isLoadedData) {
                        setErrKey(errKey, 1);
                    } else {
                        setErrKey(errKey, 0);
                    }
                }
                setViewLoading(false);

                if (mAllData.size() == 0 && !(noDataViewGone&&errKey!= FaceLoadCallBack.ERR_no_net&&errKey!= FaceLoadCallBack.ERR_http_UnknownHostException)&&!isLoadedData) {
                    noDataCallBack();
                }
                else {//已经存在数据了
                    // setLoadStats(WarnView.LOADED_OK);
                    mWarnView.setLoadedOk();
                }
            }
        };
    }
    FaceCommCallBack faceCommCallBack=new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            isNotRefession=false;
            return false;
        }
    };
    private void linkViewData() {
        if (this.viewType == ViewKeys.AutoView_WaterView) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemWaterView);
        } else if (viewType == ViewKeys.AutoView_HeadGView) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemListView);
        } else if (viewType == ViewKeys.AutoView_HeadGViewOnlyList) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemListView);
        } else if (viewType == ViewKeys.AutoView_HeadGViewOnlyListImHis) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemListView);
        } else if (viewType == ViewKeys.AutoView_HeadGViewOnlyGird) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemGridView);
        } else if (viewType == ViewKeys.AutoView_PADView_4) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemPadView_4);
        } else if (viewType == ViewKeys.AutoView_PADView_3) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemPadView_3);
        }
//         else if (viewType == ViewKeys.AutoView_PADSTIDIYView_4) {
//            mLoadCursorSetting.setMustGroup(true);
//            stickyGridHeadersSimpleArrayAdapter = new StickyGridHeadersSimpleArrayAdapter<String>(mFaceContextWrap, mAllData,
//                    R.layout.header, R.layout.list_item_deal,viewType);
//        }else if(viewType == ViewKeys.AutoView_PADSTIDIYView){
//            stickyGridHeadersSimpleArrayAdapter = new StickyGridHeadersSimpleArrayAdapter<String>(mFaceContextWrap, mAllData,
//                    R.layout.header, R.layout.list_item_deal,viewType);
//        }
        else if (viewType == ViewKeys.AutoView_HeadGViewOnlyListLoadNextFromTop) {
            //添加特定的chat
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
            mFaceListAdapter.setItemViewKey(ViewKeys.ItemListView);
        } else if (viewType == ViewKeys.AutoView_Brand_MuYing) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
        } else if (viewType == ViewKeys.AutoView_Brand_MuYing_Two) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
        } else if (viewType == ViewKeys.AutoView_Brand) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
        } else if (viewType == ViewKeys.AutoView_BrandDetail) {
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
        } else if(viewType==ViewKeys.AutoView_HotBrand){
            mFaceListAdapter = new FaceListAdapter(mFaceContextWrap, mLoadCursorSetting);
        }
        else {
            throw new IllegalArgumentException("Illegal view type, please check ViewKeys.class!");
        }


        if (mFaceListAdapter != null)
            mFaceListAdapter.setList(mAllData);
    }

    private boolean addInToCache(Long loadTime, int thisPageNumber, boolean isFromZero, boolean isHasMoreToload, List list) {

        if (mLoadCursorSetting.isMustRemoveTheSame && mAllData != null && mAllData.size() > 0) {
            List listadd = new ArrayList();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object oo = it.next();
                IDSameable mFaceIDSameable = (IDSameable) oo;
                if (!removeSameSet.contains(mFaceIDSameable.getID())) {
                    removeSameSet.add(mFaceIDSameable.getID());
                    listadd.add(mFaceIDSameable);
                } else {
                    Su.log("重复的ID" + mFaceIDSameable);
                }
            }
            list.clear();
            list = null;
            list = listadd;
        }

        boolean isAdded = false;
        if (!isAdded && mLoadCursorSetting.isMustGroup) {//分组
            if (mAllData != null || mAllData.size() > 0) {
                list.addAll(mAllData);
            }
            mAllData.clear();

            oldSize = mAllData.size();
            mAllData.addAll(list);
            isAdded = true;
        }
        if (!isAdded && mLoadCursorSetting.isMustLoadNextFromTop) {
            oldSize = mAllData.size();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object oo = it.next();
                mAllData.add(0, oo);
            }
            isAdded = true;
        }

        switch (mLoadCursorSetting.MustHybridizationLoadType) {
            case LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain: {
                if (mLoadCursorSetting.isMidLoadAccurateStatus_Wait_Data) {
                    //步骤3  更改中间态
                    isRecommended = true;
                    mLoadCursorSetting.isMidLoadAccurateStatus_Wait_Data = false;
                    if (!list.isEmpty() && !mAllData.isEmpty()) {
//                                        添加null位置
                        if (mContentView.getColumnsNum() != 1 && mLoadCursorSetting.getAccurateNullObj() != null) {
                            int needAdd = mAllData.size() % mContentView.getColumnsNum();
                            if (needAdd != 0) {
                                for (int i = 0; i < needAdd; i++) {
                                    mAllData.add(mLoadCursorSetting.getAccurateNullObj());
                                    Su.logPullView("need add");
                                }
                            }
                        }
//                                        添加展示位
                        for (int i = 0; i < mContentView.getColumnsNum(); i++) {
                            mAllData.add(mLoadCursorSetting.getAccurateObj());
                        }
                    }

                } else {
                    isRecommended = false;
                }
            }
            break;
            case LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains: {
                if (!mLoadCursorSetting.isLoadAccurateDataStatus && list != null && !list.isEmpty()) {
//                    if (list.size() >= 6&&mPositin!=-2) {-2是
//                        InViewGroupWithAdaper mInViewGroupWithAdaper = mLoadCursorSetting.getAccurateObj();
//                        if (mInViewGroupWithAdaper != null) {
//                            List newlist = new ArrayList();
//                            if(mPositin==0)//添加头部
//                                addHeadViewItem(newlist);
//                            for (int i = 0; i <= 4; i++) {
//                                Object oo = list.remove(0);
//                                newlist.add(oo);
//                            }
//                            StaticProxyAccurateFlag mStaticProxyAccurate = (StaticProxyAccurateFlag) mInViewGroupWithAdaper;
//                            mStaticProxyAccurate.setViewKey(mFaceContextWrap.getViewKey());
//                            newlist.add(mInViewGroupWithAdaper);//点击
//                            mStaticProxyAccurate.setmFaceCommCallBack(new FaceCommCallBack() {
//                                @Override
//                                public boolean callBack(Object[] t) {
//                                    if(loadHeaderListView!=null){
//                                        loadHeaderListView.notifyDataChanged();
//                                    }
//                                    return false;
//                                }
//                            });
//
//                            if(loadHeaderListView!=null){
//                                loadHeaderListView.loaded(loadTime,  thisPageNumber,  isFromZero,  isHasMoreToload,  newlist);
//                            }
//
//                            mStaticProxyAccurate.setOo(list);//剩下的
//                            isAdded = true;
//                        }
//                    } else {
//                        Su.logPullView("品牌少于5");
                    if (mPositin == 0)//添加头部
                        addHeadViewItem(list);
//                        if(loadHeaderListView!=null) {
//                            loadHeaderListView.loaded(loadTime, thisPageNumber, isFromZero, isHasMoreToload, list);
//                            isAdded = true;
//                        }
//                    }
                    return true;
                }
            }
            break;



        }


        if (!isAdded) {
            oldSize = mAllData.size();
            mAllData.addAll(list);
        }

        return false;
    }

    private void addHeadViewItem(List list) {
        InViewGroupWithAdaper mInViewGroupWithAdaper=mLoadCursorSetting.getInViewGroupWithAdaper("topic_view");
        if(mInViewGroupWithAdaper!=null);
        list.add(0,mInViewGroupWithAdaper);
    }


    @FaceEventViewINforDlp
    public boolean onKeyBack() {
        return mLoadForViewManger.OnKeyBack();
    }


    @FaceEventViewINforDlp
    private void registerListeners() {
        mWarnView.setOnLoadErrorListener(new WarnView.OnLoadErrorListener() {
            @Override
            public void onAgainRefresh() {
                mLoadForViewManger.loadNextPage(true);
            }
        });
    }

    private void initData() {
        mAllData = new ArrayList();
        if (mLoadCursorSetting.isMustRemoveTheSame) {
            removeSameSet = new HashSet<String>();
        }

        mLoadForViewManger.setApterData(mAllData);
    }

//    LoadHeaderListView loadHeaderListView;

    private void initViews() {
        switch (viewType) {
            case ViewKeys.AutoView_HeadGView:
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
                ((LoadHeaderGridView) mContentView).showSwipeListPage();
                break;
            case ViewKeys.AutoView_HeadGViewOnlyList:
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
                ((LoadHeaderGridView) mContentView).showSwipeListPage();
                break;
            case ViewKeys.AutoView_HeadGViewOnlyListImHis:
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
                ((LoadHeaderGridView) mContentView).showSwipeListPage();
                break;
            case ViewKeys.AutoView_HeadGViewOnlyListLoadNextFromTop:// im消息
                mContentView = new LoadListView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter, editText);
                //((LoadListView) mContentView).showSwipeListPage();
                //为了处理
                ListView dataListView = ((LoadListView) mContentView).getDataListView();
                dataListView.setDividerHeight(0);
                break;
            case ViewKeys.AutoView_HeadGViewOnlyGird: {
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
                LoadHeaderGridView mLoadHeaderGridView = ((LoadHeaderGridView) mContentView);
                (mLoadHeaderGridView).showWaterFallPicPage();
            }
            break;
            case ViewKeys.AutoView_WaterView:
                mContentView = new LoadStaggeredGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
                break;
            case ViewKeys.AutoView_PADView_4: {
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
                LoadHeaderGridView mLoadHeaderGridView = ((LoadHeaderGridView) mContentView);
                mLoadHeaderGridView.setNumColumns(3);
                mLoadHeaderGridView.setHorizontalSpacing(10, 10);
            }
            break;

            case ViewKeys.AutoView_PADView_3: {
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
                LoadHeaderGridView mLoadHeaderGridView = ((LoadHeaderGridView) mContentView);
                mLoadHeaderGridView.setNumColumns(3);
                mLoadHeaderGridView.setHorizontalSpacing(20, 10);
            }
            break;

            case ViewKeys.AutoView_Brand_MuYing: {//母婴儿的 品牌列表
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter, 1, mPositin,null);
                ((LoadHeaderGridView) mContentView).showSwipeListPage();
            }
            break;
            case ViewKeys.AutoView_Brand_MuYing_Two: {//母婴儿的 单品列表
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter, 1, mPositin,null);
                LoadHeaderGridView mLoadHeaderGridView = ((LoadHeaderGridView) mContentView);
                (mLoadHeaderGridView).showWaterFallPicPage();
            }
            break;
            case ViewKeys.AutoView_BrandDetail: {//品牌团两列列表
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter, 3, -1,null);
                LoadHeaderGridView mLoadHeaderGridView = ((LoadHeaderGridView) mContentView);
                mLoadHeaderGridView.showWaterFallPicPage();
            }
            break;
            case ViewKeys.AutoView_Brand: {//品牌团列表
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter, 2, mPositin,faceCommCallBack);
//                loadHeaderListView = new LoadHeaderListView(mFaceContextWrap, mLoadCursorSetting,mAllData);
//                setHeadView(loadHeaderListView.getMainView());
//                loadHeaderListView.setAdapter();
                LoadHeaderGridView mLoadHeaderGridView = ((LoadHeaderGridView) mContentView);
                mLoadHeaderGridView.showSwipeListPage();
            }
            break;
            case ViewKeys.AutoView_HotBrand:
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);
//                loadHeaderListView = new LoadHeaderListView(mFaceContextWrap, mLoadCursorSetting,mAllData);
//                setHeadView(loadHeaderListView.getMainView());
//                loadHeaderListView.setAdapter();
                LoadHeaderGridView mLoadHeaderGridView = ((LoadHeaderGridView) mContentView);
                mLoadHeaderGridView.showSwipeListPage();
                break;
            case ViewKeys.AutoView_PADSTIDIYView: {
//                mContentView = new LoadStidiyGridView(mFaceContextWrap, mLoadForViewManger, stickyGridHeadersSimpleArrayAdapter,mBannerImgUrl);
            }

            break;
            default:
                mContentView = new LoadHeaderGridView((FaceBaseActivity_1)mFaceContextWrap.getFaceContext(), mLoadForViewManger, mFaceListAdapter);

        }
        mContentView.setIsMustLoadNextFromTop(mLoadCursorSetting.isMustLoadNextFromTop);
        mainView=new FrameLayout(getContext());
        //        下面一层
        ((FrameLayout)mainView).addView(mContentView.getMainView(), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        mWarnView = new WarnView((Activity)mFaceContextWrap.getFaceContext());
        //   mWarnView.setVisibility(View.GONE);
        if(viewType==ViewKeys.AutoView_Brand&& mLoadCursorSetting.isHaveCategoty){
            FrameLayout.LayoutParams layoutParams =new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layoutParams.topMargin= ScreenUtil.dip2px(getContext(), 90);
            ((FrameLayout)mainView). addView(mWarnView,layoutParams);
        }else{
            ((FrameLayout)mainView).addView(mWarnView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }

//        上面一层
        // 为列表添加返回到顶部的按钮逻辑
        View mBackTopContainer = LayoutInflater.from(mFaceContextWrap.getFaceContext()).inflate(R.layout.layer_back_top_container, null);
//        backTopView.setVisibility(View.INVISIBLE);
        mBackTopView = (TextView) mBackTopContainer.findViewById(R.id.btm_muying_return_top);
        ((FrameLayout)mainView).addView(mBackTopContainer, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
     }


    @Override
    protected void initView() {


    }

    @Override
    public boolean isOnTop() {
        return isOnTop;
    }

    @Override
    @FaceEventViewINforDlp

    @Face_UnsolvedForDlp
    public void setOnTop(boolean isOnTop) {
        this.isOnTop = isOnTop;

        Su.log("this" + this.toString() + " " + mLoadForViewManger.toString() + " " + mAllData.hashCode() + isOnTop);
        if (isOnTop) {
            if (isMustAutoListenerNetStatus) {
                setNetListener();
            }
            if ((mAllData != null && mAllData.size() == 0) || mLoadCursorSetting.isMidLoadAccurateStatus_Wait_Data) {
                mLoadForViewManger.loadNextPage(true);
            } else if (mAllData.size() > 0) {
                //setLoadStats(WarnView.LOADED_OK);//
                mWarnView.setLoadedOk();
                notifyDataSetChanged();
            }

            mLoadForViewManger.startLoadImage();
            if (DevRunningTime.isSuTestM_C) {
                DevRunningTime.isGoonLoadByRunningTime(mAllData, mFaceContextWrap.getFaceContext());
            }

        } else {
            if (isMustAutoListenerNetStatus) {
                setDisNetListener();
            }


            mLoadForViewManger.stopLoadData();
            mLoadForViewManger.stopLoadImage();

            if (mLoadCursorSetting.isMustAnalysisItem) {
//                ExposeManger.getInstance().sendAll();
            }
        }

//        this.postInvalidate();//刷新
    }

    @FaceEventViewINforDlp
    private BroadcastReceiver mConnectivityChangedReceiver;

    @FaceEventViewINforDlp
    private void setNetListener() {
        // 网络变化
        if (mConnectivityChangedReceiver == null) {
            mConnectivityChangedReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {

                    NetworkInfo networkInfo = (NetworkInfo) intent
                            .getParcelableExtra("networkInfo");

                    boolean hasNetToDoSome = (networkInfo != null) && (networkInfo.isConnected());

                    Su.log("接收网络监听  hasNetToDoSome  " + hasNetToDoSome);

                    if (hasNetToDoSome) {
                        hasNetToDoSome();
                    } else {
                        unHasNet();
                    }
                }
            };
        }
        mFaceContextWrap.getFaceContext().registerReceiver(this.mConnectivityChangedReceiver,
                new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

    }

    @FaceEventViewINforDlp
    private void setDisNetListener() {
        if (mConnectivityChangedReceiver == null) return;
        mFaceContextWrap.getFaceContext().unregisterReceiver(this.mConnectivityChangedReceiver);
        mConnectivityChangedReceiver = null;
    }

    @FaceEventViewINforDlp
    private void unHasNet() {
        if (!isOnTop()) return;
//       是不是提示网络出错呢
    }

    @FaceEventViewINforDlp
    private void hasNetToDoSome() {
        if (isOnTop() && mAllData != null && mAllData.size() == 0) {
            mLoadForViewManger.loadNextPage(true);//只要是加载就会展示的
        }
    }

    //只是从0加载
    public boolean reLoadFromStart(boolean isRefession) {
        if(viewType == ViewKeys.AutoView_Brand){
            mWarnView.setLoadProgressStatus();

        }
        isNotRefession=isRefession;
        return mLoadForViewManger.reLoadFromStart();
    }


   /* private void setLoadStats(int statusCode) {
        if (viewType == ViewKeys.AutoView_Brand_MuYing || viewType == ViewKeys.AutoView_Brand_MuYing_Two) {
            mWarnView.setLoadStats(statusCode, 1);
        } else if (viewType == ViewKeys.Brand) {
            mWarnView.setLoadStats(statusCode, 2);
        } else if (viewType == ViewKeys.AutoView_BrandDetail) {
            mWarnView.setLoadStats(statusCode, 3);
        } else {
            mWarnView.setLoadStats(statusCode);
        }
        mContentView.endLoadNextPageUI();
    }*/

    private void setErrKey(int errorCode, int count) {
        mWarnView.setErrKey(errorCode, count);
        mContentView.endLoadNextPageUI();
    }

    private void showRefreshSuccess() {
        // setLoadStats(mWarnView.LOAD_REFRESH_SESSION);
        if (viewType == ViewKeys.AutoView_Brand_MuYing || viewType == ViewKeys.AutoView_Brand_MuYing_Two) {
            mWarnView.setLoadRefreshSessionMuyingBrand(1, isRecommended);
            //mWarnView.setLoadStats(statusCode, 1);
        } else {
            //mWarnView.setLoadStats(statusCode);
            mWarnView.setLoadRefreshSession(isRecommended);
        }
        mContentView.endLoadNextPageUI();
        mWarnView.postDelayed(new Runnable() {

            @Override
            public void run() {
                //setLoadStats(mWarnView.LOAD_REFRESH_GONE);
                mWarnView.setLoadRefreshGone();

            }
        }, 2000);

       /* mFaceContextWrap.getHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //setLoadStats(mWarnView.LOAD_REFRESH_GONE);
                mWarnView.setLoadRefreshGone();
            }
        }, 2000);*/
    }


    public void onDestroy() {
        isFaceCommCallBackOneTimes = false;
        mAllData.clear();
        removeSameSet.clear();
    }

    public LoadView getContentView() {
        return mContentView;
    }


    FaceCommCallBack
            faceCommCallBackOneTimes,//第一次加载完成
            faceCommCallBackForNOData,//没有数据展示 多次
            faceCommCallBackForAllTimes;//多次回调

    boolean isFaceCommCallBackOneTimes;//只是一次的回调

    public void setfaceCommCallBackOneTimes(FaceCommCallBack faceCommCallBack) {
        this.faceCommCallBackOneTimes = faceCommCallBack;
    }

    public void setNoDataCallBack(FaceCommCallBack faceCommCallBackForNOData) {
        this.faceCommCallBackForNOData = faceCommCallBackForNOData;
    }

    public void setFaceCommCallBackForAllTimes(FaceCommCallBack faceCommCallBackForAllTimes) {
        this.faceCommCallBackForAllTimes = faceCommCallBackForAllTimes;
    }

    //    这个可以多次调用；加载到没有数据就调用   返回全部数据
//    最后一次也调用
    private void noDataCallBack() {
        if (faceCommCallBackForNOData != null) {
            faceCommCallBackForNOData.callBack(mAllData);
        }
    }

    public void setSelectIndex(int index) {//选中
        mContentView.setSelectIndex(index);
    }

    public void setBackToTopAndRefresh() {//返回顶部并刷新（有下拉的动作）
        mContentView.setBackToTopAndRefresh();
    }

    public void setSelectIndexByScroll(int index) {
        mContentView.setSelectIndexByScroll(index);
    }

    public void removeAllData(List dealList) {
        mAllData.removeAll(dealList);
        mFaceListAdapter.notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        if (mFaceListAdapter != null) {
            mFaceListAdapter.notifyDataSetChanged();
        }
        /*if (stickyGridHeadersSimpleArrayAdapter != null) {
            stickyGridHeadersSimpleArrayAdapter.notifyDataSetChanged();
        }*/
    }

    //    删除 展示 reload
    public boolean reLoadFromStartClearDataToShowLoading() {
        mAllData.clear();
        mFaceListAdapter.notifyDataSetChanged();
        mContentView.hideFootView();

        return reLoadFromStart(false);
    }

    //    滑动展示隐藏上部
    public void setFaceCommCallBackForShowTopView(FaceCommCallBack faceCommCallBackForShowTopView) {
        mContentView.setFaceCommCallBackForShowTopView(faceCommCallBackForShowTopView);
    }

    //母婴返回顶部
    public void setFaceCommCallBackForMuyingBackTop(FaceCommCallBack faceForMuyingBackTop) {
        mContentView.setFaceCommCallBackMuyingBackTop(faceForMuyingBackTop);
    }

    public void addOneItemToLastNotifi(Object messageXmpp) {
        addOneItemToLast(messageXmpp);
        setLast();
    }
    public void  addOneItemtoLastOnly(Object messageXmpp){
        addOneItemToLast(messageXmpp);
    }

    public void addOneItemToLast(Object messageXmpp) {
        if (mAllData.isEmpty()) {
//            setLoadStats(WarnView.LOADED_OK);
            mWarnView.setLoadedOk();
        }
        mAllData.add(messageXmpp);
    }

    public void addOneItemToFirstNotifi(Object oo) {
        mAllData.add(0, oo);
        //setLoadStats(WarnView.LOADED_OK);
        mWarnView.setLoadedOk();
        mContentView.endLoadNextPageUI();
    }


    public void setLast() {
        mFaceListAdapter.notifyDataSetChanged();
        this.getMainView().postDelayed(new Runnable() {
            @Override
            public void run() {
                mContentView.setLast(mAllData.size());
            }
        }, 500);

    }

    //只是添加
    public void addClearOldAllDataNotifi(List goodsList) {
        mAllData.clear();
        mAllData.addAll(goodsList);
        //setLoadStats(WarnView.LOADED_OK);
        mWarnView.setLoadedOk();
        mContentView.endLoadNextPageUI();
        mFaceListAdapter.notifyDataSetChanged();
    }

    //滑到最下
    public void addAllDataTolastNotClearOldNotifi(List goodsList) {
        if (mAllData.size() == 0) {
            // setLoadStats(WarnView.LOADED_OK);
            mWarnView.setLoadedOk();
            mContentView.endLoadNextPageUI();
        }
        mAllData.addAll(goodsList);
        setLast();
    }

    public void setFaceCommCallBackTabClick(FaceCommCallBack faceCommCallBackTabClick) {
        mContentView.setFaceCommCallBackTabClick(faceCommCallBackTabClick);
    }

    public void setLastPositinOnly() {
        mContentView.setLast(mAllData.size());
    }


    // add back top logic
    public void showBackTop(final FaceCommCallBack mFaceCommCallBackForTop) {

        mBackTopView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFaceCommCallBackForTop!=null)mFaceCommCallBackForTop.callBack();
                getContentView().backTopView();
                mBackTopView.setVisibility(View.GONE);
            }
        });


        this.setFaceCommCallBackForMuyingBackTop(new FaceCommCallBack() {
            @Override
            public boolean callBack(Object[] t) {
                boolean isUp = (Boolean) t[0];
                setBackTopStatus(isUp);
                return false;
            }
        });
    }

    private void setBackTopStatus(boolean needVisiable) {
        if (mBackTopView == null) return;
        if (needVisiable) {
            if (mBackTopView.getVisibility() != View.VISIBLE) {
                mBackTopView.setAnimation(AnimationUtils.loadAnimation(mFaceContextWrap.getFaceContext(), R.anim.anim_show_switch_image));
                mBackTopView.setVisibility(View.VISIBLE);
            }
        } else {
            if (mBackTopView.getVisibility() == View.VISIBLE) {
                if (mBackTopView != null) {
                    mBackTopView.setAnimation(AnimationUtils.loadAnimation(mFaceContextWrap.getFaceContext(), R.anim.anim_hide_switch_image));
                    mBackTopView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }



}
