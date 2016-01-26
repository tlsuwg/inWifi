package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.stickygridheaders;

import android.content.Context;
import android.content.res.TypedArray;
import android.hxuehh.com.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.EmptyViewMethodAccessor;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.headered.PullToRefreshAdapterViewBase_stidygridview;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullLoadingLayoutfooter;


/**
 * Created by yubaojian on 14-8-28.
 */
public class PullToRefreshStidiyGridView2 extends PullToRefreshAdapterViewBase_stidygridview<StickyGridHeadersGridView> {
    private PullLoadingLayoutfooter mHeaderLoadingView;
    private PullLoadingLayoutfooter mFooterLoadingView;
    private FrameLayout mLvFooterLoadingFrame;
    private boolean mAddedLvFooter = false;

    public PullToRefreshStidiyGridView2(Context context) {
        super(context);
        setDisableScrollingWhileRefreshing(false);
    }

    public PullToRefreshStidiyGridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDisableScrollingWhileRefreshing(false);
    }

    @Override
    protected StickyGridHeadersGridView createRefreshableView(Context context, AttributeSet attrs) {
        StickyGridHeadersGridView lv = new InternalHeaderGridView(context, attrs);
        final int mode = getMode();

        boolean isShowHeader;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.pullshowheader);
        isShowHeader = typedArray.getBoolean(R.styleable.pullshowheader_showheader, true);

        String pullLabel = context.getString(R.string.pull_to_refresh);
        String refreshingLabel = context.getString(R.string.label_loading);
        String releaseLabel = context.getString(R.string.pull_to_refresh_release);
        String upLabel = context.getString(R.string.up_to_refresh);

        if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
            FrameLayout frame = new FrameLayout(context);
            mHeaderLoadingView = new PullLoadingLayoutfooter(context, MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel, isShowHeader);
            frame.addView(mHeaderLoadingView, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            mHeaderLoadingView.setVisibility(View.GONE);
          //  lv.addHeaderView(frame, null, false);
        }
        if (mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH) {
            mLvFooterLoadingFrame = new FrameLayout(context);
            mFooterLoadingView = new PullLoadingLayoutfooter(context, MODE_PULL_UP_TO_REFRESH, releaseLabel, upLabel, refreshingLabel, isShowHeader);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            mFooterLoadingView.setVisibility(View.GONE);


        }

        lv.setId(android.R.id.list);
        return lv;
    }

    private class InternalHeaderGridView extends StickyGridHeadersGridView implements EmptyViewMethodAccessor {
        public InternalHeaderGridView(Context context) {
            super(context);
        }

        public InternalHeaderGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        /*@Override
        public void draw(Canvas canvas) {
            */

        /**
         * This is a bit hacky, but ListView has got a bug in it when using
         * Header/Footer Views and the list is empty. This masks the issue
         * so that it doesn't cause an FC. See Issue #66.
         *//*
            try {
                super.draw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    /*    @Override
        public void setAdapter(ListAdapter adapter) {
            // Add the Footer View at the last possible moment
            if (!mAddedLvFooter && null != mLvFooterLoadingFrame) {
                addFooterView(mLvFooterLoadingFrame, null, false);
                mAddedLvFooter = true;
            }

            super.setAdapter(adapter);
        }
*/
        @Override
        public void setEmptyView(View emptyView) {
            InternalHeaderGridView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }
    }

    @Override
    protected void resetHeader() {
        // If we're not showing the Refreshing view, or the list is empty, then
        // the header/footer views won't show so we use the
        // normal method

        ListAdapter adapter = getRefreshableView().getAdapter();
        if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
            super.resetHeader();
            return;
        }

        PullLoadingLayoutfooter originalLoadingLayout;
        PullLoadingLayoutfooter listViewLoadingLayout = null;
        PullLoadingLayoutfooter listViewLoadingLayout2= null;
        int scrollToHeight = getHeaderHeight();
        int selection;
        boolean scroll;

        switch (getCurrentMode()) {
            case MODE_PULL_UP_TO_REFRESH:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = getRefreshableView().getCount() - 1;
                scroll = getRefreshableView().getLastVisiblePosition() == selection;
                break;
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout2 = mHeaderLoadingView;
                scrollToHeight *= -1;
                selection = 0;
                scroll = getRefreshableView().getFirstVisiblePosition() == selection;
                break;
        }

        // Set our Original View to Visible
        originalLoadingLayout.setVisibility(View.VISIBLE);

        /**
         * Scroll so the View is at the same Y as the ListView header/footer,
         * but only scroll if we've pulled to refresh and it's positioned
         * correctly
         */
        if (scroll && getState() != 0x3) {
            getRefreshableView().setSelection(selection);
            setHeaderScroll(scrollToHeight);
        }

        // Hide the ListView Header/Footer
        if(getCurrentMode()== MODE_PULL_UP_TO_REFRESH){
            listViewLoadingLayout.setVisibility(View.GONE);
        }else {
            listViewLoadingLayout2.setVisibility(View.GONE);
        }


        super.resetHeader();
    }

    @Override
    protected void setRefreshingInternal(boolean doScroll) {

        // If we're not showing the Refreshing view, or the list is empty, then
        // the header/footer views won't show so we use the
        // normal method
        ListAdapter adapter = getRefreshableView().getAdapter();
        if (!getShowViewWhileRefreshing() || null == adapter || adapter.isEmpty()) {
            super.setRefreshingInternal(doScroll);
            return;
        }

        super.setRefreshingInternal(false);

        PullLoadingLayoutfooter originalLoadingLayout;
        PullLoadingLayoutfooter listViewLoadingLayout = null;
        PullLoadingLayoutfooter listViewLoadingLayout2 = null;
        final int selection, scrollToY;

        switch (getCurrentMode()) {
            case MODE_PULL_UP_TO_REFRESH:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = getRefreshableView().getCount() - 1;
                scrollToY = getScrollY() - getHeaderHeight();
                break;
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout2 = mHeaderLoadingView;
                selection = 0;
                scrollToY = getScrollY() + getHeaderHeight();
                break;
        }

        if (doScroll) {
            // We scroll slightly so that the ListView's header/footer is at the
            // same Y position as our normal header/footer
            setHeaderScroll(scrollToY);
        }

        // Hide our original Loading View
        originalLoadingLayout.setVisibility(View.INVISIBLE);

        // Show the ListView Loading View and set it to refresh
        if(getCurrentMode()==MODE_PULL_UP_TO_REFRESH){
            listViewLoadingLayout.setVisibility(View.VISIBLE);
            listViewLoadingLayout.refreshing();
        }else {
            listViewLoadingLayout2.setVisibility(View.VISIBLE);
            listViewLoadingLayout2.refreshing();
        }



        if (doScroll) {
            // Make sure the ListView is scrolled to show the loading
            // header/footer
            getRefreshableView().setSelection(selection);

            // Smooth scroll as normal
            smoothScrollTo(0);
        }
    }
}
