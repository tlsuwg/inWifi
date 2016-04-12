/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.headered;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.hxuehh.com.R;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;



import java.util.ArrayList;

//存在head 的GridView
public class HeaderGridView extends GridView {
    private static final String TAG = HeaderGridView.class.getSimpleName();
    private int mColumnsNum;
    private float mFirstX;
    private float mFirstY;
    private float mFirstDownY;
    private OnMoveTouchListener mOnMoveTouchListener;
    private OnMoveTouchListener mOnMoveTouchListenerDistance;
    private static Context mContext;
    /**
     * A class that represents a fixed view in a list, for example a header at the top
     * or a footer at the bottom.
     */
    private static class FixedViewInfo {
        /**
         * The view to add to the grid
         */
        public View view;
        public ViewGroup viewContainer;
        /**
         * The data backing the view. This is returned from {@link ListAdapter#getItem(int)}.
         */
        public Object data;
        /**
         * <code>true</code> if the fixed view should be selectable in the grid
         */
        public boolean isSelectable;
    }
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<FixedViewInfo>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<FixedViewInfo>();

    private void initHeaderGridView() {

        // super.setClipChildren(false);
    }

    public HeaderGridView(Context context) {
        super(context);
        mContext=context;
        initHeaderGridView();
    }




    @Override
    public void setNumColumns(int numColumns) {
        this.mColumnsNum = numColumns;
        super.setNumColumns(numColumns);
    }

    public int getColumnsNum() {
        return mColumnsNum;
    }

    public HeaderGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        initHeaderGridView();
    }

    public HeaderGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext=context;
        initHeaderGridView();
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ListAdapter adapter = getAdapter();
        if (adapter != null && adapter instanceof HeaderViewGridAdapter) {

            ((HeaderViewGridAdapter) adapter).setNumColumns(mColumnsNum);
        }
    }

    @Override
    public void setClipChildren(boolean clipChildren) {
        super.setClipChildren(clipChildren);
        // Ignore, since the header rows depend on not being clipped
    }

    /**
     * Add a fixed view to appear at the top of the grid. If addHeaderView is
     * called more than once, the views will appear in the order they were
     * added. Views added using this call can take focus if they want.
     * <p/>
     * NOTE: Call this before calling setAdapter. This is so HeaderGridView can wrap
     * the supplied cursor with one that will also account for header views.
     *
     * @param v            The view to add.
     * @param data         Data to associate with this view
     * @param isSelectable whether the item is selectable
     */
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        ListAdapter adapter = getAdapter();

        if (adapter != null && !(adapter instanceof HeaderViewGridAdapter)) {
            throw new IllegalStateException(
                    "Cannot add header view to grid -- setAdapter has already been called.");
        }

        FixedViewInfo info = new FixedViewInfo();
        FrameLayout fl = new FullWidthFixedViewLayout(getContext());
        fl.addView(v);
        info.view = v;
        info.viewContainer = fl;
        info.data = data;
        info.isSelectable = isSelectable;
        mHeaderViewInfos.add(info);

        // in the case of re-adding a header view, or adding one later on,
        // we need to notify the observer
        if (adapter != null) {
            ((HeaderViewGridAdapter) adapter).notifyDataSetChanged();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstDownY = ev.getRawY();
                mFirstX = lastX;
                mFirstY = lastY;
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                float currentY = ev.getRawY();

                if (mOnMoveTouchListener != null) {
                    if (currentY - mFirstDownY > 0) {
                        mOnMoveTouchListener.onMoveDown();
                    } else {
                        mOnMoveTouchListener.onMoveUp();
                    }

                }
                if(mOnMoveTouchListenerDistance!=null){
                    if (dy > 50) {
                        mOnMoveTouchListenerDistance.onMoveDown();
                    } else if(dy  <- 50) {
                        mOnMoveTouchListenerDistance.onMoveUp();
                    }
                }


            case MotionEvent.ACTION_CANCEL:
                break;


        }
        return super.onTouchEvent(ev);
    }


    /**
     * Add a fixed view to appear at the top of the grid. If addHeaderView is
     * called more than once, the views will appear in the order they were
     * added. Views added using this call can take focus if they want.
     * <p/>
     * NOTE: Call this before calling setAdapter. This is so HeaderGridView can wrap
     * the supplied cursor with one that will also account for header views.
     *
     * @param v The view to add.
     */
    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }

    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }

    /**
     * Removes a previously-added header view.
     *
     * @param v The view to remove
     * @return true if the view was removed, false if the view was not a header
     * view
     */
    public boolean removeHeaderView(View v) {
        if (mHeaderViewInfos.size() > 0) {
            boolean result = false;
            ListAdapter adapter = getAdapter();
            if (adapter != null && ((HeaderViewGridAdapter) adapter).removeHeader(v)) {
                result = true;
            }
            removeFixedViewInfo(v, mHeaderViewInfos);
            return result;
        }
        return false;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            FixedViewInfo info = where.get(i);
            if (info.view == v) {
                where.remove(i);
                break;
            }
        }
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        ListAdapter adapter = getAdapter();
        boolean rus = adapter instanceof HeaderViewGridAdapter;
        if (adapter != null && !(adapter instanceof HeaderViewGridAdapter)) {
            throw new IllegalStateException(
                    "Cannot add header view to grid -- setAdapter has already been called.");
        }

        FixedViewInfo info = new FixedViewInfo();
        FrameLayout fl = new FullWidthFixedViewLayout(getContext());
        fl.addView(v);
        info.view = v;
        info.viewContainer = fl;
        info.data = data;
        info.isSelectable = isSelectable;
        mFooterViewInfos.add(info);

        // in the case of re-adding a header view, or adding one later on,
        // we need to notify the observer
        if (adapter != null) {
            ((HeaderViewGridAdapter) adapter).notifyDataSetChanged();
        }
    }

    public void addFooterView(View v) {
        addFooterView(v, null, true);
    }

    public int getFooterViewsCount() {
        return mFooterViewInfos.size();
    }

    public boolean removeFooterView(View v) {
        if (mFooterViewInfos.size() > 0) {

            boolean result = false;
            ListAdapter adapter = getAdapter();
            if (adapter != null && ((HeaderViewGridAdapter) adapter).removeFooter(v)) {
                result = true;
            }
            removeFixedViewInfo(v, mFooterViewInfos);
            return result;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
            HeaderViewGridAdapter hadapter = new HeaderViewGridAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
            int numColumns = mColumnsNum;
            if (numColumns > 1) {
                hadapter.setNumColumns(numColumns);
            }
            super.setAdapter(hadapter);
        } else {
            super.setAdapter(adapter);
        }
    }

    private class FullWidthFixedViewLayout extends FrameLayout {
        public FullWidthFixedViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int targetWidth = HeaderGridView.this.getMeasuredWidth()
                    - HeaderGridView.this.getPaddingLeft()
                    - HeaderGridView.this.getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * ListAdapter used when a HeaderGridView has header views. This ListAdapter
     * wraps another one and also keeps track of the header views and their
     * associated data objects.
     * <p>This is intended as a base class; you will probably not need to
     * use this class directly in your own code.
     */
    private static class HeaderViewGridAdapter implements WrapperListAdapter, Filterable {

        // This is used to notify the container of updates relating to number of columns
        // or headers changing, which changes the number of placeholders needed
        private final DataSetObservable mDataSetObservable = new DataSetObservable();

        private final ListAdapter mAdapter;
        private int mNumColumns = 1;
        private int height;

        // This ArrayList is assumed to NOT be null.
        ArrayList<FixedViewInfo> mHeaderViewInfos;
        ArrayList<FixedViewInfo> mFooterViewInfos;
        //ArrayList<FixedViewInfo> allViewInfos;
        boolean mAreAllFixedViewsSelectable;

        private final boolean mIsFilterable;

        public HeaderViewGridAdapter(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
            mAdapter = adapter;
            mIsFilterable = adapter instanceof Filterable;

            if (headerViewInfos == null && footerViewInfos == null) {
                throw new IllegalArgumentException("headerViewInfos or footerViewInfos cannot be both null");
            }

            mHeaderViewInfos = headerViewInfos;
            mFooterViewInfos = footerViewInfos;
            boolean head = areAllListInfosSelectable(mHeaderViewInfos);
            boolean foot = areAllListInfosSelectable(mFooterViewInfos);
            mAreAllFixedViewsSelectable = head && foot;
        }

        public int getHeadersCount() {
            return mHeaderViewInfos.size();
        }

        public int getFootersCount() {
            return mFooterViewInfos.size();
        }

        @Override
        public boolean isEmpty() {
            return (mAdapter == null || mAdapter.isEmpty()) && getHeadersCount() == 0 && getFootersCount() == 0;
        }

        public void setNumColumns(int numColumns) {
            if (numColumns < 1) {
                throw new IllegalArgumentException("Number of columns must be 1 or more");
            }
            if (mNumColumns != numColumns) {
                mNumColumns = numColumns;
                notifyDataSetChanged();
            }
        }

        private boolean areAllListInfosSelectable(ArrayList<FixedViewInfo> infos) {
            if (infos != null) {
                for (FixedViewInfo info : infos) {
                    if (!info.isSelectable) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean removeHeader(View v) {
            for (int i = 0; i < mHeaderViewInfos.size(); i++) {
                FixedViewInfo info = mHeaderViewInfos.get(i);
                if (info.view == v) {
                    mHeaderViewInfos.remove(i);
                    boolean head = areAllListInfosSelectable(mHeaderViewInfos);
                    boolean foot = areAllListInfosSelectable(mFooterViewInfos);
                    mAreAllFixedViewsSelectable = head && foot;
                    mDataSetObservable.notifyChanged();
                    return true;
                }
            }

            return false;
        }

        public boolean removeFooter(View v) {
            for (int i = 0; i < mFooterViewInfos.size(); i++) {
                FixedViewInfo info = mFooterViewInfos.get(i);
                if (info.view == v) {
                    mFooterViewInfos.remove(i);


                    boolean head = areAllListInfosSelectable(mHeaderViewInfos);
                    boolean foot = areAllListInfosSelectable(mFooterViewInfos);
                    mAreAllFixedViewsSelectable = head && foot;

                    mDataSetObservable.notifyChanged();
                    return true;
                }
            }

            return false;
        }

        @Override
        public int getCount() {
            if (mAdapter != null) {
                if (mAdapter.getCount() % mNumColumns != 0) {
                    return getHeadersCount() * mNumColumns + getFootersCount() * mNumColumns + mAdapter.getCount() + mNumColumns-mAdapter.getCount() % mNumColumns;
                }
                // int a = getHeadersCount() * mNumColumns + getFootersCount() * mNumColumns + mAdapter.getCount();
                else {
                    return getHeadersCount() * mNumColumns + getFootersCount() * mNumColumns + mAdapter.getCount();
                }

            } else {
                return getHeadersCount() * mNumColumns + getFootersCount() * mNumColumns;
            }
        }

        @Override
        public boolean areAllItemsEnabled() {
            if (mAdapter != null) {
                return mAreAllFixedViewsSelectable && mAdapter.areAllItemsEnabled();
            } else {
                return true;
            }
        }

        @Override
        public boolean isEnabled(int position) {
            //header
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            if (position < numHeadersAndPlaceholders) {
                return (position % mNumColumns == 0)
                        && mHeaderViewInfos.get(position / mNumColumns).isSelectable;
            }


            //footer
            if (mAdapter.getCount() % mNumColumns != 0) {
                if (position >= numHeadersAndPlaceholders + mAdapter.getCount()+mNumColumns-mAdapter.getCount() % mNumColumns) {
                    int footid = position - mAdapter.getCount() - numHeadersAndPlaceholders-mNumColumns+mAdapter.getCount() % mNumColumns;
                    return (footid % mNumColumns == 0)
                            && mFooterViewInfos.get(footid / mNumColumns).isSelectable;
                }
            } else {
                if (position >= numHeadersAndPlaceholders + mAdapter.getCount()) {
                    int footid = position - mAdapter.getCount() - numHeadersAndPlaceholders;
                    return (footid % mNumColumns == 0)
                            && mFooterViewInfos.get(footid / mNumColumns).isSelectable;
                }

            }

            // Adapter
            final int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (mAdapter != null) {
                adapterCount = mAdapter.getCount();
                if (adjPosition <adapterCount) {
                    return mAdapter.isEnabled(adjPosition);
                } else {
                    return false;
                }
            }

            throw new ArrayIndexOutOfBoundsException(position);
        }

        @Override
        public Object getItem(int position) {
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            //header
            if (position < numHeadersAndPlaceholders) {
                if (position % mNumColumns == 0) {
                    return mHeaderViewInfos.get(position / mNumColumns).data;
                }
                return null;
            }
            //footer
            if ( mAdapter.getCount() % mNumColumns != 0) {
                if (position >= numHeadersAndPlaceholders + mAdapter.getCount()+mNumColumns-mNumColumns+mAdapter.getCount() % mNumColumns) {
                    int footerposition = position - numHeadersAndPlaceholders - mAdapter.getCount() - mNumColumns+mAdapter.getCount() % mNumColumns;
                    if (footerposition % mNumColumns == 0) {
                        return mFooterViewInfos.get(footerposition / mNumColumns).data;
                    }
                    return null;
                }
            } else {
                if (position >= numHeadersAndPlaceholders + mAdapter.getCount()) {
                    int footerposition = position - numHeadersAndPlaceholders - mAdapter.getCount();
                    if (footerposition % mNumColumns == 0) {
                        return mFooterViewInfos.get(footerposition / mNumColumns).data;
                    }
                    return null;
                }
            }

            // Adapter
            final int adjPosition = position - numHeadersAndPlaceholders;
            int adapterCount = 0;
            if (mAdapter != null) {
                adapterCount = mAdapter.getCount();
                if (adjPosition <adapterCount) {
                    return mAdapter.getItem(adjPosition);
                } else if (mAdapter.getCount() % mNumColumns != 0 ) {
                    return null;
                }
            }

            throw new ArrayIndexOutOfBoundsException(position);
        }

        @Override
        public long getItemId(int position) {
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            if (mAdapter != null && position >= numHeadersAndPlaceholders) {
                int adjPosition = position - numHeadersAndPlaceholders;
                int adapterCount = mAdapter.getCount();
                if (adjPosition <adapterCount) {
                    return mAdapter.getItemId(adjPosition);
                }
                else if ( mAdapter.getCount() % mNumColumns!= 0) {
                    return mAdapter.getItemId(adjPosition)+adjPosition-adapterCount+1;
                }
            }
            return -1;
        }

        @Override
        public boolean hasStableIds() {
            if (mAdapter != null) {
                return mAdapter.hasStableIds();
            }
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            if (position < numHeadersAndPlaceholders) {
                View headerViewContainer = mHeaderViewInfos
                        .get(position / mNumColumns).viewContainer;
                if (position % mNumColumns == 0) {
                    return headerViewContainer;
                } else {
                    if (convertView == null) {
                        convertView = new View(parent.getContext());
                    }
                    // We need to do this because GridView uses the height of the last item
                    // in a row to determine the height for the entire row.
                    if (mNumColumns != 1) {
                        convertView.setVisibility(View.INVISIBLE);
                    }

                    convertView.setMinimumHeight(headerViewContainer.getHeight());
                    return convertView;
                }
            }

           //footer
            int number = 0;
            int footid = 0;
            if (mAdapter.getCount() % mNumColumns != 0) {
                number = numHeadersAndPlaceholders + mAdapter.getCount() +mNumColumns-mAdapter.getCount() % mNumColumns ;
                footid = position - mAdapter.getCount() - numHeadersAndPlaceholders -mNumColumns+mAdapter.getCount() % mNumColumns;
            } else {
                number = numHeadersAndPlaceholders + mAdapter.getCount();
                footid = position - mAdapter.getCount() - numHeadersAndPlaceholders;
            }
            if (position >= number) {

                View footerViewContainer = mFooterViewInfos.get(footid / mNumColumns).viewContainer;

                if (footid % mNumColumns == 0) {
                    return footerViewContainer;
                } else {
                    //this.invalidate();
                    if (convertView == null) {
                        convertView = new View(parent.getContext());
                    }
                    convertView.requestLayout();
                    // We need to do this because GridView uses the height of the last item
                    // in a row to determine the height for the entire row.
                    if (mNumColumns != 1) {
                        convertView.setVisibility(View.INVISIBLE);
                    }
                    if (footerViewContainer.getMeasuredHeight() != 0) {
                        convertView.setMinimumHeight(footerViewContainer.getHeight());
                    } else {
                        //  footerViewContainer.measure(0,0);
                        convertView.setMinimumHeight(footerViewContainer.getHeight());
                    }

                    // convertView.setMinimumHeight(500);
                    return convertView;
                }
            }


            // Adapter
            final int adjPosition = position - numHeadersAndPlaceholders;

            int adapterCount = 0;
            if (mAdapter != null) {

                if (mAdapter.getCount() % mNumColumns != 0) {
                    adapterCount = mAdapter.getCount();
                    if (adjPosition <adapterCount) {
                        convertView = mAdapter.getView(adjPosition, convertView, parent);
                        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.lottery_gray));
                        if(adjPosition ==0){
                            height=convertView.getHeight();
                        }
                        convertView.setVisibility(View.VISIBLE);
                        if(convertView.findViewById(R.id.tv_mask)!=null){
                            convertView.findViewById(R.id.tv_mask).setVisibility(View.GONE);
                        }
                        if(convertView.findViewById(R.id.iv_baoyou)!=null){
                            convertView.findViewById(R.id.iv_baoyou).setVisibility(View.VISIBLE);
                        }
                        return convertView;
                    } else  {
                       // TextView tv=new TextView(parent.getFaceContext());
                       // tv.setBackgroundResource(R.drawable.muyingcategory);
                        convertView = mAdapter.getView(adapterCount - 1, convertView, parent);
                        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.lottery_gray));
                        convertView.setVisibility(View.VISIBLE);
                        convertView.setBackgroundColor(mContext.getResources().getColor(R.color.lottery_gray));
                        if(convertView.findViewById(R.id.tv_mask)!=null){
                            convertView.findViewById(R.id.tv_mask).setVisibility(View.VISIBLE);
                        }
                        if(convertView.findViewById(R.id.iv_baoyou)!=null){
                            convertView.findViewById(R.id.iv_baoyou).setVisibility(View.GONE);
                        }

                       /* GoodsBigDataItemLayout mDataLayout = (GoodsBigDataItemLayout) convertView.findViewById(R.id.view_left);
                        mDataLayout.setBackGroundResources();
*/
                        convertView.setClickable(false);
                       // tv.setHeight(height);
                        return convertView;
                    }
                } else {
                    adapterCount = mAdapter.getCount();
                    if (adjPosition <adapterCount) {
                        convertView = mAdapter.getView(adjPosition, convertView, parent);
                        if(adjPosition ==0){
                            height=convertView.getHeight();
                        }
                        if(convertView!=null){
                            convertView.setVisibility(View.VISIBLE);
                        }
                        if(convertView.findViewById(R.id.tv_mask)!=null){
                            convertView.findViewById(R.id.tv_mask).setVisibility(View.GONE);
                        }
                        if(convertView.findViewById(R.id.iv_baoyou)!=null){
                            convertView.findViewById(R.id.iv_baoyou).setVisibility(View.VISIBLE);
                        }
                        return convertView;
                    }
                }
            }
            throw new ArrayIndexOutOfBoundsException(position);
        }

        @Override
        public int getItemViewType(int position) {
            int numHeadersAndPlaceholders = getHeadersCount() * mNumColumns;
            int big = 0;
            if ( mAdapter.getCount()>=mNumColumns&&mAdapter.getCount() % mNumColumns != 0) {
                big = numHeadersAndPlaceholders + mAdapter.getCount() +mNumColumns-mAdapter.getCount() % mNumColumns;
            } else {
                big = numHeadersAndPlaceholders + mAdapter.getCount();
            }

            if (mAdapter != null && numHeadersAndPlaceholders <= position && position < big) {
                int adjPosition = position - numHeadersAndPlaceholders;
                int adapterCount = mAdapter.getCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemViewType(adjPosition);
                } else {
                    return mAdapter.getItemViewType(adjPosition-1);
                }
            }
            return AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
        }

        @Override
        public int getViewTypeCount() {

            if (mAdapter != null) {
                return mAdapter.getViewTypeCount() + 1;
            }
            return 1;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.registerObserver(observer);
            if (mAdapter != null) {
                mAdapter.registerDataSetObserver(observer);
            }
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.unregisterObserver(observer);
            if (mAdapter != null) {
                mAdapter.unregisterDataSetObserver(observer);
            }
        }

        @Override
        public Filter getFilter() {
            if (mIsFilterable) {
                return ((Filterable) mAdapter).getFilter();
            }
            return null;
        }

        @Override
        public ListAdapter getWrappedAdapter() {
            return mAdapter;
        }

        public void notifyDataSetChanged() {
            mDataSetObservable.notifyChanged();
        }
    }

    public void setOnMoveTouchListener(OnMoveTouchListener listener) {
        this.mOnMoveTouchListener = listener;
    }

    public void setOnMoveTouchListenerDistance(OnMoveTouchListener listener) {
        this.mOnMoveTouchListenerDistance = listener;
    }

    public interface OnMoveTouchListener {
        public void onMoveUp();
        public void onMoveDown();
    }

}