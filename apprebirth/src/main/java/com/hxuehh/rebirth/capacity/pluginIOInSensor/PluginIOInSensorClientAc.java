package com.hxuehh.rebirth.capacity.pluginIOInSensor;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.capacity.pluginInfrareBodySensor.PluginInfrareBodySensor;
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
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/9/11.
 */


public class PluginIOInSensorClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.PluginIOInSensorClientAc;
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
        String names[] = new String[]{"查看当前状态", "清除日志","查看日志","最后日志","启用", "失效","out状态"};
        int keys[] = new int[]{
               PluginIOInSensor.PluginIOInSensorParameter.TypeGetStatus,
               PluginIOInSensor.PluginIOInSensorParameter.Type_ClearHis,
               PluginIOInSensor.PluginIOInSensorParameter.Type_ShowHis,
               PluginIOInSensor.PluginIOInSensorParameter.Type_ShowLastHis,
               PluginIOInSensor.PluginIOInSensorParameter.Type_SetDeviceCapacityBaseSenserUsed_1,
               PluginIOInSensor.PluginIOInSensorParameter.Type_SetDeviceCapacityBaseSenserUsed_0,
               PluginIOInSensor.PluginIOInSensorParameter.Type_GetDeviceOutBytes,
        };

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChange((Integer) v.getTag());
            }
        };

        mEditText = new EditText(this);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditText.setText("1");
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
        String info = mEditText.getEditableText().toString();
        int min = -1;
        if (!StringUtil.isEmpty(info)) {
            try {
                min = Integer.parseInt(info);
            } catch (Exception e) {
            }
        }
        DeviceCapacityInParameter mDeviceCapacityParameter = new PluginIOInSensor.PluginIOInSensorParameter(changeKey);
        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        mid.setTag(changeKey);
        return mid;
    }


}
