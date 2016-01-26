package com.hxuehh.appCore.faceFramework.faceUI.androidWrap;

import android.content.Intent;
import android.os.Bundle;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Peripheryable;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceEcxeption.ChangeToOtherActivityException;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;


/**
 * Created by suwg on 2014/8/13.
 * 这个是入口类，来自PUSH什么的，需要升级DB之类需要在启动页面完成，这里做一个判断
 *
 */

public abstract class FaceHitBaseActivity_2 extends FaceBaseActivity_1 {

    public String mPushId;
    protected Object obj;

    public boolean IMpush;

//    这个需要捕获  已经跳转
    @Override
    protected void onCreate(Bundle savedInstanceState) throws ChangeToOtherActivityException {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        if (in != null) {
            IMpush=in.getBooleanExtra("push",false);
            checkAppStatusChangeToOtherActivty(in);
        }
    }

    protected final void checkAppStatusChangeToOtherActivty(Intent in) throws ChangeToOtherActivityException {
        obj = in.getSerializableExtra(Peripheryable.KeyName);
        if (obj != null && obj instanceof Peripheryable) {
            Peripheryable mPeripheryable = (Peripheryable) obj;
            switch (mPeripheryable.getFrom()) {
                case Peripheryable.KeyPush: {
                    startAnalytic(obj);
                }
                break;
                case Peripheryable.KeyWidget: {
                    checkWidgetDBStatus();
                }
                break;
                case Peripheryable.KeyF: {
                }
                break;
                default:
            }
            checkVersionChange();
        }
    }

    private void startAnalytic(Object obj) {
//        if (obj instanceof PushMessage) {
//            PushMessage push = (PushMessage) obj;
//            mPushId = push.getId();
//            Analytics.onEvent(SuApplication.getInstance(), AnalyticsInfo.EVENT_PUSH_CLICK, "d:" + push.getId());
//            Analytics.flush();
//        }
    }

    private void checkVersionChange() throws ChangeToOtherActivityException {
        if (checkAppVison()) {
            changeChangeToOtherActivty();
            throw new ChangeToOtherActivityException("等待app初始化完毕");
        }
    }

    protected void checkWidgetDBStatus() {
        boolean is = SharedPreferencesUtils.getBoolean("this_current_app_vison");
        if (!is) {
            SharedPreferencesUtils.putBoolean("this_current_app_vison", true);
            SuApplication.getInstance().checkService();
        }
    }

    private boolean checkAppVison() {
        return SuApplication.getInstance().isNewVison();
    }

    private void changeChangeToOtherActivty() {//跳转首页 重新进行app初始化环节
//        this.startActivity(new Intent(this, SplashActivity.class));
        this.finish();
    }


    @Override
    public void onBackPressed() {
        if(IMpush){
//            MainActivity.invoke(this);
        }
        super.onBackPressed();
    }



}
