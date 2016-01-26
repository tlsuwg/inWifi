package com.hxuehh.rebirth.capacity.mediaPlayer;

import android.view.View;
import android.widget.Button;

import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.capacity.brightness.Brightness;
import com.hxuehh.rebirth.capacity.record.Record;
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
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.io.IOException;

/**
 * Created by suwg on 2015/9/11.
 */


public class SUMediaPlayerClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.SUMediaPlayerClientAc;
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


    boolean isLight;

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
        String names[] = new String[]{"远程播放测试固定文件", "暂停", "重新启动","跳跃到30", "停止","音乐标志位"};
        int keys[] = new int[]{
                SUMediaPlayer.MediaPlayerParameter.TypePlay_Bytes,
                SUMediaPlayer.MediaPlayerParameter.TypePlay_Pause,
                SUMediaPlayer.MediaPlayerParameter.TypePlay_ReStart,
                SUMediaPlayer.MediaPlayerParameter.TypePlay_SeekTo,
                SUMediaPlayer.MediaPlayerParameter.TypePlay_Stop,
//                SUMediaPlayer.MediaPlayerParameter.T,
//                SUMediaPlayer.MediaPlayerParameter.,
//                SUMediaPlayer.MediaPlayerParameter.,
//                SUMediaPlayer.MediaPlayerParameter.,
//                SUMediaPlayer.MediaPlayerParameter.,
//                SUMediaPlayer.MediaPlayerParameter.,
//                SUMediaPlayer.MediaPlayerParameter.,
                SUMediaPlayer.MediaPlayerParameter.TypePlay_GetHis_one_Rebirth,
        };
        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            ViewKeys.addIntoLin(R.id.main_lin, button1, this);
            button1.setOnClickListener(mOnClickListener);
        }

        final Button button1 = new Button(this);
        button1.setText("本地录音并发送");

        ViewKeys.addIntoLin(R.id.main_lin, button1, this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecord == null)
                    mRecord = new Record();
                if (mRecord.isRecordering()) {
                    mProView.setOk("正在发送", false);
                    mRecord.stopAudio();
                    button1.setText("本地录音并发送");
                    doChange(100);
                } else {
                    button1.setText("停止并发送");
                    try {
                        mProView.setLoadingName("正在录音,不限时长的啊");
                        mRecord.initializeAudio();
                    } catch (IOException e) {
                        e.printStackTrace();
                        mProView.setErrorInfo(e.getMessage());
                    } catch (FaceException e) {
                        e.printStackTrace();
                        mProView.setErrorInfo(e.getMessage());
                    }
                }

            }
        });

    }

    Record mRecord;


    public void doChange(int changeKey) {
        if (mProView.isLoading()) {
            DialogUtil.showShortToast(getFaceContext(), "请稍后....");
            return;
        }
        mProView.setLoadingName("改变中...");
        ClientService_TCPLongLink_ mClientService_TCPLongLink_ = getLinkShow();
        if (mClientService_TCPLongLink_ == null) return;
        try {
            MidMessageOrder_2 midMessageOrder = getCmdForChange(changeKey);
            midMessageOrder.setTag(changeKey);
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
        DeviceCapacityInParameter mDeviceCapacityParameter = null;
        switch (changeKey) {
            case SUMediaPlayer.MediaPlayerParameter.TypePlay_Bytes: {
                mDeviceCapacityParameter = sendBytesToPlay("data/data/com.hxuehh.rebirth/cache/get_amr/getamr.amr", SUMediaPlayer.MediaPlayerParameter.TypePlay_Bytes);
            }
            break;
            case 100: {
                mDeviceCapacityParameter = sendBytesToPlay( mRecord.getPath(), SUMediaPlayer.MediaPlayerParameter.TypePlay_Bytes);
            }
            break;
            case SUMediaPlayer.MediaPlayerParameter.TypePlay_Pause: {
                mDeviceCapacityParameter = new SUMediaPlayer.MediaPlayerParameter(changeKey);
            }
            break;
            case SUMediaPlayer.MediaPlayerParameter.TypePlay_ReStart: {
                mDeviceCapacityParameter = new SUMediaPlayer.MediaPlayerParameter(changeKey);
            }
            break;
            case SUMediaPlayer.MediaPlayerParameter.TypePlay_SeekTo: {
                mDeviceCapacityParameter = new SUMediaPlayer.MediaPlayerParameter(changeKey, 30);
            }
            break;
            case SUMediaPlayer.MediaPlayerParameter.TypePlay_Stop: {
                mDeviceCapacityParameter = new SUMediaPlayer.MediaPlayerParameter(changeKey);
            }
            break;
            case SUMediaPlayer.MediaPlayerParameter.TypePlay_GetHis_one_Rebirth: {
                mDeviceCapacityParameter = new SUMediaPlayer.MediaPlayerParameter(changeKey);
            }
            break;

        }


        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        return mid;
    }

    private SUMediaPlayer.MediaPlayerParameter sendBytesToPlay(String path, int changeKey) throws FaceException {
        try {
            byte[] bs = FileUtil.getBytes(path);
            return new SUMediaPlayer.MediaPlayerParameter(changeKey, "getamr.amr", bs);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FaceException("获取文件出错");
        }
    }


}
