package com.hxuehh.rebirth.all.FaceAc;

import android.content.Intent;
import android.os.Bundle;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.ChangeToOtherActivityException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceHitBaseActivity_2;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;


/**
 * Created by suwg on 2015/8/13.
 */
public class InitAc extends FaceHitBaseActivity_2 {

    public static final String ConfigInitOK = "com.su.ConfigInitOK";

    private String actions[] = new String[]{ConfigInitOK};

    FaceCommCallBack FaceCommCallBackConfigInitOK = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            Intent in = (Intent) t[0];

            if(AppStaticSetting.isSunShaoQingPro){//如果是孙绍清
                IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_mi_test, true);
                return false;
            }

            int key = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.device_used_type);
            switch (key) {
                case SharedPreferencesKeys.device_used_type_noSet:
                    IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_used_type_noSet, true);
                    break;
                case SharedPreferencesKeys.device_used_type_device:
                    IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_only, true);
                    break;
                case SharedPreferencesKeys.device_used_type_only_server:
                    IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_server_only, true);
                    break;
                case SharedPreferencesKeys.device_used_type_server_Device:
                    if (IntentChangeManger.isClient_Dev_Main()) {
                        DialogUtil.showShortToast(SuApplication.getInstance(),"2 OK，做3体,请选择客户端，运行");
                        if(IntentChangeManger.isClientServiceStart()){
                            IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_client_only, true);
                        }else{
                            IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_used_type_noSet, true);
                        }
                    } else {
                        IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_server_Device, true);
                    }
                    break;
                case SharedPreferencesKeys.device_used_type_client:
                    IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_client_only, false);
                    break;
            }

            return false;
        }
    };



    private FaceCommCallBack faceCommCallBacks[] = new FaceCommCallBack[]{FaceCommCallBackConfigInitOK};


    @Override
    public int getViewKey() {
        return 0;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) throws ChangeToOtherActivityException {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_lin);
        setActionReceivers(actions, faceCommCallBacks);
        initView();
        addListeners();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getFaceContext().sendBroadcast(new Intent(ConfigInitOK));//加载配置完成
            }
        }, 3000);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void initView() {
        super.initView();
        TitleView mTitleView = new TitleView(this);
            mTitleView.setTitle("初始化");
        mTitleView.addIntoView(this, R.id.title_lin);
        ProView mPro=new ProView(getFaceContext());
        mPro.setLoadingName("");
        ViewKeys.addIntoLin(R.id.main_lin,mPro.getMainView(),this);
    }

    @Override
    public void addListeners() {
        super.addListeners();
    }

    @Override
    public void setOnTop(boolean isOnTop) {
        super.setOnTop(isOnTop);
    }

    @Override
    public void setViewKey(int key) {
        super.setViewKey(key);
    }
}
