package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.hxuehh.com.R;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.AdapterViewOnItemClickListener;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.LoadForViewManager;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.LoadView;
import com.hxuehh.appCore.faceFramework.faceUI.viewAdapter.FaceListAdapter;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.im.IMPullToRefreshBase;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.im.IMPullToRefreshListView;
import com.hxuehh.reuse_Process_Imp.staticKey.TabClickObservable;


import java.util.List;

public class LoadListView extends LoadView {
    @Override
    public int getColumnsNum() {
        return 1;
    }

    @Override
    public void callReportScrollStateChange() {

    }

    private static final String TAG = LoadListView.class.getSimpleName();

    private boolean isList;

    private IMPullToRefreshListView mIMPullToRefreshListView;   //存放列表控件容器
    private ListView mListView;                             //列表控件

    private LoadForViewManager mLoadForViewManager;                   //
    private FaceListAdapter mDealSwipeListAdapter;
    private FaceBaseActivity_1 mContext;
    private InputMethodManager imm;
    private EditText editText;


    public LoadListView(FaceBaseActivity_1 context, LoadForViewManager manager, FaceListAdapter adapter,EditText editText) {

        this.mContext = context;
        this.mLoadForViewManager = manager;
        this.mDealSwipeListAdapter = adapter;
        this.editText=editText;
        initViews();
        registerListeners();
        mListView.setAdapter(mDealSwipeListAdapter);

    }


    private void initViews() {

        mainView = LayoutInflater.from(mContext).inflate(R.layout.layer_base_listview_for_im, null);

        mIMPullToRefreshListView = (IMPullToRefreshListView) mainView.findViewById(R.id.pull_list_view_im);
        mIMPullToRefreshListView.setMode(IMPullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH);
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        mListView = mIMPullToRefreshListView.getRefreshableView();

        mHeaderViewContainer = new RelativeLayout(mContext);
        mHeaderViewContainer.setVisibility(View.GONE);
        mListView.addHeaderView(mHeaderViewContainer);
    }

    private void registerListeners() {

        IMPullToRefreshBase.OnRefreshListener onRefreshListener = new IMPullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHeaderViewContainer.setVisibility(View.VISIBLE);
                mLoadForViewManager.loadNextPage(true);
                // 显示HeaderView，用于占位
                mIMPullToRefreshListView.getHeaderView().setVisibility(View.VISIBLE);
            }
        };

        mIMPullToRefreshListView.setOnRefreshListener(onRefreshListener);

        AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int a=mListView.getHeaderViewsCount();
                int tempPos = position -a;
                if (tempPos > mDealSwipeListAdapter.getList().size() || tempPos < 0) {
                    return;
                }
                Object oo = mDealSwipeListAdapter.getList().get(tempPos);
                if (oo != null) {
                    AdapterViewOnItemClickListener mAdapterViewOnItemClickListener = (AdapterViewOnItemClickListener) oo;
                    try {
                        mAdapterViewOnItemClickListener.OnItemClickListener(mContext, view, tempPos, mDealSwipeListAdapter.getList(), mLoadForViewManager.getmLoadSetting());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }catch (FaceException e) {
                        e.printStackTrace();
                    }
                }

                // TODO 点击事件
            }
        };

        mListView.setOnItemClickListener(mOnItemClickListener);

        AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
            private boolean isBottom;
            private int mFirstItem;
            private int mVisibleItemCount;
            private boolean isLoadPa;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                isBottom = mFirstItem <= firstVisibleItem;
                mFirstItem = firstVisibleItem;
                mVisibleItemCount = visibleItemCount;
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
                        if (mLoadForViewManager.isMustPreLoadItem()||mLoadForViewManager.isMustAnalysisItem()) {
                            mLoadForViewManager.onScrollShow(mFirstItem, mFirstItem + mVisibleItemCount, isBottom,mVisibleItemCount);
                        }
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
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(imm.isActive()&&editText!=null){
                    mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    // imm.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                return false;
            }
        });
        mListView.setOnScrollListener(mOnScrollListener);
    }

    private boolean isMustNeedEndTag = true;

    @Override
    public void loading(boolean isHasMoreToLoad, boolean isMustNeedEndTag) {
        this.isMustNeedEndTag = isHasMoreToLoad;
//        if (!isHasMoreToLoad) {
//            if (isMustNeedEndTag) {
//                if (mDealSwipeListAdapter != null && mDealSwipeListAdapter.getCount() > 0) {
//                    showSpecialFooterView();
//                } else {
//                    hideSpecialFootView();
//                }
//            } else {
//                hideSpecialFootView();
//                Tao800Util.showShortToast(mContext, R.string.pull_to_refresh_nodata);
//            }
//            return;
//        }
//        if (mDealSwipeListAdapter != null && mDealSwipeListAdapter.getCount() > 0) {
//            showFooterView();
//        } else {
//            hideFooterView();
//        }
        // TODO 这里还需要优化
    }

    private int size = 0;
    @Override
    public void loaded(Long loadTime, int thisPageNumber, boolean isFromZero, boolean isHasMoreToLoad, List data) {
        refreshComplete();
       /* if(isFromZero&data.size()<4){
            mContext.getWindow().setSoftInputMode(EditorInfo.IME_ACTION_DONE);
            mContext.getWindow().setSoftInputMode(  WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }else{
            mContext.getWindow().setSoftInputMode(EditorInfo.IME_ACTION_DONE);
            mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }*/

        checkIfHasMoreToLoad(isHasMoreToLoad, data);
        TextView textView =new TextView(mContext);
        if(!isHasMoreToLoad){

            textView.setText("无更多历史消息");
            textView.setPadding(0, 10, 0, 0);
            textView.setTextColor(mContext.getResources().getColor(R.color.light_gray));
            RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            textView.setLayoutParams(layoutParams);
            mHeaderViewContainer.addView(textView);

        }
        else {
            textView.setVisibility(View.GONE);
        }

        //  if (thisPageNumber != 1 ) {// 不是第一页
        mListView.setSelectionFromTop(data.size() + mListView.getHeaderViewsCount(), mIMPullToRefreshListView.getHeaderHeight());
        // mIMPullToRefreshListView.getHeaderView().setVisibility(View.GONE);
        //}
    }

    @Override
    public void loadErr(int errKey, int errTimes, boolean isReload, Exception... err) {
        refreshComplete();

        // mListView.setSelectionFromTop(5+2, mIMPullToRefreshListView.getHeaderHeight());
        // mIMPullToRefreshListView.getHeaderView().setVisibility(View.GONE);
    }

    @Override
    public void backTopView() {
//        mListView.setSelection(0);
        mListView.smoothScrollToPosition(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setSelection(0);
            }
        }, TabClickObservable.SCROLL_TIME);
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public void setLast(int i) {

      //  mListView.smoothScrollToPosition(3);
        int j=mListView.getHeaderViewsCount();
       // mListView.smoothScrollToPosition(i+mListView.getHeaderViewsCount()-1);
    }

    @Override
    public void setBackToTopAndRefresh() {

    }

    @Override
    public void setSelectIndex(int index) {
        mListView.setSelection(index);
    }

    @Override
    public void setSelectIndexByScroll(int index) {

    }

    @Override
    public void setPullToRefreshMode(int mode) {
        mIMPullToRefreshListView.setMode(mode);
    }

    private void refreshComplete() {

        mIMPullToRefreshListView.onRefreshComplete();
    }

    public void checkIfHasMoreToLoad(boolean hasMore, List data) {
//        if (hasMore) {
//            // mIMPullToRefreshListView.setMode(IMPullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH);
//            mIMPullToRefreshListView.hideSpecialHeader();
//        } else {
//            if (data != null && data.size() > 0) {
//                // mIMPullToRefreshListView.setMode(IMPullToRefreshBase.MODE_DISABLE);
//                if (isMustNeedEndTag) {
//                    mIMPullToRefreshListView.showSpecialHeader();
//                } else {
//                    mIMPullToRefreshListView.hideSpecialHeader();
//                }
//            }
//        }


    }

    //获取显示列表数据的listView
    public ListView getDataListView(){
        return mListView;
    }




}
