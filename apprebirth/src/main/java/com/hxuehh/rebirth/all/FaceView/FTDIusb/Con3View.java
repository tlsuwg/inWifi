/**
 *
 */
package com.hxuehh.rebirth.all.FaceView.FTDIusb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.R;


/**
 * @author suwg
 * @data 2014-4-26
 */

@SuppressLint("NewApi")
public class Con3View extends LinearLayout {

    /**
     * @param context
     */
    public Con3View(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public Con3View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public Con3View(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }


    Con40DataItemView[] mCon32DataItemView_all = new Con40DataItemView[3];


    private void initView(Context context) {
        // TODO Auto-generated method stub
        this.inflate(context, R.layout.con_3_view, this);

        int i = 0;
        mCon32DataItemView_all[i++] = (Con40DataItemView) findViewById(R.id.con40_data_item_view_1);
        mCon32DataItemView_all[i++] = (Con40DataItemView) findViewById(R.id.con40_data_item_view_2);
        mCon32DataItemView_all[i++] = (Con40DataItemView) findViewById(R.id.con40_data_item_view_3);


    }


    FaceCommCallBack mSuCallBack;

    public FaceCommCallBack getmSuCallBack() {
        return mSuCallBack;
    }

    public void setmSuCallBack(FaceCommCallBack mSuCallBack) {
        this.mSuCallBack = mSuCallBack;
        for (Con40DataItemView mCon32DataItemView : mCon32DataItemView_all) {
            mCon32DataItemView.setmSuCallBack(mSuCallBack);
        }
    }


    public byte[] getDatas() {
        byte[] bys = new byte[mCon32DataItemView_all.length];
        for (int i = 0; i < mCon32DataItemView_all.length; i++) {
            bys[i] = mCon32DataItemView_all[i].getNumber();
        }
        String oostr = "";
        for (int i = 0; i <= bys.length - 1; i++) {
            oostr += bys[i];
        }
        Su.log("con40 view:" + oostr);
        return bys;
    }

    public void setDataInMainThread(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            if (bytes.length == 3) {
                for (int i = 0; i < bytes.length; i++) {
                    mCon32DataItemView_all[i].setNumber(bytes[i]);
                }
            }
        }
    }

}
