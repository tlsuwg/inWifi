package com.hxuehh.rebirth.capacity.brightness;

import android.view.View;
import android.widget.Button;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.capacity.location.Loc_Geographic;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.faceAc.BaseDeviceCapacityAc_2;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrderForDeviceCap_4;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/9/11.
 */


public class BrightnessClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.BrightnessClientAc;
    }

    public void ShowBack(MidMessageBack_2 mMidMessageBack) {
        Object key = mMidMessageBack.getTag();
        if (key == null) {
            mProView.setErrorInfo("关联出错");
        } else {
            Object err = mMidMessageBack.getByKey(MidMessage.Key_ErrInfo);
            if (err != null) {
                mProView.setErrorInfo(err.toString() + "");
                return;
            }
            int kk = (Integer) key;
            if (mMidMessageBack.isOK()) {
                mProView.setOk(" 完成  "+mMidMessageBack.getByKey(MidMessage.Key_Res), true);
            } else {
                mProView.setErrorInfo("失败");
            }
        }
    }


    public void setMainView() {
        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.main_lin, mProView.getMainView(), this);
        mProView.gone_all();

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doChange((Integer) v.getTag());
            }
        };

        String names[]=new String[]{"++","最亮","--","最暗","设置自动调节","设置手动调节"};
        int keys[]=new int[]{Brightness.BrightnessParameter.Type_addition,Brightness.BrightnessParameter.Type_addition_max,
                Brightness.BrightnessParameter.Type_subtraction,  Brightness.BrightnessParameter.Type_subtraction_mid,
                Brightness.BrightnessParameter.Type_AUTO,Brightness.BrightnessParameter.Type_UNAUTO};

        for(int i=0;i<names.length;i++){
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            ViewKeys.addIntoLin(R.id.main_lin, button1, this);
            button1.setOnClickListener(mOnClickListener);
        }
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
        if (midMessageOrder == null) {
            mProView.setErrorInfo("没有设置此类动作的消息");
            return;
        }

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

        DeviceCapacityInParameter mDeviceCapacityParameter = new Brightness.BrightnessParameter(changeKey);

        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setTag(changeKey);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        return mid;
    }


}
