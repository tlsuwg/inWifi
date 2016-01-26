package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.staggered;

import android.annotation.TargetApi;
import android.content.Context;
import android.hxuehh.com.R;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;

import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullToRefreshBase_1;



//瀑布流 + 滑动位置
public class PullToRefreshStaggeredGridView_2 extends PullToRefreshBase_1<StaggeredGridView> {

    public PullToRefreshStaggeredGridView_2(Context context) {
        super(context);
    }

    public PullToRefreshStaggeredGridView_2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected StaggeredGridView createRefreshableView(Context context, AttributeSet attrs) {
        StaggeredGridView stgv;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            stgv = new InternalStaggeredGridViewSDK9(context, attrs);
        } else {
            stgv = new StaggeredGridView(context, attrs);
        }

        int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
        stgv.setColumnCount(2);
        stgv.setItemMargin(margin);
        // stgv.setPadding(margin, 0, margin, 0);

        stgv.setId(R.id.stgv);
        return stgv;
    }

    @Override
    protected void dumpGone(boolean isGone,boolean hander) {

    }

    @Override
    protected boolean isReadyForPullDown() {
        return refreshableView.mGetToTop;
    }

    @Override
    protected boolean isReadyForPullUp() {
        return false;
    }


    public void setAdapter(BaseAdapter adapter) {
        refreshableView.setAdapter(adapter);
    }




//    兼容性存在
    @TargetApi(9)
    final class InternalStaggeredGridViewSDK9 extends StaggeredGridView {

        public InternalStaggeredGridViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
//            OverscrollHelper.overScrollBy(PullToRefreshStaggeredGridView.this, deltaX, scrollX, deltaY, scrollY,
//                    getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }

    public final void setOnLoadmoreListener(StaggeredGridView.OnLoadmoreListener listener) {
        refreshableView.setOnLoadmoreListener(listener);
    }

}
