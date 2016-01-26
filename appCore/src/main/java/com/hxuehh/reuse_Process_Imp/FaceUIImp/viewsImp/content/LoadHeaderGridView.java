package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content;

import android.annotation.TargetApi;
import android.hxuehh.com.R;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.AdapterViewOnItemClickListener;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.LoadForViewManager;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.headered.HeaderGridView;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullToRefreshBase_1;
import com.hxuehh.appCore.develop.LogUtil;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.LoadView;
import com.hxuehh.appCore.faceFramework.faceUI.viewAdapter.FaceListAdapter;
import com.hxuehh.reuse_Process_Imp.staticKey.TabClickObservable;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.headered.PullToRefreshHeaderGridView_3;


import java.util.List;

public class LoadHeaderGridView extends LoadView {
    private static final String TAG = LoadHeaderGridView.class.getSimpleName();

    private boolean isList;
    private PullToRefreshHeaderGridView_3 mPullToRefreshHeaderGridView;   //存放列表控件容器
    private HeaderGridView mHeaderGridView;                             //列表控件
    private View mSpecialFooterView;                                    //特殊footer view
    private View mFooterView;                                           //footer view
    private View mTabFooterView;
    private LoadForViewManager mLoadForViewManager;                   //
    private FaceListAdapter mDealSwipeListAdapter;
    private FaceBaseActivity_1 mContext;

    private int isfrombrand = -1;
    private int mPosition;
    private int mFirstVisibleItem = -1;
    FaceCommCallBack mFaceCommCallBack;

    //boolean isToBottom = true;
    public LoadHeaderGridView(FaceBaseActivity_1 context, LoadForViewManager manager, FaceListAdapter adapter) {

        this.mContext = context;
        this.mLoadForViewManager = manager;
        this.mDealSwipeListAdapter = adapter;
        initViews();
        registerListeners();
        mHeaderGridView.setAdapter(mDealSwipeListAdapter);
    }

    public LoadHeaderGridView(FaceBaseActivity_1 context, LoadForViewManager manager, FaceListAdapter adapter, int isfrombran, int position,FaceCommCallBack faceCommCallBack) {
        this.mPosition = position;
        this.mContext = context;
        isfrombrand = isfrombran;
        this.mLoadForViewManager = manager;
        this.mDealSwipeListAdapter = adapter;
        mFaceCommCallBack=faceCommCallBack;
        initViews();
        registerListeners();
        mHeaderGridView.setAdapter(mDealSwipeListAdapter);
    }

    private void initViews() {

        if (isfrombrand == 1) {//母婴
            mainView = LayoutInflater.from(mContext).inflate(R.layout.layer_base_listview_muying, null);
        } else if (isfrombrand == 2) {//品牌团
            mainView = LayoutInflater.from(mContext).inflate(R.layout.layer_base_listview_brand, null);
        } else if (isfrombrand == 3) {//品牌团详情
            mainView = LayoutInflater.from(mContext).inflate(R.layout.layer_base_listview_brand_detail, null);
        } else {
            mainView = LayoutInflater.from(mContext).inflate(R.layout.layer_base_listview, null);
        }
        mPullToRefreshHeaderGridView = (PullToRefreshHeaderGridView_3) mainView.findViewById(R.id.pull_header_grid_view);
     //   mPullToRefreshHeaderGridView.setMode(PullToRefreshBase_1.MODE_PULL_DOWN_TO_REFRESH);
      mPullToRefreshHeaderGridView.setMode(PullToRefreshBase_1.MODE_DISABLE);

        mHeaderGridView = mPullToRefreshHeaderGridView.getRefreshableView();
//        mHeaderGridView.setBackgroundResource(R.color.v_line_color_f6);
        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.include_list_footer, null);
        mSpecialFooterView = View.inflate(mContext, R.layout.list_footer, null);
        mTabFooterView = View.inflate(mContext, R.layout.tabmorefooter, null);

        // addHeaderView();
        addFooterView();
        addSpecialFooterView();
        addTabFooterView();

        hideFooterView();
        hideSpecialFootView();
        hideTabFooterView();

        mHeaderViewContainer = new FrameLayout(mContext);
        mHeaderGridView.addHeaderView(mHeaderViewContainer);
//        mHeaderViewContainer=mHeaderGridView;
    }


    private void registerListeners() {
        mTabFooterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (faceCommCallBackTabClick != null) {
                    faceCommCallBackTabClick.callBack(true);
                }
            }
        });
        PullToRefreshBase_1.OnRefreshListener onRefreshListener = new PullToRefreshBase_1.OnRefreshListener() {
            @Override
            public void onRefresh(boolean is) {
                if(mFaceCommCallBack!=null){
                    mFaceCommCallBack.callBack(true);
                }
                mLoadForViewManager.reLoadFromStart();
                hideFooterView();
                hideSpecialFootView();
                hideTabFooterView();
            }
        };

        mPullToRefreshHeaderGridView.setOnRefreshListener(onRefreshListener);

        AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //int a=mHeaderGridView.getHeaderViewCount();
                int tempPos = position - mHeaderGridView.getHeaderViewCount() * mHeaderGridView.getColumnsNum();
                if (tempPos >= mDealSwipeListAdapter.getList().size() || tempPos < 0) {
                    return;
                }

                Object oo = mDealSwipeListAdapter.getList().get(tempPos);
                if (oo != null) {
                    AdapterViewOnItemClickListener mAdapterViewOnItemClickListener = (AdapterViewOnItemClickListener) oo;
                    try {
                        mAdapterViewOnItemClickListener.OnItemClickListener(mContext, view, tempPos, mDealSwipeListAdapter.getList(), mLoadForViewManager.getmLoadSetting());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (FaceException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        mHeaderGridView.setOnItemClickListener(mOnItemClickListener);
        mHeaderGridView.setOnMoveTouchListenerDistance(new HeaderGridView.OnMoveTouchListener() {
            @Override
            public void onMoveUp() {
                if (faceCommCallBackForShowTopView != null && mDealSwipeListAdapter.getCount() > 2) {
                    faceCommCallBackForShowTopView.callBack(false);
                }
            }

            @Override
            public void onMoveDown() {
                if (faceCommCallBackForShowTopView != null && mDealSwipeListAdapter.getCount() > 2) {
                    faceCommCallBackForShowTopView.callBack(true);
                }
            }
        });

        AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
            private boolean isScrollEnd
                    ,
                    isScrollTop;
            private boolean isToBottom = false;
            int mFirstItem;
            int mVisibleItemCount;
            int mTotalItemCount;
            boolean isLoadPa;
            boolean isIn;//是不是展示头部
            boolean isScroll;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
//                Su.logPullView(mFirstItem+"  now"+firstVisibleItem);
                if (!isScroll && mFirstItem != firstVisibleItem) {
                    isScroll = true;
                    isToBottom = mFirstItem < firstVisibleItem;
                }

                mFirstItem = firstVisibleItem;
                mTotalItemCount = totalItemCount;
                mVisibleItemCount = visibleItemCount;
                mFirstVisibleItem = firstVisibleItem;
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:

                        if (isLoadPa) return;
                        mLoadForViewManager.pauseLoadImage();
                        isLoadPa = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                    {//load next
                        int last=mFirstItem + mVisibleItemCount;
                        isScrollEnd =last >= mTotalItemCount;
                        isScrollTop = mFirstItem == 0;

                        if (isAutoLoadData) {
                            if (!isMustLoadNextFromTop() && isScrollEnd) {
                                if (mPullToRefreshHeaderGridView.isFlingUp()) {
                                    mLoadForViewManager.loadNextPage(true);
                                }
                            } else if (isMustLoadNextFromTop() && isScrollTop) {
                                mLoadForViewManager.loadNextPage(true);
                            }
                        }

                        if (faceCommCallBackMuyingBackTop != null) {
                            if ((isScrollEnd && mFirstItem>6)||last > mHeaderGridView.getColumnsNum() * 10) {
                                faceCommCallBackMuyingBackTop.callBack(true);
                            } else {
                                faceCommCallBackMuyingBackTop.callBack(false);
                            }
                        }
                    }

                    {//true 位置
                        boolean isShowHead = mFirstItem < mHeaderGridView.getHeaderViewCount() * getColumnsNum();
                        boolean isShowFooter = mFirstItem + mVisibleItemCount > mTotalItemCount - mHeaderGridView.getFooterViewsCount() * getColumnsNum();

                        mFirstItem = mFirstItem - (!isShowHead ? mHeaderGridView.getHeaderViewCount() * getColumnsNum() : 0);
                        mVisibleItemCount = mVisibleItemCount -
                                (isShowHead ? mHeaderGridView.getHeaderViewCount() * getColumnsNum() : 0) -
                                (isShowFooter ? (mHeaderGridView.getFooterViewsCount() > 0 ? 1 : 0) * getColumnsNum() : 0);

                        Su.logPush("mFirstItem" + mFirstItem + " mVisibleItemCount" + mVisibleItemCount);

                        if (mLoadForViewManager.isMustPreLoadItem() || mLoadForViewManager.isMustAnalysisItem()) {
                            mLoadForViewManager.onScrollShow(mFirstItem, mFirstItem + mVisibleItemCount, isToBottom, mVisibleItemCount);
                        }
                    }

                    isScroll = false;

                    if (!isLoadPa) return;
                    mLoadForViewManager.startLoadImage();
                    isLoadPa = false;
                    break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

                        if (!isLoadPa) return;
                        mLoadForViewManager.startLoadImage();
                        isLoadPa = false;

                        break;
                    default:
                        break;
                }
            }

        };

        mHeaderGridView.setOnScrollListener(mOnScrollListener);
    }

    public void showWaterFallPicPage() {//展示2个
        mHeaderGridView.setNumColumns(2);
        mDealSwipeListAdapter.setItemViewKey(ViewKeys.ItemGridView);
        // mDealSwipeListAdapter.notifyDataSetChanged();
        isList = false;
    }

    public void showSwipeListPage() {//展示1个
        mHeaderGridView.setNumColumns(1);
        mDealSwipeListAdapter.setItemViewKey(ViewKeys.ItemListView);
        mDealSwipeListAdapter.notifyDataSetChanged();
        isList = true;
    }

    public void setNumColumns(int numColumns) {
        mHeaderGridView.setNumColumns(numColumns);
    }

    @Override
    public int getColumnsNum() {
        return mHeaderGridView.getColumnsNum();
    }

    @Override
    public void callReportScrollStateChange() {
        mHeaderGridView.smoothScrollBy(1, 1);
    }

    public void setHorizontalSpacing(int withSpace, int heightSpace) {
        mHeaderGridView.setHorizontalSpacing(withSpace);
        mHeaderGridView.setVerticalSpacing(heightSpace);
    }

    private void addFooterView() {
        if (mFooterView != null) {
            mHeaderGridView.addFooterView(mFooterView);
            mFooterView.setVisibility(View.GONE);
        }
    }

    private void showFooterView() {
        if (mFooterView != null) {
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

    private void hideFooterView() {
        if (mHeaderGridView != null) {
            mFooterView.setVisibility(View.GONE);
        }
    }

    private void addSpecialFooterView() {
        if (mSpecialFooterView != null) {
            mHeaderGridView.addFooterView(mSpecialFooterView);
            mSpecialFooterView.setVisibility(View.GONE);
        }
    }

    private void showSpecialFooterView() {
        if (mSpecialFooterView != null) {
            mSpecialFooterView.setVisibility(View.VISIBLE);
        }
    }

    private void hideSpecialFootView() {
        if (mSpecialFooterView != null) {
            mSpecialFooterView.setVisibility(View.GONE);
        }
    }


    private void addTabFooterView() {
        if (mTabFooterView != null) {
            mHeaderGridView.addFooterView(mTabFooterView);
            mTabFooterView.setVisibility(View.GONE);
        }
    }

    private void showTabFooterView() {
        if (mTabFooterView != null) {
            mTabFooterView.setVisibility(View.VISIBLE);
        }
    }

    private void hideTabFooterView() {
        if (mTabFooterView != null) {
            mTabFooterView.setVisibility(View.GONE);
        }
    }

    private boolean isMustNeedEndTag = true;

    @Override
    public void loading(boolean isHasMoreToLoad, boolean isMustNeedEndTag) {
        this.isMustNeedEndTag = isMustNeedEndTag;
        if (!isHasMoreToLoad) {
            if (isMustNeedEndTag) {
               /* if (isfrombrand == 2 && (mPosition == 0 || mPosition == 1)) {
                    if (mDealSwipeListAdapter != null && mDealSwipeListAdapter.getCount() > 0) {
                        showTabFooterView();
                    } else {
                        hideTabFooterView();
                    }
                } else if (isfrombrand == 1 && mPosition == 0) {
                    if (mDealSwipeListAdapter != null && mDealSwipeListAdapter.getCount() > 0) {
                        showTabFooterView();
                    } else {
                        hideTabFooterView();
                    }
                } else {
                    if (mDealSwipeListAdapter != null && mDealSwipeListAdapter.getCount() > 0) {
                        showSpecialFooterView();
                    } else {
                        hideSpecialFootView();
                    }
                }*/

            } else {
                if (isfrombrand == 2 || isfrombrand == 1) {
                    hideTabFooterView();
                } else {
                    hideSpecialFootView();
                }
//                Tao800Util.showShortToast(mContext, R.string.pull_to_refresh_nodata);
            }
            return;
        }
        if (mDealSwipeListAdapter != null && mDealSwipeListAdapter.getCount() > 0) {
            hideTabFooterView();
            showFooterView();
        } else {
            hideFooterView();
        }
    }

    @Override
    public void loaded(Long loadTime, int thisPageNumber, boolean isFromZero, boolean isHasMoreToLoad, List data) {
        refreshComplete();
        checkIfHasMoreToLoad(isHasMoreToLoad);
    }

    @Override
    public void loadErr(int errKey, int errTimes, boolean isReload, Exception... err) {
        refreshComplete();
    }

    @Override
    public void backTopView() {
        mHeaderGridView.smoothScrollToPosition(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHeaderGridView.setSelection(0);
            }
        }, TabClickObservable.SCROLL_TIME);
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public void setLast(int i) {
        mHeaderGridView.smoothScrollToPosition(i);
    }


    @Override
    public void setSelectIndex(int index) {
        // mHeaderGridView.setSelection(index + mHeaderGridView.getHeaderViewCount());
        mHeaderGridView.setSelection(index);
        LogUtil.d("---------------index = " + index + "---count = " + mHeaderGridView.getHeaderViewCount());
    }

    @Override
    public void setSelectIndexByScroll(final int index) {
        mHeaderGridView.smoothScrollToPosition(index);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mHeaderGridView.setSelection(index);
            }
        }, TabClickObservable.SCROLL_TIME);
    }


    @Override
    public void setBackToTopAndRefresh() {
        mPullToRefreshHeaderGridView.setHeaderRefreshing();
        mHeaderGridView.setSelection(0);
    }

    @Override
    public void setPullToRefreshMode(int mode) {
        mPullToRefreshHeaderGridView.setMode(mode);
    }

    private void refreshComplete() {
        mPullToRefreshHeaderGridView.onRefreshComplete();
    }

    public void checkIfHasMoreToLoad(boolean hasMore) {
        if (hasMore) {
            showFooterView();
            hideSpecialFootView();
            hideTabFooterView();
        } else {
            if (isMustNeedEndTag) {
                if (isfrombrand == 2 && (mPosition == 0 || mPosition == 1)) {
                    showTabFooterView();
                } else if (isfrombrand == 1 && mPosition == 0) {
                    showTabFooterView();
                } else {
                    showSpecialFooterView();
                }
            }
            hideFooterView();
        }
    }

    @Override
    public void endLoadNextPageUI() {
        super.endLoadNextPageUI();
        hideFooterView();

    }


    @Override
    public void startLoadNextPageUI() {
        super.startLoadNextPageUI();
        showFooterView();
    }

    @Override
    public void hideFootView() {
        hideTabFooterView();
        hideSpecialFootView();
    }
}
