package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp;

import android.content.Context;
import android.graphics.Color;
import android.hxuehh.com.R;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;


/**
 * Created by suwg on 2014/11/12.
 */
public class ProView extends FaceGetMainViewImp {


    private LinearLayout pro_ing_lin;
    private TextView loading, pro_error_text;
    private boolean isLoading;
    private Handler mHandler;


    @Deprecated
    public ProView(Context context, Handler mHandler) {
        super(context);
        this.mHandler = mHandler;
        initView();
    }

    public ProView(FaceBaseActivity_1 context) {
        super(context);
        this.mHandler = context.getHandler();
        initView();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void initView() {
        mainView = View.inflate(context, R.layout.progresslinview, null);
        pro_ing_lin = (LinearLayout) mainView.findViewById(R.id.pro_ing_lin);
        loading = (TextView) mainView.findViewById(R.id.loading);
        pro_error_text = (TextView) mainView.findViewById(R.id.pro_error_text);
        pro_error_text.setVisibility(View.GONE);
    }

    public void gone_all() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                pro_ing_lin.setVisibility(View.GONE);
                pro_error_text.setVisibility(View.GONE);

            }
        });

    }

    public void setLoadingName(final String loadingStr) {
        isLoading = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                pro_ing_lin.setVisibility(View.VISIBLE);
                pro_error_text.setVisibility(View.GONE);
                loading.setText(loadingStr + "");
               
                Su.log("setLoadingName" + loadingStr);
            }
        });
    }

    public void setErrorInfo(final String Str) {
        isLoading = false;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                pro_ing_lin.setVisibility(View.GONE);
                pro_error_text.setVisibility(View.VISIBLE);
                pro_error_text.setTextColor(Color.RED);
                pro_error_text.setText(Str);
               
            }
        });
    }

    public void setOk(final String Str,
                      boolean is_close_Ok_text) {
        Su.log(Str);
        isLoading = false;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                pro_ing_lin.setVisibility(View.GONE);
                pro_error_text.setText(Str);
                pro_error_text.setVisibility(View.VISIBLE);
                pro_error_text.setTextColor(Color.GRAY);
               
            }
        });

        if (is_close_Ok_text) {
            if (mHandler != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pro_error_text.setVisibility(View.GONE);
                    }
                }, 3000);
            }
        }
    }


    public void appendOk(final String Str) {
        Su.log(Str);
        isLoading = false;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                pro_ing_lin.setVisibility(View.GONE);
                pro_error_text.append(Str + StringUtil.N);
                pro_error_text.setVisibility(View.VISIBLE);
                pro_error_text.setTextColor(Color.GRAY);

            }
        });


    }
}
