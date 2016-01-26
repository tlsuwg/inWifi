package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.headered;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullToRefreshBase_2;

/**
 * Created by yubaojian on 14-8-28.
 */
public class PullToReshAdapterViewBase_stidiygrideview <T extends AbsListView> extends PullToRefreshBase_2<T> {
    private View emptyView;
    private FrameLayout refreshableViewHolder;

    public PullToReshAdapterViewBase_stidiygrideview(Context context) {
        super(context);
    }

    public PullToReshAdapterViewBase_stidiygrideview(Context context, int mode) {
        super(context, mode);
    }

    public PullToReshAdapterViewBase_stidiygrideview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBackToTopView() {
        if (refreshableView instanceof ListView) {
            ((ListView) refreshableView).setSelection(0);
        } else if (refreshableView instanceof GridView) {
            ((GridView) refreshableView).setSelection(0);
        }
    }


    public final void setEmptyView(View newEmptyView) {
        if (null != emptyView) {
            refreshableViewHolder.removeView(emptyView);
        }

        if (null != newEmptyView) {
            ViewParent newEmptyViewParent = newEmptyView.getParent();
            if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
                ((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
            }

            this.refreshableViewHolder.addView(newEmptyView, ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT);
        }


        this.refreshableView.setEmptyView(newEmptyView);
    }







    protected void addRefreshableView(Context context, T refreshableView) {
        refreshableViewHolder = new FrameLayout(context);
        refreshableViewHolder.addView(refreshableView, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        addView(refreshableViewHolder, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 0, 1.0f));
    }

    /**
     * This is implemented by derived classes to return the created View. If you
     * need to use a custom View (such as a custom ListView), override this
     * method and return an instance of your custom class.
     * <p/>
     * Be sure to set the ID of the view in this method, especially if you're
     * using a ListActivity or ListFragment.
     *
     * @param context
     * @param attrs   AttributeSet from wrapped class. Means that anything you
     *                include in the XML layout declaration will be routed to the
     *                created View
     * @return New instance of the Refreshable View
     */
    @Override
    protected T createRefreshableView(Context context, AttributeSet attrs) {
        return null;
    }


    protected boolean isReadyForPullDown() {
        return isFirstItemVisible();
    }

    protected boolean isReadyForPullUp() {
        return isLastItemVisible();
    }

    public boolean isFirstItemVisible() {
        if (this.refreshableView.getCount() == 0) {
            return true;
        } else if (refreshableView.getFirstVisiblePosition() == 0) {

            final View firstVisibleChild = refreshableView.getChildAt(0);

            if (firstVisibleChild != null) {
                return firstVisibleChild.getTop() >= refreshableView.getTop();
            }
        }

        return false;
    }

    public boolean isLastItemVisible() {
        final int count = this.refreshableView.getCount();
        final int lastVisiblePosition = refreshableView.getLastVisiblePosition();

        if (count == 0) {
            return true;
        } else if (lastVisiblePosition == count - 1) {

            final int childIndex = lastVisiblePosition - refreshableView.getFirstVisiblePosition();
            final View lastVisibleChild = refreshableView.getChildAt(childIndex);

            if (lastVisibleChild != null) {
                return lastVisibleChild.getBottom() <= refreshableView.getBottom();
            }
        }
        return false;
    }

}
