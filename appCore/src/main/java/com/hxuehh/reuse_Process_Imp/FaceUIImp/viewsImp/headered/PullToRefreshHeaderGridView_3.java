package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.headered;

import android.content.Context;
import android.content.res.TypedArray;
import android.hxuehh.com.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.EmptyViewMethodAccessor;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullLoadingLayout;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullToRefreshBase_1;

//GridView+ 下拉刷新
public class PullToRefreshHeaderGridView_3 extends PullToRefreshAdapterViewBase_2<HeaderGridView> {
    private PullLoadingLayout mHeaderLoadingView;
    private PullLoadingLayout mFooterLoadingView;
    private FrameLayout mLvFooterLoadingFrame;
    private boolean mAddedLvFooter = false;
    private  TextView dump;
    private int modifyHeight=titleHeight;
    private LinearLayout dumpLinearLayout;
    private boolean hander=false;// 双击tab 还是true手动刷新
    private boolean isRunning=false;//控制线程停止
    private boolean isStop=false;//线程是否停止
     //private  boolean margintops;
    public PullToRefreshHeaderGridView_3(Context context) {
        super(context);
        setDisableScrollingWhileRefreshing(false);
    }
    public PullToRefreshHeaderGridView_3(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDisableScrollingWhileRefreshing(false);
    }
    @Override
    protected HeaderGridView createRefreshableView(Context context, AttributeSet attrs) {
        HeaderGridView lv = new InternalHeaderGridView(context, attrs);
        final int mode = getMode();

        boolean isShowHeader;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.pullshowheader);
        isShowHeader = typedArray.getBoolean(R.styleable.pullshowheader_showheader, true);

        TypedArray typedArray2 = context.obtainStyledAttributes(attrs, R.styleable.pullmargintops);
        titleHeight = (int) typedArray2.getDimension(R.styleable.pullmargintops_titleheight, 0);
        String pullLabel = context.getString(R.string.pull_to_refresh);
        String refreshingLabel = context.getString(R.string.label_loading);
        String releaseLabel = context.getString(R.string.pull_to_refresh_release);
        String upLabel = context.getString(R.string.up_to_refresh);

        if (mode == PullToRefreshBase_1.MODE_PULL_DOWN_TO_REFRESH || mode == PullToRefreshBase_1.MODE_BOTH) {
          // FrameLayout frame = new FrameLayout(context);
            mHeaderLoadingView = new PullLoadingLayout(context, PullToRefreshBase_1.MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel, isShowHeader);
          //  frame.addView(mHeaderLoadingView, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            mHeaderLoadingView.setVisibility(View.GONE);
//            lv.addHeaderView(frame, null, false);
            dump=new TextView(context);
            LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,titleHeight);
//            dump.setBackgroundColor(context.getResources().getColor(R.color.v_color_f0));
            dump.setLayoutParams(layoutParams);
           // lv.addHeaderView(textView);
            LinearLayout linearLayout =new LinearLayout(context);
            dumpLinearLayout=new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams. MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(lp1);
            dumpLinearLayout.setLayoutParams(lp1);
            dumpLinearLayout.addView(dump);
            linearLayout.addView(dumpLinearLayout);
            linearLayout.addView(mHeaderLoadingView);
            if(titleHeight==0){
                dump.setVisibility(View.GONE);
            }else{
                dump.setVisibility(View.VISIBLE);
            }
            lv.addHeaderView( linearLayout, null, false);
        }
        if (mode == PullToRefreshBase_1.MODE_PULL_UP_TO_REFRESH || mode == PullToRefreshBase_1.MODE_BOTH) {
            mLvFooterLoadingFrame = new FrameLayout(context);
            mFooterLoadingView = new PullLoadingLayout(context, PullToRefreshBase_1.MODE_PULL_UP_TO_REFRESH, releaseLabel, upLabel, refreshingLabel, isShowHeader);
            mLvFooterLoadingFrame.addView(mFooterLoadingView, FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            mFooterLoadingView.setVisibility(View.GONE);
        }
        lv.setId(android.R.id.list);
        return lv;
    }

    @Override
    protected void dumpGone(boolean isGone,boolean hand) {
        if(titleHeight!=0){
            hander=hand;
            if(isGone){

               smoothSetHeaderHeight(titleHeight, 0, 150);
             // dump.setVisibility(View.GONE);
            }else{
              dumpLinearLayout.getLayoutParams().height =titleHeight;
                dumpLinearLayout.requestLayout();
                //smoothSetHeaderHeight(0,titleHeight, 500);
               dump.setVisibility(View.VISIBLE);
            }
        }else{
            dump.setVisibility(View.GONE);
        }

    }

    private class InternalHeaderGridView extends HeaderGridView implements EmptyViewMethodAccessor {
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
        @Override
        public void setAdapter(ListAdapter adapter) {
            // Add the Footer View at the last possible moment
            if (!mAddedLvFooter && null != mLvFooterLoadingFrame) {
                addFooterView(mLvFooterLoadingFrame, null, false);
                mAddedLvFooter = true;
            }

            super.setAdapter(adapter);
        }

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
        PullLoadingLayout originalLoadingLayout;
        PullLoadingLayout listViewLoadingLayout;
        int scrollToHeight = getHeaderHeight();
        int selection;
        boolean scroll;
        switch (getCurrentMode()) {
            case PullToRefreshBase_1.MODE_PULL_UP_TO_REFRESH:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = getRefreshableView().getCount() - 1;
                scroll = getRefreshableView().getLastVisiblePosition() == selection;
                break;
            case PullToRefreshBase_1.MODE_PULL_DOWN_TO_REFRESH:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout = mHeaderLoadingView;
                scrollToHeight *= -1;
                selection = 0;
                scroll = getRefreshableView().getFirstVisiblePosition() == selection;
                break;
        }
        if(titleHeight!=0){
            isRunning=false;
            dumpLinearLayout.getLayoutParams().height =titleHeight;
            dumpLinearLayout.requestLayout();
            dump.setVisibility(View.VISIBLE);
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
        listViewLoadingLayout.setVisibility(View.GONE);

        super.resetHeader();
    }

    public void smoothSetHeaderHeight(final int from, final int to, long duration) {
        final int frameCount = (int) (duration / 1000f * 30) + 1;
        isRunning=true;
        final float partation = (to - from) / (float) frameCount;
        new Thread("Thread#smoothSetHeaderHeight") {

            @Override
            public void run() {
             try {
                 if(hander){
                 }
                 else{
                     sleep(100);
                 }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < frameCount; i++) {
                    if(isRunning){
                        final int height;
                        if (i == frameCount - 1) {
                            height = to;
                        } else {
                            height = (int) (from + partation * i);
                        }
                        post(new Runnable() {
                            public void run() {
                                setHeaderHeight(height);
                            }
                        });
                        try {
                            sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        return;
                    }

                }
            };

        }.start();
    }

   private void setHeaderHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        if (modifyHeight != height) {
            modifyHeight = height;
            dumpLinearLayout.getLayoutParams().height =height;
            dumpLinearLayout.requestLayout();
        }
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

        final PullLoadingLayout originalLoadingLayout, listViewLoadingLayout;
        final int selection, scrollToY;

        switch (getCurrentMode()) {
            case PullToRefreshBase_1.MODE_PULL_UP_TO_REFRESH:
                originalLoadingLayout = getFooterLayout();
                listViewLoadingLayout = mFooterLoadingView;
                selection = getRefreshableView().getCount() - 1;
                scrollToY = getScrollY() - getHeaderHeight();
                break;
            case PullToRefreshBase_1.MODE_PULL_DOWN_TO_REFRESH:
            default:
                originalLoadingLayout = getHeaderLayout();
                listViewLoadingLayout = mHeaderLoadingView;
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
        listViewLoadingLayout.setVisibility(View.VISIBLE);
        if(titleHeight!=0 ){
            isRunning=false;
                    dumpLinearLayout.getLayoutParams().height =titleHeight;
                    dumpLinearLayout.requestLayout();
                    dump.setVisibility(View.VISIBLE);

        }


        listViewLoadingLayout.refreshing();

        if (doScroll) {
            // Make sure the ListView is scrolled to show the loading
            // header/footer
            getRefreshableView().setSelection(selection);

            // Smooth scroll as normal
            smoothScrollTo(0);
        }
    }
}
