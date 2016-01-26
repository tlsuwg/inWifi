package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp;

import android.content.Context;
import android.hxuehh.com.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.ScreenUtil;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceLoadCallBack;
import com.hxuehh.reuse_Process_Imp.staicUtil.utils.Tao800Util;



/**
 * Created by suwg on 2014/6/24.
 */

//报错 和成功status等消息展示
//
public class WarnView extends LinearLayout {

    public static final String TAG = "com.hxuehh.rebirth.activities.faceAc.OneBrandGroupDetailActivity";
    private Context mContext;


    public static final int LOADED_OK = 0;
    public static final int LOADING_PROGRESS = 1;
    public static final int LOAD_NO_NET_NO_CACHE = 2;
    public static final int LOAD_NO_NET_HAS_CACHE = 3;
    public static final int LOADED_NO_DATA = 4;
    public static final int LOAD_HANDLER_ERR = 5;
    public static final int LOAD_BOUTIQUE_SWIPE = 6;
    public static final int LOAD_TIMEOUT_NO_CACHE = 7;
    public static final int LOAD_TIMEOUT_HAS_CACHE = 8;
    public static final int LOAD_SERVER_ERROR_NO_CACHE = 9;
    public static final int LOAD_SERVER_ERROR_HAS_CACHE = 10;
    public static final int LOAD_REFRESH_SESSION = 11;
    public static final int LOAD_REFRESH_GONE = 12;
    public static final int LOAD_COMMON_ERROR_NO_CACHE = 13;
    public static final int LOAD_COMMON_ERROR_HAS_CACHE = 14;

    private int mCurrentStatus = LOADING_PROGRESS;


    public ImageView cancel;
    public ImageView loadedNoData;
    public ImageView loadFailureImg;

    public LinearLayout refreshSession;
    public RelativeLayout loadRl;

    public TextView brandTipTv; // 品牌价值体现
    public TextView tv_nodata;

    public RelativeLayout loadFailure;
    public RelativeLayout loadNodata;
    public RelativeLayout loadPartFailure;

    public boolean isPullRefresh = true;
    private TextView tv_rf;
    public LoadingView mLoadingView;
    public String[] mBrandTips;

    public WarnView(Context context) {
        super(context);
        mContext =context;
        initViews();
    }


    public WarnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext =context;
        initViews();
    }

    private void initViews() {

        View.inflate(this.getContext(), R.layout.load_data_stats, this);
        this.tv_nodata= (TextView) findViewById(R.id.tv_nodata);
        this.cancel = (ImageView) findViewById(R.id.img_stats_cancel);
        this.loadedNoData = (ImageView) findViewById(R.id.loaded_no_data);
        this.loadFailureImg = (ImageView) findViewById(R.id.load_failure_img);
        this.loadFailure = (RelativeLayout) findViewById(R.id.load_failure);
        this.loadNodata = (RelativeLayout) findViewById(R.id.load_no_date);
        this.loadPartFailure = (RelativeLayout) findViewById(R.id.load_part_failure);
        this.refreshSession = (LinearLayout) findViewById(R.id.load_refresh_session);
        tv_rf= (TextView) this.findViewById(R.id.tv_rf);

        this.brandTipTv = (TextView) findViewById(R.id.tv_brand_tip);

        this.loadRl = (RelativeLayout) findViewById(R.id.rl_load_layout);

        this.cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoadedOk();
               // setLoadStats(LOADED_OK);
            }
        });

        this.loadRl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        this.mLoadingView = (LoadingView) findViewById(R.id.loading_view);
    }


    public void setMessage(String msg) {
        brandTipTv.setText(msg);
    }

    public void setLoadProgressStatus(){
        isPullRefresh = false;
        loadRl.setVisibility(View.VISIBLE);
        mLoadingView.isShowLoading(true);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
        setBrandTip();
    }

    public void setLoadBoutiqueSwipeStatus(){
        isPullRefresh = false;
        loadRl.setVisibility(View.VISIBLE);
        mLoadingView.isShowLoading(true);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
        setBrandTip();
    }
    public void setLoadNoNetNoCache() {
        isPullRefresh = false;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.VISIBLE);
        loadPartFailure.setVisibility(View.GONE);
        this.loadFailureImg.setImageResource(R.mipmap.app_net_no);
//        if ( mContext.getClass().getName().toString().equals("com.hxuehh.rebirth.activities.faceAc.OneBrandGroupDetailActivity"))
//        {
//            DisplayMetrics displayMetric = new DisplayMetrics();
//            displayMetric = Resources.getSystem().getDisplayMetrics();
//            Rect rect = new Rect(0, 0, displayMetric.widthPixels, displayMetric.heightPixels);
//            int actualHeight = ScreenUtil.px2dip(getFaceContext(), rect.height()) - 150; //减去品牌详情的头部高度
//            ViewGroup.LayoutParams params = this.loadFailure.getLayoutParams();
//            params.height = ScreenUtil.dip2px(getFaceContext(), actualHeight);
//            this.loadFailure.setLayoutParams(params);
//        }
    }
    public void setLoadNoNetHasCache(){

        isPullRefresh = true;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.VISIBLE);
        loadRl.setVisibility(View.GONE);
    }

    public void setLoadedNoData(){
        isPullRefresh = false;
        loadRl.setVisibility(View.GONE);
        loadNodata.setVisibility(View.VISIBLE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
    }


    public void setLoadedNoDataIMCon(){
        isPullRefresh = false;
        tv_nodata.setVisibility(View.VISIBLE);
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.VISIBLE);
        loadFailure.setVisibility(View.GONE);
        loadedNoData.setImageResource(R.mipmap.nomessage);
        loadPartFailure.setVisibility(View.GONE);
    }

    public void setLoadHandlerErr(){
        isPullRefresh = false;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
        Tao800Util.showToast(this.getContext(), R.string.label_data_error);
    }
    public void setLoadTimeoutNoCache(){
        isPullRefresh = false;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.VISIBLE);
        loadPartFailure.setVisibility(View.GONE);

        this.loadFailureImg.setImageResource(R.mipmap.app_net_error);
    }

    public void setLoadTimeoutHasCache(){
        isPullRefresh = true;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
        Tao800Util.showToast(this.getContext(), R.string.label_net_timeout);
    }
    public void setLoadServerErrorNoCache(){
        isPullRefresh = false;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.VISIBLE);
        loadPartFailure.setVisibility(View.GONE);

        this.loadFailureImg.setImageResource(R.mipmap.app_server_error);//3.0.2针对服务器错误进行优化
    }
    public void setLoadServerErrorHasCache(){
        isPullRefresh = true;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
        Tao800Util.showToast(this.getContext(), R.string.label_server_error);
    }
    public void setLoadCommonErrorNoCache(){
        isPullRefresh = false;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.VISIBLE);
        loadPartFailure.setVisibility(View.GONE);

        this.loadFailureImg.setImageResource(R.mipmap.app_net_error);
    }
    public void setLoadCommonErrorHasCache(){
        isPullRefresh = true;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
        Tao800Util.showToast(this.getContext(), R.string.label_net_timeout);

    }

    public void setLoadRefreshSession(boolean iscomment){
        if (isPullRefresh&&!iscomment) {

            refreshSession.setVisibility(View.VISIBLE);
        }
    }
    public void setLoadRefreshGone(){
        refreshSession.setVisibility(View.GONE);
    }
    public void setRefreshSessionErr(){

            refreshSession.setVisibility(View.VISIBLE);
        tv_rf.setText("刷新失败");
    }
    public void setLoadedOk(){
        isPullRefresh = true;
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.GONE);
        loadFailure.setVisibility(View.GONE);
        loadPartFailure.setVisibility(View.GONE);
    }
    public void setLoadRefreshSessionMuyingBrand(int isfrom,boolean isRecomment){
        if (isPullRefresh) {
            if(isfrom==1&&!isRecomment){//母婴儿
                // LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin= ScreenUtil.dip2px(this.getContext(), 40);
                refreshSession.setLayoutParams(layoutParams);
                refreshSession.setVisibility(View.VISIBLE);
            }else if(isfrom==2&&!isRecomment){
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin= ScreenUtil.dip2px(this.getContext(), 15);
                refreshSession.setLayoutParams(layoutParams);
                refreshSession.setVisibility(View.VISIBLE);
            }else if(isfrom==3&&!isRecomment){
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin= ScreenUtil.dip2px(this.getContext(), 52);
                refreshSession.setLayoutParams(layoutParams);
                refreshSession.setVisibility(View.VISIBLE);
            }


        }
    }
   /* public void setLoadStats(int loadStats) {
        mCurrentStatus = loadStats;
        switch (loadStats) {
            case LOADING_PROGRESS:
                isPullRefresh = false;
                loadRl.setVisibility(View.VISIBLE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                setBrandTip();
                break;

            case LOAD_BOUTIQUE_SWIPE:
                isPullRefresh = false;
                loadRl.setVisibility(View.VISIBLE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                setBrandTip();
                break;

            case LOAD_NO_NET_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_net_no);
                break;

            case LOAD_NO_NET_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.VISIBLE);
                break;

            case LOADED_NO_DATA:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.VISIBLE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                break;

            case LOAD_HANDLER_ERR:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_data_error);
                break;

            case LOAD_TIMEOUT_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_net_error);
                break;

            case LOAD_TIMEOUT_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_net_timeout);
                break;

            case LOAD_SERVER_ERROR_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_server_error);//3.0.2针对服务器错误进行优化
                break;

            case LOAD_SERVER_ERROR_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_server_error);
                break;

            case LOAD_COMMON_ERROR_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_net_error);
                break;

            case LOAD_COMMON_ERROR_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_net_timeout);
                break;


            case LOAD_REFRESH_SESSION:
                if (isPullRefresh) {

                    refreshSession.setVisibility(View.VISIBLE);
                }
                break;

            case LOAD_REFRESH_GONE:
                refreshSession.setVisibility(View.GONE);
                break;

            case LOADED_OK:
            default:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                break;
        }
    }
*//**
 * 母婴品牌团专用
 **//*
    public void setLoadStats(int loadStats,int isfrom) {
        mCurrentStatus = loadStats;
        switch (loadStats) {
            case LOADING_PROGRESS:
                isPullRefresh = false;
                loadRl.setVisibility(View.VISIBLE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                setBrandTip();
                break;

            case LOAD_BOUTIQUE_SWIPE:
                isPullRefresh = false;
                loadRl.setVisibility(View.VISIBLE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                setBrandTip();
                break;

            case LOAD_NO_NET_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_net_no);
                break;

            case LOAD_NO_NET_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.VISIBLE);
                break;

            case LOADED_NO_DATA:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.VISIBLE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                break;

            case LOAD_HANDLER_ERR:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_data_error);
                break;

            case LOAD_TIMEOUT_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_net_error);
                break;

            case LOAD_TIMEOUT_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_net_timeout);
                break;

            case LOAD_SERVER_ERROR_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_server_error);//3.0.2针对服务器错误进行优化
                break;

            case LOAD_SERVER_ERROR_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_server_error);
                break;

            case LOAD_COMMON_ERROR_NO_CACHE:
                isPullRefresh = false;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.VISIBLE);
                loadPartFailure.setVisibility(View.GONE);

                this.loadFailureImg.setImageResource(R.drawable.app_net_error);
                break;

            case LOAD_COMMON_ERROR_HAS_CACHE:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                Tao800Util.showShortToast(this.getFaceContext(), R.string.label_net_timeout);
                break;


            case LOAD_REFRESH_SESSION:
                if (isPullRefresh) {
                    if(isfrom==1){//母婴儿
                       // LayoutParams layoutParams=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                        layoutParams.topMargin= ScreenUtil.dip2px(this.getFaceContext(),40);
                        refreshSession.setLayoutParams(layoutParams);
                    }else if(isfrom==2){
                        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                        layoutParams.topMargin= ScreenUtil.dip2px(this.getFaceContext(),48);
                        refreshSession.setLayoutParams(layoutParams);
                    }else if(isfrom==3){
                        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
                        layoutParams.topMargin= ScreenUtil.dip2px(this.getFaceContext(),52);
                        refreshSession.setLayoutParams(layoutParams);
                    }

                    refreshSession.setVisibility(View.VISIBLE);
                }
                break;

            case LOAD_REFRESH_GONE:
                refreshSession.setVisibility(View.GONE);
                break;

            case LOADED_OK:
            default:
                isPullRefresh = true;
                loadRl.setVisibility(View.GONE);
                loadNodata.setVisibility(View.GONE);
                loadFailure.setVisibility(View.GONE);
                loadPartFailure.setVisibility(View.GONE);
                break;
        }
    }*/
    public int getCurrentStatus() {
        return mCurrentStatus;
    }


    public void setOnLoadErrorListener(final OnLoadErrorListener listener) {
        findViewById(R.id.load_failure).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAgainRefresh();
            }
        });

        findViewById(R.id.load_no_date).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // 没有数据，不可点击加载。
                // listener.onAgainRefresh();
            }
        });
    }

    public void setBrandTip() {
        /*int tipId = (int) (Math.random() * 25);
        int sourceId = getResources().getIdentifier("string/brand_tip_string" + tipId, null, this.getFaceContext().getPackageName());
        try {
            brandTipTv.setText(sourceId);
        }catch (Exception e){

        }*/

        try {
            mBrandTips = getResources().getStringArray(R.array.brand_tips);
            int tipId = (int) (Math.random() * mBrandTips.length);
            brandTipTv.setText(mBrandTips[tipId]);
        }catch (Exception e){

        }
    }

    public void setLoadedNoDataIMNoLink() {
        isPullRefresh = false;
        tv_nodata.setVisibility(View.VISIBLE);
        tv_nodata.setText("亲，还没有聊天记录哦");
        loadRl.setVisibility(View.GONE);
        mLoadingView.isShowLoading(false);
        loadNodata.setVisibility(View.VISIBLE);
        loadFailure.setVisibility(View.GONE);
        loadedNoData.setImageResource(R.mipmap.nomessage);
        loadPartFailure.setVisibility(View.GONE);
    }

    public interface OnLoadErrorListener {
        public void onAgainRefresh();
    }

    public void setErrKey(int errKey, int count) {

        switch (errKey) {
            case FaceLoadCallBack.ERR_clone:
                break;
            case FaceLoadCallBack.ERR_no_net:
                if (count == 0) {
                    //setLoadStats(LOAD_NO_NET_NO_CACHE);
                    setLoadNoNetNoCache();
                } else {
                   // setLoadStats(LOAD_NO_NET_HAS_CACHE);
                    setLoadNoNetHasCache();
                }
                break;
            case FaceLoadCallBack.ERR_http_link:
                if (count == 0) {
                    //setLoadStats(LOAD_COMMON_ERROR_NO_CACHE);
                    setLoadCommonErrorNoCache();
                } else {
                   // setLoadStats(LOAD_COMMON_ERROR_HAS_CACHE);
                    setLoadCommonErrorHasCache();
                }
                break;
            case FaceLoadCallBack.ERR_http_server:
            case FaceLoadCallBack.ERR_http_UnknownHostException:
                if (count == 0) {
                   // setLoadStats(LOAD_SERVER_ERROR_NO_CACHE);
                    setLoadServerErrorNoCache();
                } else {
                    //setLoadStats(LOAD_SERVER_ERROR_HAS_CACHE);
                    setLoadServerErrorHasCache();
                }
                break;
            case FaceLoadCallBack.ERR_http_link_time_out:
                if (count == 0) {
                    //setLoadStats(LOAD_TIMEOUT_NO_CACHE);
                    setLoadTimeoutNoCache();
                } else {
                    //setLoadStats(LOAD_TIMEOUT_HAS_CACHE);
                    setLoadTimeoutHasCache();
                }
                break;
            case FaceLoadCallBack.ERR_loaded_all:// no data
                if (count == 0) {
                    //setLoadStats(WarnView.LOADED_NO_DATA);
                    setLoadedNoData();
                }
                break;
            default:

                if (count == 0) {
                    //setLoadStats(LOAD_COMMON_ERROR_NO_CACHE);
                    setLoadCommonErrorNoCache();
                } else {
                   // setLoadStats(LOAD_COMMON_ERROR_HAS_CACHE);
                    setLoadCommonErrorHasCache();
                }

                break;
        }
    }

}
