package com.hxuehh.rebirth.capacity.accelerationSensor;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
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
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/9/11.
 */


public class AccelerationSensorClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.AccelerationSensorClientAc;
    }

    public void ShowBack(MidMessageBack_2 mMidMessageBack) {
        Object err = mMidMessageBack.getByKey(MidMessage.Key_ErrInfo);
        if (err != null) {
            mProView.setErrorInfo(err.toString() + "");
            return;
        }
        int kk = (int) mMidMessageBack.getTag();
        if (mMidMessageBack.isOK()) {
            Object info = mMidMessageBack.getByKey(MidMessage.Key_Res);
            if (info != null) {
                mProView.setOk(info + "", false);
                return;
            }
            mProView.setOk("完成", true);
        } else {
            mProView.setErrorInfo("失败");
        }

    }

    EditText mEditText;

    public void setMainView() {
        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.main_lin, mProView.getMainView(), this);
        mProView.gone_all();

        String names[] = new String[]{"查看状态","设置灵敏度空间（乘以倍数）","设置X","设置Y","设置Z","初始化", "启用", "失效"};
        int keys[] = new int[]{AccelerationSensor.AccelerationSensorParameter.Type_AskStatus,
                AccelerationSensor.AccelerationSensorParameter.Type_Amplification,
                AccelerationSensor.AccelerationSensorParameter.Type_Setting_X,
                AccelerationSensor.AccelerationSensorParameter.Type_Setting_Y,
                AccelerationSensor.AccelerationSensorParameter.Type_Setting_Z,
                AccelerationSensor.AccelerationSensorParameter.Type_Setting_Init,
                AccelerationSensor.AccelerationSensorParameter.Type_SetDeviceCapacityBaseSenserUsed_1,
                AccelerationSensor.AccelerationSensorParameter.Type_SetDeviceCapacityBaseSenserUsed_0,
        };

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChange((Integer) v.getTag());
            }
        };

        mEditText = new EditText(this);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        ViewKeys.addIntoLin(R.id.main_lin, mEditText, this);


        for (int i = 0; i < names.length; i++) {
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

        MidMessageOrder_2 midMessageOrder = null;
        try {
            midMessageOrder = getCmdForChange(changeKey);
        } catch (FaceException e) {
            e.printStackTrace();
            mProView.setErrorInfo(e.getMessage());
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

    public MidMessageOrder_2 getCmdForChange(int changeKey) throws FaceException {

        int min = -1;
        String info = mEditText.getEditableText().toString();
        if (!StringUtil.isEmpty(info)) {
            try {
                min=Integer.parseInt(info);
            } catch (Exception e) {
            }
        }

        AccelerationSensor.AccelerationSensorParameter mDeviceCapacityParameter = new AccelerationSensor.AccelerationSensorParameter(changeKey);
        switch (changeKey){
            case AccelerationSensor.AccelerationSensorParameter.Type_Amplification:{
                if(min<=0)throw new FaceException("请设置数值，必须大于0");
                mDeviceCapacityParameter.setmAmplification(min);
            }
            break;
            case AccelerationSensor.AccelerationSensorParameter.Type_Setting_X:
            case AccelerationSensor.AccelerationSensorParameter.Type_Setting_Y:
            case AccelerationSensor.AccelerationSensorParameter.Type_Setting_Z:{
                if(min<=0)throw new FaceException("请设置数值，必须大于0");
                mDeviceCapacityParameter.setXyz(min);
            }
            break;
        }

        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        mid.setTag(changeKey);
        return mid;
    }


}
