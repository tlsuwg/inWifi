package com.hxuehh.rebirth.capacity.voiceSynthesis;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class VoiceSynthesisPlay extends DeviceCapacityBase {

    @Override
    public boolean isShowStatus() {
        return false;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_VoiceSynthesis;
    }

    @Override
    public boolean testHardware_SDK() {
        return true;
    }

    @Override
    public boolean isDevEnable() {
        return true;
    }

    @Override
    public String getDevUnEnableInfo() {
        return null;
    }


    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {
        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if (!(t instanceof VoiceSynthesisParameter)) throw new FaceException("参数类型出错");
        final VoiceSynthesisParameter tt = (VoiceSynthesisParameter) t;
        if (!ismSpeechRecognizerInitOK) throw new FaceException("没有初始化完成");
        CommonDeviceCapacityOutResult mCommonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);

        switch (tt.getType()){
            case VoiceSynthesisParameter.Synthesis:{
                if (StringUtil.isEmpty(tt.info)) throw new FaceException("没有传递需要播放的文字");
                startOnce(tt.info);
            }
            break;
            case VoiceSynthesisParameter.Synthesis_setting:{
                voicer=tt.voc;
                SharedPreferencesUtils.putString(SharedPreferencesKeys.VoiceSynthesisVoicerName,voicer);
                startOnce("设置播报人" + tt.name);
            }
            break;
        }


        return mCommonDeviceCapacityOutResult;
    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Type_VoiceRecognition_his_max;
    }


    @Override
    public void activeReportOfEvent(Object[] f) {

    }


    @Override
    public void onCreat() {
        String name= SharedPreferencesUtils.getString(SharedPreferencesKeys.VoiceSynthesisVoicerName);
        if(!StringUtil.isEmpty(name)){
            this.voicer=name;
        }
        SpeechUtility.createUtility(SuApplication.getInstance(), "appid=56264687");
        mTts = SpeechSynthesizer.createSynthesizer(SuApplication.getInstance(), mInitListener);
    }

    @Override
    public void stop() {
        if (mTts != null) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }

    @Override
    public void onDestry() {
        stop();
    }

    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_voice_synthesis_ac, false);
    }



    //    =====================================================================
// 语音合成对象
    transient private SpeechSynthesizer mTts;
    // 语音听写对象
    transient boolean ismSpeechRecognizerInitOK;

    transient InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                ismSpeechRecognizerInitOK = true;
            }
        }
    };

    transient String voicer = "xiaoyan";

    private void setParam() {
        String mEngineType = SpeechConstant.TYPE_CLOUD;
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        //设置合成语速
        mTts.setParameter(SpeechConstant.SPEED, "50");
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        //设置合成音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, SuApplication.getInstance().getCacheDir().getAbsolutePath() + "/msc/tts.wav");
    }

    // 缓冲进度
    transient private int mPercentForBuffering = 0;
    // 播放进度
    transient private int mPercentForPlaying = 0;

    transient private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
           mAccelerationSensorLockLong();
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;

        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;

        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
            mAccelerationSensorUnLock100();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };


    public void startOnce(String text) throws FaceException {

        Su.log(text);
        if (!ismSpeechRecognizerInitOK) {
            this.onCreat();
        }
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            throw new FaceException("语音合成失败,错误码: " + code);
        }
    }


    private void showTip(String info) {
        Su.log(info);
    }


    @Face_UnsolvedForDlp
    public static class VoiceSynthesisParameter extends DeviceCapacityInParameter implements Serializable {
        public  static final int Synthesis=1;
        public  static final int Synthesis_setting=2;

        String info;//两个参数合一了  读的内容，设置的播报人
        public VoiceSynthesisParameter(int type, String info) {
            super(type);
            this.info = info;
        }

        String name,voc;
        public VoiceSynthesisParameter(int type, String name,String voc) {
            super(type);
            this.name = name;
            this.voc=voc;
        }
    }


}
