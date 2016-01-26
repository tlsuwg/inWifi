package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content;

import android.annotation.TargetApi;
import android.hxuehh.com.R;
import android.os.Build;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.ScreenUtil;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.AdapterViewOnItemClickListener;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.viewInterfaces.LoadView;
import com.hxuehh.appCore.faceFramework.faceUI.viewAdapter.FaceListAdapter;


import java.util.List;
/**
 * Created by yubaojian on 15-4-28.
 */
public class LoadHeaderListView extends LoadView {
    @Override
    public int getColumnsNum() {
        return 1;
    }

    @Override
    public void callReportScrollStateChange() {

    }

    private static final String TAG = LoadListView.class.getSimpleName();

    private ListView mListView;                             //列表控件

    private FaceListAdapter mDealSwipeListAdapter;
    private FaceBaseActivity_1 mContext;
    private  LoadCursorSetting mLoadCursorSetting;
    List mData;

    public LoadHeaderListView(FaceBaseActivity_1 context, LoadCursorSetting loadCursorSetting, List data) {

        this.mContext = context;
        this.mLoadCursorSetting=loadCursorSetting;
        this.mDealSwipeListAdapter = new FaceListAdapter(new FaceContextWrapImp(context),mLoadCursorSetting);
        initViews();
        registerListeners();
        mData= data;
        mDealSwipeListAdapter.setList(mData);
       // ScreenUtil.setListViewHeightBasedOnChildren(mListView);
    }


    private void initViews() {
        mainView = LayoutInflater.from(mContext).inflate(R.layout.layer_base_listview_for_header, null);
        mListView = (ListView) mainView.findViewById(R.id.header_listview);
    }

    private void registerListeners() {

        AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int a = mListView.getHeaderViewsCount();
                int tempPos = position - a;
                if (tempPos > mDealSwipeListAdapter.getList().size() || tempPos < 0) {
                    return;
                }
                Object oo = mDealSwipeListAdapter.getList().get(tempPos);
                if (oo != null) {
                    AdapterViewOnItemClickListener mAdapterViewOnItemClickListener = (AdapterViewOnItemClickListener) oo;
                    try {
                        mAdapterViewOnItemClickListener.OnItemClickListener(mContext, view, tempPos, mDealSwipeListAdapter.getList(),mLoadCursorSetting);
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

    }

    private boolean isMustNeedEndTag = true;

    @Override
    public void loading(boolean isHasMoreToLoad, boolean isMustNeedEndTag) {
        this.isMustNeedEndTag = isHasMoreToLoad;

    }

    private int size = 0;

    @Override
    public void loaded(Long loadTime, int thisPageNumber, boolean isFromZero, boolean isHasMoreToLoad, List data) {
        if(data!=null){
            mData= data;
            mDealSwipeListAdapter.setList(mData);
            ScreenUtil.setListViewHeightBasedOnChildren(mListView);
           // mDealSwipeListAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void loadErr(int errKey, int errTimes, boolean isReload, Exception... err) {

        // mListView.setSelectionFromTop(5+2, mIMPullToRefreshListView.getHeaderHeight());
        // mIMPullToRefreshListView.getHeaderView().setVisibility(View.GONE);
    }

    @Override
    public void backTopView() {

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @Override
    public void setLast(int i) {

        //  mListView.smoothScrollToPosition(3);
        mListView.smoothScrollToPosition(i + mListView.getHeaderViewsCount() - 1);
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

    }
    //获取显示列表数据的listView
    public ListView getDataListView() {
        return mListView;
    }

     public void setAdapter(){
         mListView.setAdapter(mDealSwipeListAdapter);
     }

    public void notifyDataChanged() {
        ScreenUtil.setListViewHeightBasedOnChildren(mListView);
      //  mDealSwipeListAdapter.notifyDataSetChanged();

    }

    public void clearAllDateNotifi() {
        if( mData!=null){
            mData.clear();
        }
        ScreenUtil.setListViewHeightBasedOnChildren(mListView);
      //  mDealSwipeListAdapter.notifyDataSetChanged();

    }



}