package com.hxuehh.rebirth.client.faceAc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.service.DeviceService_TCPLongLink_;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/10/14.
 */
public class Client_AccelerationSetting extends FaceBaseActivity_1 {
    @Override
    public int getViewKey() {
        return ViewKeys.Client_AccelerationSetting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        initView();
        addListeners();
    }

    TextView mLogTextView;

    @Override
    public void initView() {
        super.initView();
        initTitle();

        String names[] = new String[]{"启动", "关闭"};
        int keys[] = new int[]{2000, 2001};

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kk = (Integer) v.getTag();
                switch (kk) {
                    case 2000: {
                        SharedPreferencesUtils.putString(SharedPreferencesKeys.AccelerationSettingOpen, "1");
                        doChange(true);
                    }
                    break;
                    case 2001: {
                        SharedPreferencesUtils.putString(SharedPreferencesKeys.AccelerationSettingOpen, "0");
                        doChange(false);
                    }
                    break;

                }

            }
        };

        ScrollView mScrollView = new ScrollView(getFaceContext());
        LinearLayout lin = new LinearLayout(getFaceContext());
        lin.setOrientation(LinearLayout.VERTICAL);
        mScrollView.addView(lin);

        mLogTextView = new TextView(getFaceContext());
        lin.addView(mLogTextView);
        boolean is=!"0".equals(SharedPreferencesUtils.getString(SharedPreferencesKeys.AccelerationSettingOpen));
        showStatus(is);

        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            lin.addView(button1);
            button1.setOnClickListener(mOnClickListener);
        }
        ViewKeys.addIntoLin(R.id.main_lin, mScrollView, this);
    }

    private void doChange(boolean b) {
        showStatus(b);
        Intent in=new Intent(ClientService_TCPLongLink_.AccelerationSetting);
        in.putExtra("open",b);
        sendBroadcast(in);
    }

    private void showStatus(boolean is) {
        mLogTextView.setText(is?"已经打开":"没有打开");
    }

    private void initTitle() {
        TitleView mTitleView = new TitleView(this);
        mTitleView.setTitle("翻转驱动");
        mTitleView.addIntoView(this, R.id.title_lin);
    }

    @Override
    public void addListeners() {
        super.addListeners();
    }
}
