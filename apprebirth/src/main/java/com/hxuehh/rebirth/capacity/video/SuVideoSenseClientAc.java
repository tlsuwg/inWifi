package com.hxuehh.rebirth.capacity.video;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.faceAc.BaseDeviceCapacityAc_2;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrderForDeviceCap_4;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.UDPTCPkeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import io.vov.vitamio.LibsChecker;

/**
 * Created by suwg on 2015/9/11.
 */


public class SuVideoSenseClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.SuVideoSenseClientAc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
    }

    public void ShowBack(MidMessageBack_2 mMidMessageBack) {
        Object err = mMidMessageBack.getByKey(MidMessage.Key_ErrInfo);
        if (err != null) {
            mProView.setErrorInfo(err.toString() + "");
            return;
        }
//        byte bb[] = mMidMessageBack.getBytes();
//        File file = FileUtil.getFileExist(bb, SuApplication.getInstance().getCacheDir().getPath()+ "/get_photo", "getimage.jpg");
//        if(file!=null){
//            photo_image_view.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
//            photo_image_view.setImageURI(Uri.fromFile(file));
//        }

        if (mMidMessageBack.isOK()) {
            mProView.setOk("完成", true);

        } else {
            mProView.setErrorInfo("失败");
        }
    }

    SuVideoPlayBytesView mSuVideoPlayBytesView;
    CheckBox light_is_open_check_box;


    public void setMainView() {
        Button mbutton = new Button(getFaceContext());
        mbutton.setTag(2);
        mbutton.setText("End");
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSuVideoPlayBytesView.end();
                doChange((Integer) v.getTag());
            }
        });
        ViewKeys.addIntoLin(R.id.main_lin, mbutton, getFaceContext());

        View.inflate(this, R.layout.video_byte_lin, (ViewGroup) findViewById(R.id.main_lin));
        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.ProView_lin, mProView.getMainView(), this);
        mProView.gone_all();


        mSuVideoPlayBytesView = new SuVideoPlayBytesView(new FaceContextWrapImp(getFaceContext()), this.getViewKey());
        ((ViewGroup) findViewById(R.id.video_view)).addView(mSuVideoPlayBytesView.getMainView());
        light_is_open_check_box= (CheckBox) findViewById(R.id.light_is_open_check_box);
//        mSuVideoPlayBytesView.initCoreView(UDPTCPkeys.VideoUdpPortForGet);
    }

    public void onClick_doChange(View v) {
        doChange(1);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSuVideoPlayBytesView.initCoreView(UDPTCPkeys.VideoUdpPortForGet);
            }
        }, 300);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSuVideoPlayBytesView != null)
            mSuVideoPlayBytesView.end();
    }

    public void doChange(int changeKey) {

        if (mProView.isLoading()) {
            DialogUtil.showShortToast(getFaceContext(), "请稍后....");
            return;
        }

        mProView.setLoadingName("改变中...");
        ClientService_TCPLongLink_ mClientService_TCPLongLink_ = getLinkShow();
        if (mClientService_TCPLongLink_ == null) return;
        MidMessageOrder_2 midMessageOrder = getCmdForChange(changeKey);
        try {
            mClientService_TCPLongLink_.sendSyncAddCache(midMessageOrder);
        } catch (FaceException e) {
            e.printStackTrace();
            mProView.setErrorInfo(e.getMessage() + "");
        } catch (Exception e) {
            e.printStackTrace();
            mProView.setErrorInfo(e.getMessage() + "");
        }
    }

    public MidMessageOrder_2 getCmdForChange(int changeKey) {
        boolean is= light_is_open_check_box.isChecked();
        SuVideoSense.VideoParameter mDeviceCapacityParameter = null;

        if (changeKey == 1) {
            mDeviceCapacityParameter = new SuVideoSense.VideoParameter(SuVideoSense.VideoParameter.Type_show_live);
            mDeviceCapacityParameter.setTargetIP(DeviceUtil.getWifiIP());
            mDeviceCapacityParameter.setUdpPortForGet(UDPTCPkeys.VideoUdpPortForGet);
            mDeviceCapacityParameter.setIsLightOpen(is);
        } else {
            mDeviceCapacityParameter = new SuVideoSense.VideoParameter(SuVideoSense.VideoParameter.Type_End);
        }

        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        return mid;
    }


}
