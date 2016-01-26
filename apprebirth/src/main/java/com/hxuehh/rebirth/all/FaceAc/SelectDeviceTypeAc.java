package com.hxuehh.rebirth.all.FaceAc;

import com.hxuehh.rebirth.R;
import android.os.Bundle;
import android.view.View;

import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;

/**
 * Created by suwg on 2015/8/13.
 */
public class SelectDeviceTypeAc extends FaceBaseActivity_1 implements View.OnClickListener{


    @Override
    public int getViewKey() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device_type);
        initView();
        addListeners();
    }

    @Override
    public void initView() {
        super.initView();
        new TitleView(getFaceContext()).setTitle("设备类型设置").addIntoView(getFaceContext(),R.id.title_lin);
    }

    @Override
    public void addListeners() {
        super.addListeners();
        findViewById(R.id.be_server).setOnClickListener(this);
        findViewById(R.id.be_client).setOnClickListener(this);
        findViewById(R.id.understand).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.be_client:{
                IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_select_client_init, true);
            }
            break;
            case R.id.be_server:{
                IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_select_serverordevice_init, true);
            }
            break;
            case R.id.understand:{
//                IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_test_usb_dev, true);
            }
            break;
        }

    }
}
