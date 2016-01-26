package com.hxuehh.rebirth.capacity.scene;

import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.domain.RunningStatus;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
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

import java.util.List;

/**
 * Created by suwg on 2015/9/11.
 */


public class SceneClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.SceneClientAc;
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
                if(kk== Scene.SceneParameter.Type_ShowHis){
                    List list = (List)info;
                    StringBuffer sb=new StringBuffer();
                    for (Object oode : list) {
                        try {
                            JsonElement mDeviceInfoJson = new Gson().toJsonTree(oode);
                            RunningStatus baseInfo = new Gson().fromJson(mDeviceInfoJson, RunningStatus.class);
                            sb.append(baseInfo.toStatusString()+StringUtil.N);
                        } catch (Exception e) {
                            e.printStackTrace();
                            mProView.setErrorInfo("获取到" + list.size() + ",展示出错");
                            break;
                        }
                    }
                    mProView.setOk(sb + "", false);
                    return;
                }else if(kk== Scene.SceneParameter.Type_ShowLastHis){
                    JsonElement mDeviceInfoJson = new Gson().toJsonTree(info);
                    RunningStatus baseInfo = new Gson().fromJson(mDeviceInfoJson, RunningStatus.class);
                    mProView.setOk(baseInfo.toStatusString() + "", false);
                    return;
                }
                mProView.setOk(info + "", false);
                return;
            }

            mProView.setOk("完成", true);
        } else {
            mProView.setErrorInfo("失败");
        }

    }

    EditText mEditText;
    int time;

    public void setMainView() {
        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.main_lin, mProView.getMainView(), this);
        mProView.gone_all();

        String names[] = new String[]{
                "模拟震动触发","获取运行状态及配置状态", "配置：传感延时时间", "配置：屏幕回执时间",
                "配置：屏幕有用","配置：屏幕无用",
                "配置：GPIO有用","配置：GPIO无用",
                "配置：序列化可用","配置：序列化无用",
                "配置：led可用","配置：led无用",
                "配置：语音播报可用","配置：语音播报无用",
                "查看历史全部","查看最近"
        };
        int keys[] = new int[]{
                Scene.SceneParameter.TypeCMDFrom,
                Scene.SceneParameter.Type_AskStatus,
                Scene.SceneParameter.TypeSetting_SensorKeepTime,Scene.SceneParameter.TypeSetting_ScreenKeepTime,
                1007,1001,
                1002,1003,
                1004,1005,
                1010,1011,
                1008,1009,
                Scene.SceneParameter.Type_ShowHis,Scene.SceneParameter.Type_ShowLastHis,
        };

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int kk=(Integer) v.getTag();
                if(kk==Scene.SceneParameter.TypeSetting_SensorKeepTime||
                        kk==Scene.SceneParameter.TypeSetting_ScreenKeepTime){
                    String info = mEditText.getEditableText().toString();
                    int min = -1;
                    if (!StringUtil.isEmpty(info)) {
                        try {
                            min=Integer.parseInt(info);
                        } catch (Exception e) {
                        }
                    }
                    if(min<=0){
                        DialogUtil.showLongToast(getFaceContext(),"必须设置大于0");
                        return ;
                    }
                    time=min*1000;
                }
                doChange(kk);
            }
        };

        mEditText = new EditText(this);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        ViewKeys.addIntoLin(R.id.main_lin, mEditText, this);


        ScrollView mScrollView=new ScrollView(getFaceContext());
        LinearLayout lin=new LinearLayout(getFaceContext());
        lin.setOrientation(LinearLayout.VERTICAL);
        mScrollView.addView(lin);

        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            lin.addView(button1);
            button1.setOnClickListener(mOnClickListener);
        }
        ViewKeys.addIntoLin(R.id.main_lin, mScrollView, this);
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


        Scene.SceneParameter mDeviceCapacityParameter=null;

        switch (changeKey){
            case Scene.SceneParameter.Type_AskStatus:{
                mDeviceCapacityParameter = new Scene.SceneParameter(changeKey, DeviceCapacityBase.Type_Client_AccelerationSensor);
            }
            break;
            case Scene.SceneParameter.TypeCMDFrom:{
                 mDeviceCapacityParameter = new Scene.SceneParameter(changeKey, DeviceCapacityBase.Type_Client_AccelerationSensor);
            }
            break;
            case Scene.SceneParameter.TypeSetting_SensorKeepTime:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_SensorKeepTime);
                mDeviceCapacityParameter.setKeepTime(time);
            }
            break;

            case Scene.SceneParameter.TypeSetting_ScreenKeepTime:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_ScreenKeepTime);
                mDeviceCapacityParameter.setKeepTime(time);
            }
            break;

            case  Scene.SceneParameter.Type_ShowHis:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.Type_ShowHis);
            }
            break;
            case  Scene.SceneParameter.Type_ShowLastHis:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.Type_ShowLastHis);
            }
            break;
            case 1007:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_Screen,true);
            }
            break;
            case 1001:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_Screen,false);
            }
            break;
            case 1002:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_USB_GPIO,true);
            }
            break;
            case 1003:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_USB_GPIO,false);
            }
            break;
            case 1004:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_USB_Serializ,true);
            }
            break;
            case 1005:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_USB_Serializ,false);
            }
            break;
            case 1008:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_VoiceSynthesis,true);
            }
            break;
            case 1009:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_VoiceSynthesis,false);
            }
            break;
            case 1010:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_FlashLight,true);
            }
            break;
            case 1011:{
                mDeviceCapacityParameter = new Scene.SceneParameter(Scene.SceneParameter.TypeSetting_USED, DeviceCapacityBase.Type_FlashLight,false);
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
