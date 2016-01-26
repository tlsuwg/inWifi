/**
 *
 */
package com.hxuehh.rebirth.all.FaceView.FTDIusb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.rebirth.R;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;


/**
 * @author suwg
 * @data 2014-4-26
 */

@SuppressLint("NewApi")
public class ConNView extends LinearLayout {

    Con40DataItemView[] mCon32DataItemView_all;
    BeCon40DataItemView[] mBeCon8DataItemView_all;

    int allcon;
    int allbe;
    boolean isVertical;
    int numberInOnelLine;

    public ConNView(Context context, int allcon, int allbe,int numberInOnelLine,boolean isVertical) {
        super(context);
        this.allcon = allcon;
        this.allbe = allbe;
        this.numberInOnelLine=numberInOnelLine;
        this.isVertical=isVertical;
        mCon32DataItemView_all = new Con40DataItemView[allcon];
        mBeCon8DataItemView_all = new BeCon40DataItemView[allbe];
        initView(context);
    }

    LinearLayout con_main_lin, con_be_main_lin;
    TextView con_text, con_be_text;

    private void initView(Context context) {
        // TODO Auto-generated method stub
        this.inflate(context, R.layout.con_n_view, this);

        con_main_lin = (LinearLayout) findViewById(R.id.con_main_lin);
        con_be_main_lin = (LinearLayout) findViewById(R.id.con_be_main_lin);
        con_text = (TextView) findViewById(R.id.con_text);
        con_be_text = (TextView) findViewById(R.id.con_be_text);

        con_text.setText(this.allcon + "out");
        con_be_text.setText(this.allbe + "in");

        LayoutParams mLayoutParams=   new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT, 1);

        int alllie = allcon / numberInOnelLine;
        int all=0;
        for(int i=0;i<alllie;i++) {
            LinearLayout lin = new LinearLayout(getContext());
            for(int k=0;k<numberInOnelLine;k++) {
                Con40DataItemView mCon40DataItemView = new Con40DataItemView(getContext());
                mCon32DataItemView_all[all++]=mCon40DataItemView;
                mCon40DataItemView.setLayoutParams(mLayoutParams);
                lin.addView(mCon40DataItemView);
            }
            con_main_lin.addView(lin);
        }


         alllie = allbe / numberInOnelLine;
         all=0;
        for(int i=0;i<alllie;i++) {
            LinearLayout lin = new LinearLayout(getContext());
            for(int k=0;k<numberInOnelLine;k++) {
                BeCon40DataItemView mBeCon40DataItemView = new BeCon40DataItemView(getContext());
                mBeCon8DataItemView_all[all++]=mBeCon40DataItemView;
                mBeCon40DataItemView.setLayoutParams(mLayoutParams);
                lin.addView(mBeCon40DataItemView);
            }
            con_be_main_lin.addView(lin);
        }





//        View views[] = new View[alllie];
//        for (int k = 0; k < alllie; k++) {
//            View view = View.inflate(getContext(), R.layout.con_4_item_view, null);
//            con_main_lin.addView(view);
//            views[k] = view;
//        }
//
//        int i = 0;
//        if(!isVertical) {
//            int time=0;
//            int sizenow=0;
//            for (View view : views) {
//                boolean isou=(time%2==0);
//                if(isou){
//                    sizenow=8*(time/2+1);
//                }
//                    mCon32DataItemView_all[isou?sizenow-1:sizenow-4-1] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_1);
//                    mCon32DataItemView_all[isou?sizenow-2:sizenow-4-2] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_2);
//                    mCon32DataItemView_all[isou?sizenow-3:sizenow-4-3] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_3);
//                    mCon32DataItemView_all[isou?sizenow-4:sizenow-4-4] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_4);
//                time++;
//            }
//        }else {
//            for (View view : views) {
//                mCon32DataItemView_all[i++] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_1);
//            }
//            for (View view : views) {
//                mCon32DataItemView_all[i++] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_2);
//            }
//            for (View view : views) {
//                mCon32DataItemView_all[i++] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_3);
//            }
//            for (View view : views) {
//                mCon32DataItemView_all[i++] = (Con40DataItemView) view.findViewById(R.id.con40_data_item_view_4);
//            }
//        }
//
//
//        for (int k = 0; k < allbe / 4; k++) {
//            View view = View.inflate(getContext(), R.layout.con_be_4_item_view, null);
//            con_be_main_lin.addView(view);
//            mBeCon8DataItemView_all[k * 4 + 0] = (BeCon40DataItemView) view.findViewById(R.id.be_con40_data_item_view_1);
//            mBeCon8DataItemView_all[k * 4 + 1] = (BeCon40DataItemView) view.findViewById(R.id.be_con40_data_item_view_2);
//            mBeCon8DataItemView_all[k * 4 + 2] = (BeCon40DataItemView) view.findViewById(R.id.be_con40_data_item_view_3);
//            mBeCon8DataItemView_all[k * 4 + 3] = (BeCon40DataItemView) view.findViewById(R.id.be_con40_data_item_view_4);
//        }
    }


    FaceCommCallBack mSuCallBack;

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

    public byte[] getDatasBinary() {
        byte[] bys = new byte[mCon32DataItemView_all.length];
        for (int i = 0; i < mCon32DataItemView_all.length; i++) {
            int index_8=i/8;
            int left=i%8;
            bys[i] = mCon32DataItemView_all[index_8*8+(7-left)].getNumber();
        }

        if(AppStaticSetting.isTest) {//log
            String oostr = "";
            for (int i = 0; i <= bys.length - 1; i++) {
                oostr += bys[i];
            }
            Su.log("con40 view:" + oostr);
        }
        return bys;
    }


    public void setDataInMainThread(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            if (bytes.length == allcon + allbe) {
                byte[] by8 = new byte[allbe];
                System.arraycopy(bytes, 0, by8, 0, allbe);
                setDataInMainThread(by8);
                byte[] by32 = new byte[allcon];
                System.arraycopy(bytes, allbe, by32, 0, allcon);
                setDataInMainThread(by32);
            } else if (bytes.length == allbe) {
                for (int i = 0; i < bytes.length; i++) {
                    mBeCon8DataItemView_all[i].setNumber(bytes[i]);
                }
            } else if (bytes.length == allcon) {
                for (int i = 0; i < bytes.length; i++) {
                    mCon32DataItemView_all[i].setNumber(bytes[i]);
                }
            }
        }

    }

}
