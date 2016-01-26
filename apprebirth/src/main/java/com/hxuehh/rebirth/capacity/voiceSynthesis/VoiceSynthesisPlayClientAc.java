package com.hxuehh.rebirth.capacity.voiceSynthesis;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
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
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/9/11.
 */


public class VoiceSynthesisPlayClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.VoiceSynthesisClientAc;
    }

    String VoicerName[],voicer_cloud_values[];

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
                mProView.setOk(" 完成  " + mMidMessageBack.getByKey(MidMessage.Key_Res), false);

            } else {
                mProView.setErrorInfo("失败");
            }
        }
    }
    EditText mEditText;
    String infoHis;

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

        mEditText=new EditText(this);
        infoHis=SharedPreferencesUtils.getString(SharedPreferencesKeys.VoiceSynthesisHistory);
        if(StringUtil.isEmpty(infoHis)){
            mEditText.setText("美媒披露习近平访美行程");
        }else{
            mEditText.setText(infoHis);
        }


        ViewKeys.addIntoLin(R.id.main_lin, mEditText, this);

        VoicerName= getResources().getStringArray(R.array.voicer_cloud_entries);
        voicer_cloud_values=getResources().getStringArray(R.array.voicer_cloud_values);
        String names[] = new String[VoicerName.length+1];
        names[0]="发送";
        System.arraycopy(VoicerName,0,names,1,VoicerName.length);


        ScrollView mScrollView = new ScrollView(getFaceContext());
        LinearLayout lin = new LinearLayout(getFaceContext());
        lin.setOrientation(LinearLayout.VERTICAL);
        mScrollView.addView(lin);

        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(2000+i);
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

        MidMessageOrder_2 midMessageOrder = null;
        try {
            midMessageOrder = getCmdForChange(changeKey);
        } catch (FaceException e) {
            e.printStackTrace();
           DialogUtil.showLongToast(this,e.getMessage());
            return;
        }

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

    public MidMessageOrder_2 getCmdForChange(int changeKey) throws FaceException {


        DeviceCapacityInParameter mDeviceCapacityParameter=null;
        if(changeKey==2000) {
            String info = mEditText.getEditableText().toString();
            if (StringUtil.isEmpty(info)) {
                throw new FaceException("请输入文字");
            }
            if (!info.equals(infoHis)) {
                SharedPreferencesUtils.putString(SharedPreferencesKeys.VoiceSynthesisHistory, info);
                infoHis = info;
            }
            mDeviceCapacityParameter = new VoiceSynthesisPlay.VoiceSynthesisParameter(VoiceSynthesisPlay.VoiceSynthesisParameter.Synthesis,infoHis);
        }else {
            mDeviceCapacityParameter = new VoiceSynthesisPlay.VoiceSynthesisParameter(VoiceSynthesisPlay.VoiceSynthesisParameter.Synthesis_setting,VoicerName[changeKey-2000-1],voicer_cloud_values[changeKey-2000-1]);
        }

        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setTag(changeKey);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        return mid;
    }


}
