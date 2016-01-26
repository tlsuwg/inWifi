package com.hxuehh.rebirth.capacity.vibration;

import com.hxuehh.rebirth.R;
import android.view.View;
import android.widget.Button;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.faceAc.BaseDeviceCapacityAc_2;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrderForDeviceCap_4;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;

/**
 * Created by suwg on 2015/9/11.
 */


public class VibrationClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.VibrationAc;
    }

    public void ShowBack(MidMessageBack_2 mMidMessageBack) {
        Object err = mMidMessageBack.getByKey(MidMessage.Key_ErrInfo);
        if (err != null) {
            mProView.setErrorInfo(err.toString() + "");
            return;
        }

        if (mMidMessageBack.isOK()) {
            mProView.setOk("完成", true);
        } else {
            mProView.setErrorInfo("失败");
        }

    }


    public void setMainView() {
        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.main_lin, mProView.getMainView(), this);
        mProView.gone_all();

        Button button1 = new Button(this);
        button1.setText("震动2000毫秒");
        ViewKeys.addIntoLin(R.id.main_lin, button1, this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChange(0);
            }
        });

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
        DeviceCapacityInParameter mDeviceCapacityParameter = new Vibration.VibrationParameter(2000);
        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        return mid;
    }


}
