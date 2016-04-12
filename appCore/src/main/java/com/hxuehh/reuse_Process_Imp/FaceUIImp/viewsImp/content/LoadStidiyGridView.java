package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.content;//package com.TuanDD.framework.dataFaceLoadView.faceUI.views.content;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.TuanDD.framework.dataFaceLoadView.faceUI.androidWrap.FaceBaseActivity_1;
//import com.TuanDD.framework.dataFaceLoadView.faceUI.progressView.viewInterfaces.LoadForViewManager;
//import com.TuanDD.framework.dataFaceLoadView.faceUI.progressView.viewInterfaces.LoadView;
//import com.TuanDD.framework.dataFaceLoadView.faceUI.views.pulltorefresh.PullToRefreshBase_1;
//import com.TuanDD.framework.dataFaceLoadView.faceUI.views.pulltorefresh.PullToRefreshBase_2;
//import com.TuanDD.framework.dataFaceLoadView.views.stickygridheaders.PullToRefreshStidiyGridView2;
//import com.TuanDD.framework.dataFaceLoadView.views.stickygridheaders.StickyGridHeadersBaseAdapterWrapper3;
//import com.TuanDD.framework.dataFaceLoadView.views.stickygridheaders.StickyGridHeadersSimpleAdapter;
//import com.TuanDD.framework.dataFaceLoadView.views.stickygridheaders.StickyGridHeadersSimpleAdapterWrapper;
//import com.TuanDD.framework.develop.FaceEvent事件处理forDlp;
//import com.TuanDD.framework.develop.FaceEvent回调输入事件forDlp;
//import Image13lLoader;
//import StringUtil;
//
//import com.hxuehh.rebirth.stickygrid.StickyGridHeadersGridView;
//import com.hxuehh.rebirth.stickygrid.StickyGridHeadersSimpleArrayAdapter;
//
//
//import java.util.List;
//
///**
// * Created by yubaojian on 14-8-22.
// */
//public class LoadStidiyGridView extends LoadView {
//    private static final String TAG = LoadStaggeredGridView.class.getSimpleName();
//    private FaceBaseActivity_1 mContext;
//    private boolean isAddTopBanner;//只是为了回调一次
//    private StickyGridHeadersGridView stickyGridHeadersGridView; // 下拉刷新瀑布流
//    private PullToRefreshStidiyGridView2 pullToRefreshStidiyGridView;
//    boolean isaddAll;
//    private View mStaggerAllFootView; // 包括下面两个
//    private LinearLayout mLoadingLayer;//正在加载
//    private LinearLayout mToButtomNoDataLayer;//到达底部
//    private LoadForViewManager mLoadForViewManager;
//    private StickyGridHeadersSimpleArrayAdapter mDealSwipeListAdapter;
//    private StickyGridHeadersSimpleAdapterWrapper baseAdapter;
//    private StickyGridHeadersBaseAdapterWrapper3 mAdapter;
//    private boolean nomoredat=true;
//    private boolean first =true;
//    private String mBannerImgUrl;
//    public  LoadStidiyGridView(FaceBaseActivity_1 context,
//                               LoadForViewManager manager,
//                               StickyGridHeadersSimpleArrayAdapter adapter,String mBannerImgUrl) {
//
//        this.mContext = context;
//        this.mLoadForViewManager = manager;
//        this.mDealSwipeListAdapter = adapter;
//        this.mBannerImgUrl = mBannerImgUrl;
//        initViews();
//        registerListeners();
//    }
//
//    private void initViews() {
//        mainView=  LayoutInflater.from(mContext).inflate(R.layout.stidygride, null);
//        pullToRefreshStidiyGridView=(PullToRefreshStidiyGridView2)mainView.findViewById(R.id.sticky_grid);
//
//        //pullToRefreshStidiyGridView.setMode(PullToRefreshBase.Mode.BOTH);
//        stickyGridHeadersGridView = pullToRefreshStidiyGridView.getRefreshableView();
//        pullToRefreshStidiyGridView.setMode(PullToRefreshBase_1.MODE_DISABLE);
//        pullToRefreshStidiyGridView.setOnRefreshListener(new PullToRefreshBase_2.OnRefreshListener() {
//            @Override
//            public void onRefresh(boolean isFromButtomToTop) {
//                if(isFromButtomToTop&&nomoredat){
//                    mLoadForViewManager.loadNextPage(true);
//                }
//
//            }
//
//        });
//            baseAdapter = new StickyGridHeadersSimpleAdapterWrapper(
//                    (StickyGridHeadersSimpleAdapter)mDealSwipeListAdapter);
//
//        this.mAdapter = new StickyGridHeadersBaseAdapterWrapper3(mContext, stickyGridHeadersGridView,baseAdapter);
//       if (!StringUtil.isEmpty(mBannerImgUrl)){
//            View view=View.inflate(mContext,R.layout.hd_special_title,null);
//            LinearLayout container= (LinearLayout) view.findViewById(R.id.container);
//            final ImageView imgView = (ImageView) view.findViewById(R.id.img_title);
//            imgView.setImageResource(R.drawable.img_default_banner);
//            Image13lLoader.getInstance().loadImage(mBannerImgUrl, imgView);
//            this.mAdapter.addHeader(container);
//}
//
//        stickyGridHeadersGridView.setAdapter(mAdapter);
//        stickyGridHeadersGridView.setHorizontalSpacing(10);
//        stickyGridHeadersGridView.setVerticalSpacing(10);
//
//    }
//
//
//    @FaceEvent回调输入事件forDlp
//    private void registerListeners() {
//        stickyGridHeadersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//
//
//            }
//        });
//    }
//
//
//    @FaceEvent事件处理forDlp
//    @Override
//    public void loading(boolean isHasMoreToLoad, boolean isMustNeedEndTag) {
//        nomoredat=isHasMoreToLoad;
//        // 加载第一屏，即下拉刷新。
//        if (mLoadForViewManager.isReloadFromStart()) {
//            //removeLoadingFooterView();
//        }
//
//        if (!isHasMoreToLoad) {
//
//            if (isMustNeedEndTag) {
//              //pullToRefreshStidiyGridView.onRefreshComplete();
//              //pullToRefreshStidiyGridView.onRefreshCompleteaddfooter();
//              // addLoadingFooterView();
//            } else {
//              //pullToRefreshStidiyGridView.onRefreshComplete();
//              //pullToRefreshStidiyGridView.onRefreshCompleteaddfooter();
//            }
//            return;
//        }
//
//        if (stickyGridHeadersGridView!= null) {
//           // mStaggerAllFootView.setVisibility(View.VISIBLE);
//           // mLoadingLayer.setVisibility(View.VISIBLE);
//           // mToButtomNoDataLayer.setVisibility(View.GONE);
//        }
//    }
//    @FaceEvent事件处理forDlp
//    @Override
//    public void loaded(Long loadTime, int thisPageNumber, boolean isFromZero, boolean isHasMoreToLoad, List data) {
//        pullToRefreshStidiyGridView.onRefreshComplete();
//       // nomoredat=isHasMoreToLoad;
//       // if(!isHasMoreToLoad){
//            pullToRefreshStidiyGridView.onRefreshComplete();
//          ((StickyGridHeadersSimpleAdapterWrapper) baseAdapter).addFooterData(true);
//            ((StickyGridHeadersBaseAdapterWrapper3) mAdapter).addFooterData(true);
//                mDealSwipeListAdapter.notifyDataSetChanged();
//
//           // stickyGridHeadersGridView.setAdapter(mAdapter);
//           // pullToRefreshStidiyGridView.removefooter();
//           // stickyGridHeadersGridView.setSelection(mAdapter.getCount());
//           // mDealSwipeListAdapter.notifyDataSetInvalidated();
//        }
//        //removeFooterView();
//   // }
//
//    @FaceEvent事件处理forDlp
//    @Override
//    public void loadErr(int errKey, int errTimes, boolean isReload, Exception... err) {
//        pullToRefreshStidiyGridView.onRefreshComplete();
//       // removeFooterView();
//    }
//
//    @Override
//    public void setLast(int i) {
//
//    }
//
//    @Override
//    public void setSelectIndex(int index) {
//
//    }
//
//    @Override
//    public void setPullToRefreshMode(int mode) {
//
//    }
//
//
//    private void removeFooterView() {
//        if (stickyGridHeadersGridView != null) {
//            mStaggerAllFootView.setVisibility(View.GONE);
//        }
//    }
//
//    private void removeLoadingFooterView() {
//
//        if (stickyGridHeadersGridView != null) {
//            mStaggerAllFootView.setVisibility(View.GONE);
//            mLoadingLayer.setVisibility(View.VISIBLE);
//            mToButtomNoDataLayer.setVisibility(View.GONE);
//
//        }
//    }
//
//    private void addLoadingFooterView() {
//
//        if (stickyGridHeadersGridView != null) {
//            mStaggerAllFootView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mStaggerAllFootView.setVisibility(View.VISIBLE);
//                    mLoadingLayer.setVisibility(View.GONE);
//                    mToButtomNoDataLayer.setVisibility(View.VISIBLE);
//
//                }
//            }, 50);
//        }
//    }
//
//    public void notifyadapter() {
//        this.mAdapter.notifyDataSetChanged();
//    }
//}
