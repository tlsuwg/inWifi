package com.hxuehh.appCore.faceFramework.faceUI.viewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.InViewGroupWithAdaper;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kait
 * Date: 12-10-24
 * Time: 上午11:56
 * To change this template use File | Settings | File Templates.
 */


public class FaceListAdapter<ViewItemObj extends InViewGroupWithAdaper> extends BaseAdapter {

    protected List<ViewItemObj> mList;
    protected FaceContextWrapImp mContext;
    private AdapterView mListView;
    protected LayoutInflater mInflater;
    LoadCursorSetting mLoadCursorSetting;

    protected int viewItemKey = ViewKeys.ItemListView;//获取不同的视图

    public void setNewArrive(boolean newArrive) {

    }

    public void setItemViewKey(int viewKey) {
        this.viewItemKey = viewKey;
    }

    public FaceListAdapter(FaceContextWrapImp context, LoadCursorSetting mLoadCursorSetting) {
        this.mContext = context;
        this.mLoadCursorSetting = mLoadCursorSetting;
        mInflater = LayoutInflater.from(context.getFaceContext());
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList == null ? 0 : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        InViewGroupWithAdaper mInViewGroupWithAdaper = mList.get(i);
        try {
            if (mInViewGroupWithAdaper != null) {
                View view111 = mInViewGroupWithAdaper.getView((FaceBaseActivity_1)mContext.getFaceContext(), i, view, viewGroup, mInflater, viewItemKey);
                if (view111 == null)
                    throw new RuntimeException("201502061953" + mInViewGroupWithAdaper);
                return view111;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Su.logPullView(e.getMessage());
            return null;
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        InViewGroupWithAdaper mInViewGroupWithAdaper = mList.get(position);
        if (mInViewGroupWithAdaper != null) {
            return mInViewGroupWithAdaper.getItemViewType(position);
        }
        return super.getItemViewType(position);
    }


    @Override
    public int getViewTypeCount() {
        if (mLoadCursorSetting.getObj() != null) {
            InViewGroupWithAdaper mInViewGroupWithAdaper = mLoadCursorSetting.getObj();
            if (mLoadCursorSetting.MustHybridizationLoadType ==
                    LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_OneDomain//这个是推荐的
                    ||
                    mLoadCursorSetting.MustHybridizationLoadType ==
                            LoadCursorSetting.MustHybridizationLoadType_ONLYONEDATAINTERFACE_CHANGEPARMS_TwoDomains//其实这个是不对的，因为现在使用的是head方式，只是一种
                    ) {
                if (ViewKeys.ItemListView == viewItemKey) {
                    return mInViewGroupWithAdaper.getViewTypeCount() + 1;
                } else if (ViewKeys.ItemGridView == viewItemKey) {
                    return mInViewGroupWithAdaper.getViewTypeCount() + 2;
                }
            } else {
                return mInViewGroupWithAdaper.getViewTypeCount();
            }
        }

        return super.getViewTypeCount();
    }


    public void setList(List<ViewItemObj> list) {
        this.mList = list;
    }

    public List<ViewItemObj> getList() {
        return this.mList;
    }

    public AdapterView getListView() {
        return mListView;
    }

    public void setListView(AdapterView listView) {
        mListView = listView;
    }

    public Context getContext() {
        return mContext.getFaceContext();
    }


    public void clear() {
        if (mList != null && mList.size() > 0) {
            mList.clear();
        }
    }

}
