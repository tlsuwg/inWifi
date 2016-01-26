package com.hxuehh.rebirth.client.faceAc;

import com.hxuehh.rebirth.R;
import android.os.Bundle;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceEcxeption.ChangeToOtherActivityException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceHitBaseActivity_2;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.client.FaceView.SinitCapsShow;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/9/11.
 */
public class ShowDeviceCapacityAc extends FaceHitBaseActivity_2 {


    @Override
    public int getViewKey() {
        return ViewKeys.ShowDeviceCapacityAc;
    }

   private DeviceInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws ChangeToOtherActivityException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        try {
            BytesClassAidl mBytesClassAidl = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.Provisionality, BytesClassAidl.To_Me);
            if (mBytesClassAidl != null) {
                Object oo = mBytesClassAidl.getTrue();
                if (oo != null) {
                    info = (DeviceInfo) oo;
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (info == null) {
            DialogUtil.showShortToast(SuApplication.getInstance(), "没有参数，退回");
            finish();
        }
        initView();
    }

    private void initTitle() {
        TitleView title = new TitleView(getFaceContext());
        title.setTitle(info.getShowInfo());
        ViewKeys.addIntoLin(R.id.title_lin, title.getMainView(), getFaceContext());
    }

    @Override
    public void initView() {
        super.initView();
        initTitle();
        getAllCaps();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSinitCapsShow.onResume();
    }

    private SinitCapsShow mSinitCapsShow;

    private void getAllCaps() {
        if (mSinitCapsShow == null) {
            mSinitCapsShow = new SinitCapsShow(new FaceContextWrapImp(getFaceContext()), getViewKey(),info);
            mSinitCapsShow.addIntoView(getFaceContext(), R.id.main_lin);
        }
        mSinitCapsShow.setLoadingView("获取设备信息...");
    }

}
