package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.im;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.hxuehh.com.R;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.pulltorefresh.PullLoadingLayoutIM;



/**
 * @param <T>
 * @author sunguowei
 */
public abstract class IMPullToRefreshBase<T extends ListView> extends RelativeLayout {

    // 手指摩擦阻力
    private static final float FRICTION = 2.0f;

    // 状态：下拉可刷新
    private static final int PULL_TO_REFRESH = 0x0;
    // 状态：松手即刷新
    private static final int RELEASE_TO_REFRESH = 0x1;
    // 状态：正在刷新
    private static final int REFRESHING = 0x2;
    // 状态：手动刷新
    protected static final int MANUAL_REFRESHING = 0x3;

    // 模式：下拉刷新
    public static final int MODE_PULL_DOWN_TO_REFRESH = 0x4;
    // 模式：不可刷新
    public static final int MODE_DISABLE = 0X5;

    // 记录当前的状态@see line from 37 to 43
    private int state = PULL_TO_REFRESH;
    // 首次初始化的模式@see line from 46 to 50
    private int mode = MODE_PULL_DOWN_TO_REFRESH;
    // 当前的模式
    private int currentMode;

    // 手指滑动距离
    private int touchSlop;

    // 手指滑动的坐标变量
    private float initialMotionY;
    private float lastMotionX;
    private float lastMotionY;

    // 是否正处于拉动状态
    private boolean isBeingDragged = false;
    private boolean isPullToRefreshEnabled = true;
    // 正在刷新的时候不能滚动
    private boolean disableScrollingWhileRefreshing = true;
    private boolean showViewWhileRefreshing = true;

    // header view的高度
    private int headerHeight;

    // ListView实例
    protected T refreshableView;

    // 下拉过程中，显示的的头视图。
    private PullLoadingLayoutIM headerLayout;
    // ListView的头视图。
    private PullLoadingLayoutIM originLayout;

    // ListView的头视图外层的ViewGroup，引入这个的目的是能够隐藏或显示ListView的头视图。
    private FrameLayout originLayoutHolder;


    // 上下文
    private Context mContext;
    // 处理器
    private final Handler handler = new Handler();
    // 滑动视图的工作线程
    private SmoothScrollRunnable currentSmoothScrollRunnable;
    // 下拉刷新的监听器
    private OnRefreshListener onRefreshListener;

    // ListView的头视图。
    private PullLoadingLayoutIM footerLayout;
    private FrameLayout footerLayoutHolder;

    // 没有历史消息，显示这个header
    //private View specialHeaderLayout;
    //private FrameLayout specialHeaderLayoutHolder;



    /**
     * 构造器
     */
    public IMPullToRefreshBase(Context context) {
        super(context);
        mContext = context;
        init(context, null);
    }

    /**
     * 构造器
     */
    public IMPullToRefreshBase(Context context, int mode) {
        super(context);
        mContext = context;
        this.mode = mode;
        init(context, null);
    }

    /**
     * 构造器
     */
    public IMPullToRefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs);
    }

    /**
     * 获取可刷新视图的实例，实为ListView
     */
    public final T getRefreshableView() {
        return refreshableView;
    }

    /**
     * 获取ListView的头视图
     */
    public View getHeaderView() {
        return originLayout;
    }

    public final boolean getShowViewWhileRefreshing() {
        return showViewWhileRefreshing;
    }

    public final boolean isPullToRefreshEnabled() {
        return isPullToRefreshEnabled;
    }

    public final void setDisableScrollingWhileRefreshing(boolean disableScrollingWhileRefreshing) {
        this.disableScrollingWhileRefreshing = disableScrollingWhileRefreshing;
    }

    public final boolean isDisableScrollingWhileRefreshing() {
        return disableScrollingWhileRefreshing;
    }

    /**
     * 是否正在刷新
     */
    public final boolean isRefreshing() {
        return state == REFRESHING || state == MANUAL_REFRESHING;
    }

    /**
     * 刷新完成
     */
    public final void onRefreshComplete() {
        if (state != PULL_TO_REFRESH) {
            resetHeader();
        }
    }

    /**
     * 设置刷新监听
     */
    public final void setOnRefreshListener(OnRefreshListener listener) {
        onRefreshListener = listener;
    }

    public final void setPullToRefreshEnabled(boolean enable) {
        this.isPullToRefreshEnabled = enable;
    }

    public void setReleaseLabel(String releaseLabel) {
        if (null != headerLayout) {
            headerLayout.setReleaseLabel(releaseLabel);
            originLayout.setReleaseLabel(releaseLabel);
        }
    }

    public void setPullLabel(String pullLabel) {
        if (null != headerLayout) {
            headerLayout.setPullLabel(pullLabel);
            originLayout.setPullLabel(pullLabel);
        }
    }

    public void setRefreshingLabel(String refreshingLabel) {
        if (null != headerLayout) {
            headerLayout.setRefreshingLabel(refreshingLabel);
        }
    }

    public final void setRefreshing() {
        this.setRefreshing(true);
    }

    public final void setHeaderRefreshing() {
        if (!isRefreshing()) {
            state = REFRESHING;
            if (null != headerLayout) {
                headerLayout.refreshing();
                originLayout.refreshing();
            }
            smoothScrollTo(-headerHeight);
            state = MANUAL_REFRESHING;
        }
    }

    public final void setFooterLoading() {
        if (!isRefreshing()) {
            state = REFRESHING;
            setHeaderScroll(headerHeight);
            state = MANUAL_REFRESHING;
        }
    }

    public final void setRefreshing(boolean doScroll) {
        if (!isRefreshing()) {
            setRefreshingInternal(doScroll);
            state = MANUAL_REFRESHING;
        }
    }

    protected void resetHeader() {
        state = PULL_TO_REFRESH;
        isBeingDragged = false;

        if (null != headerLayout) {
            headerLayout.reset();
            originLayout.reset();
        }

        // scrollTo(0, 0);
        smoothScrollTo(0);

        switch (mode) {
            default:
            case MODE_PULL_DOWN_TO_REFRESH:
                LayoutParams rpl_list = (LayoutParams) refreshableView.getLayoutParams();
                rpl_list.topMargin = -headerHeight;
                break;
        }

        footerLayout.setVisibility(View.GONE);

    }

    protected void setRefreshingInternal(boolean doScroll) {
        state = REFRESHING;

        if (null != headerLayout) {
            headerLayout.refreshing();
            originLayout.refreshing();
        }

        if (doScroll) {
            if (showViewWhileRefreshing) {
                smoothScrollTo(currentMode == MODE_PULL_DOWN_TO_REFRESH ? -headerHeight : headerHeight);
            } else {
                smoothScrollTo(0);
            }
        }
        footerLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        touchSlop = ViewConfiguration.getTouchSlop();

        String pullLabel = "下拉加载更多...";
        String refreshingLabel = context.getString(R.string.label_loading);
        String releaseLabel = "下拉加载更多...";

        boolean isShowHeader;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.pullshowheader);
        isShowHeader = typedArray.getBoolean(R.styleable.pullshowheader_showheader, true);

        // 加头
        if (mode == MODE_PULL_DOWN_TO_REFRESH) {
            headerLayout = new PullLoadingLayoutIM(context, MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel, isShowHeader);
            measureView(headerLayout);
            headerHeight = headerLayout.getMeasuredHeight();
        }

        // footer
        footerLayout = new PullLoadingLayoutIM(context, MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel, isShowHeader);
        footerLayoutHolder = new FrameLayout(getContext());
        footerLayoutHolder.addView(footerLayout, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        footerLayout.setVisibility(View.GONE);

        // ListView自己的HeaderView
        originLayout = new PullLoadingLayoutIM(context, MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel, isShowHeader);
        originLayoutHolder = new FrameLayout(getContext());
        originLayoutHolder.addView(originLayout, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        refreshableView = this.createRefreshableView(context, attrs);
        refreshableView.addHeaderView(originLayoutHolder);

        refreshableView.addFooterView(footerLayoutHolder);
        footerLayout.setVisibility(View.GONE);

        // 没有历史消息时，显示的header
//        specialHeaderLayout = View.inflate(mContext, R.layout.list_footer, null);
//        specialHeaderLayoutHolder = new FrameLayout(getFaceContext());
//        specialHeaderLayoutHolder.addView(specialHeaderLayout, 0,
//                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
//        specialHeaderLayout.setVisibility(View.GONE);
//        refreshableView.addHeaderView(specialHeaderLayoutHolder);

        this.addRefreshableView(context, refreshableView);

        switch (mode) {
            default:
            case MODE_PULL_DOWN_TO_REFRESH:
                LayoutParams rpl_list = (LayoutParams) refreshableView.getLayoutParams();
                rpl_list.topMargin = -headerHeight;
                break;
        }
    }

//    public void showSpecialHeader() {
//        if (specialHeaderLayout != null && specialHeaderLayout.getVisibility() != View.VISIBLE) {
//            specialHeaderLayout.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void hideSpecialHeader() {
//        if  (specialHeaderLayout != null && specialHeaderLayout.getVisibility() != View.GONE) {
//            specialHeaderLayout.setVisibility(View.GONE);
//        }
//    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public final boolean onTouchEvent(MotionEvent event) {// 手势处理
        if (!isPullToRefreshEnabled) {
            return false;
        }

        if (isRefreshing() && disableScrollingWhileRefreshing) {
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (isBeingDragged) {
                    lastMotionY = event.getY();
                    this.pullEvent();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                if (isReadyForPull()) {
                    lastMotionY = initialMotionY = event.getY();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                if (isBeingDragged) {
                    isBeingDragged = false;

                    if (state == RELEASE_TO_REFRESH && null != onRefreshListener) {
                        switch (currentMode) {
                            case MODE_PULL_DOWN_TO_REFRESH:
                            default:
                                setHeaderScroll(-headerHeight);
                                break;
                        }

                        setRefreshingInternal(true);
                        onRefreshListener.onRefresh();
                    } else {
                        if (state == REFRESHING) {
                            smoothScrollTo(-headerHeight);
                            footerLayout.setVisibility(View.INVISIBLE);
                        } else {
                            smoothScrollTo(0);
                        }
                    }
                    return true;
                }
                break;
            }
        }

        return false;
    }
    public void onHideSoftInput(MotionEvent event)
    {
        InputMethodManager manager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (((Activity)mContext).getCurrentFocus() != null && ((Activity)mContext).getCurrentFocus().getWindowToken() != null)
            {
               manager.hideSoftInputFromWindow(((Activity)mContext).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public final boolean onInterceptTouchEvent(MotionEvent event) {// 手势拦截


        if (!isPullToRefreshEnabled) {
            return false;
        }

        if (isRefreshing() && disableScrollingWhileRefreshing) {
            return true;
        }

        final int action = event.getAction();

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            isBeingDragged = false;
            return false;
        }

        if (action != MotionEvent.ACTION_DOWN && isBeingDragged) {
            return true;
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                if (isReadyForPull()) {

                    final float y = event.getY();
                    final float dy = y - lastMotionY;
                    final float yDiff = Math.abs(dy);
                    final float xDiff = Math.abs(event.getX() - lastMotionX);

                    if (yDiff > touchSlop && yDiff > xDiff) {
                        if ((mode == MODE_PULL_DOWN_TO_REFRESH) && dy >= 0.0001f
                                && isReadyForPullDown()) {
                            lastMotionY = y;
                            isBeingDragged = true;
                        }

                    }
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                onHideSoftInput( event);
                if (isReadyForPull()) {
                    lastMotionY = initialMotionY = event.getY();
                    lastMotionX = event.getX();
                    isBeingDragged = false;
                }
                break;
            }
        }

        return isBeingDragged;
    }

    private boolean pullEvent() {
        final int newHeight;
        final int oldHeight = this.getScrollY();

        switch (currentMode) {
            case MODE_PULL_DOWN_TO_REFRESH:
            default:
                newHeight = Math.round(Math.min(initialMotionY - lastMotionY, 0) / FRICTION);
                break;
        }

        originLayout.setVisibility(View.VISIBLE);
        headerLayout.setVisibility(View.VISIBLE);
        if (state == REFRESHING) {
            setHeaderScroll(newHeight - headerHeight);
        } else {
            setHeaderScroll(newHeight);
        }

        if (newHeight != 0) {
            if (state == PULL_TO_REFRESH && headerHeight < Math.abs(newHeight)) {
                state = RELEASE_TO_REFRESH;

                switch (currentMode) {
                    case MODE_PULL_DOWN_TO_REFRESH:
                        headerLayout.releaseToRefresh();
                        originLayout.releaseToRefresh();
                        break;
                }

                return true;

            } else if (state == RELEASE_TO_REFRESH && headerHeight >= Math.abs(newHeight)) {
                state = PULL_TO_REFRESH;

                switch (currentMode) {
                    case MODE_PULL_DOWN_TO_REFRESH:
                        headerLayout.pullToRefresh();
                        originLayout.pullToRefresh();
                        break;
                }

                return true;
            }
        }

        return oldHeight != newHeight;
    }

    /**
     * 是否准备好可以下拉了
     */
    private boolean isReadyForPull() {
        switch (mode) {
            case MODE_PULL_DOWN_TO_REFRESH:
                return isReadyForPullDown();
            case MODE_DISABLE:
                return false;
        }
        return false;
    }

    /**
     * 添加可刷新视图
     */
    protected void addRefreshableView(Context context, final T refreshableView) {
        addView(refreshableView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 滑动headerview
     */
    protected final void setHeaderScroll(int y) {
        scrollTo(0, y);
    }

    /**
     * 平滑滚动到y坐标
     */
    protected final void smoothScrollTo(int y) {
        if (null != currentSmoothScrollRunnable) {
            currentSmoothScrollRunnable.stop();
        }

        if (this.getScrollY() != y) {
            this.currentSmoothScrollRunnable = new SmoothScrollRunnable(handler, getScrollY(), y);
            handler.post(currentSmoothScrollRunnable);
        }
    }

    /**
     * 创建可刷新的视图
     *
     * @param context
     * @param attrs
     * @return
     */
    protected abstract T createRefreshableView(Context context, AttributeSet attrs);

    /**
     * 获取当前的模式
     */
    protected final int getCurrentMode() {
        return currentMode;
    }

    /**
     * 获取headerlayout
     *
     * @return
     */
    protected final PullLoadingLayoutIM getHeaderLayout() {
        return headerLayout;
    }

    /**
     * 获取头视图的高度
     */
    public final int getHeaderHeight() {
        return headerHeight;
    }

    /**
     * 获取模式
     */
    protected final int getMode() {
        return mode;
    }

    /**
     * 获取状态
     */
    protected final int getState() {
        return state;
    }

    /**
     * 设置模式
     */
    public final void setMode(int mode) {
        this.mode = mode;
        this.currentMode = mode;
    }

    /**
     * 是否准备可以进行好下拉刷新了
     */
    protected abstract boolean isReadyForPullDown();

    @Override
    public void setLongClickable(boolean longClickable) {
        getRefreshableView().setLongClickable(longClickable);
    }

    public static interface OnLastItemVisibleListener {
        public void onLastItemVisible();
    }

    /**
     * 刷新的监听接口
     */
    public static interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * 平滑移动的工作线程
     */
    final class SmoothScrollRunnable implements Runnable {

        static final int ANIMATION_DURATION_MS = 400;
        static final int ANIMATION_FPS = 1000 / 60;

        private final Interpolator interpolator;
        private final int scrollToY;
        private final int scrollFromY;
        private final Handler handler;

        private boolean continueRunning = true;
        private long startTime = -1;
        private int currentY = -1;

        public SmoothScrollRunnable(Handler handler, int fromY, int toY) {
            this.handler = handler;
            this.scrollFromY = fromY;
            this.scrollToY = toY;
            this.interpolator = new AccelerateDecelerateInterpolator();
        }

        @Override
        public void run() {

            /**
             * Only set startTime if this is the first time we're starting, else
             * actually calculate the Y delta
             */
            if (startTime == -1) {
                startTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - startTime)) / ANIMATION_DURATION_MS;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((scrollFromY - scrollToY)
                        * interpolator.getInterpolation(normalizedTime / 1000f));
                this.currentY = scrollFromY - deltaY;
                setHeaderScroll(currentY);
            }

            // If we're not at the target Y, keep going...
            if (continueRunning && scrollToY != currentY) {
                handler.postDelayed(this, ANIMATION_FPS);
            }
        }

        public void stop() {
            this.continueRunning = false;
            this.handler.removeCallbacks(this);
        }
    }
}
