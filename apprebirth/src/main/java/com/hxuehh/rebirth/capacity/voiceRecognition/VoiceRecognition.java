package com.hxuehh.rebirth.capacity.voiceRecognition;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.capacity.voiceSynthesis.VoiceSynthesisPlay;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by suwg on 2015/8/15.
 */
//识别
public class VoiceRecognition extends DeviceCapacityBase {

    transient USBSerializ mUSBSerializ;
    transient VoiceSynthesisPlay mVoiceSynthesis;

    public VoiceRecognition(USBSerializ mUSBSerializ, VoiceSynthesisPlay mVoiceSynthesis) {
        this.mUSBSerializ = mUSBSerializ;
        this.mVoiceSynthesis = mVoiceSynthesis;
    }

    transient FaceCommCallBack faceCommCallBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            try {
                dochars(t);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return false;
        }
    };

    private void dochars(Object[] t) throws FaceException {
        String info = (String) t[0];
        if (!StringUtil.isEmpty(info)) {
            if (info.contains("全部")) {
                if (info.contains("开")) {
                    mUSBSerializ.doChangeStatus(new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_ON));
                } else if (info.contains("关")) {
                    mUSBSerializ.doChangeStatus(new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_OFF));
                }
            } else if (info.contains("加")) {
                String num = info.replace("添加", "");
                 num = info.replace("增加", "");
                try {
                    byte [] setIO=getBytesByIndex(num);
                        mUSBSerializ.doChangeStatus(new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_Set40IO, setIO));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    byte [] setIO=getBytesByIndex(info);
                    mUSBSerializ.doChangeStatus(new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_Set40IO, setIO));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] getBytesByIndex(String info) {
        int index = Integer.parseInt(info);
        byte[] bsio = mUSBSerializ.getReadBytes().clone();
        bsio[0]=0x22;
        if (index > 0 || index <= 32) {
            byte kk= (byte) (1<<(index%8));
            bsio[index / 8 + 1] |= kk;
        }
        return bsio;
    }

    @Override
    public boolean isShowStatus() {
        return false;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_VoiceRecognition;
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
        if ( !(t instanceof VoiceRecognitionParameter)) throw new FaceException("参数类型出错");
            final VoiceRecognitionParameter mVibrationP = (VoiceRecognitionParameter) t;
            if (!ismSpeechRecognizerInitOK) throw new FaceException("没有初始化完成");
            CommonDeviceCapacityOutResult mCommonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
            mFaceException = null;
            result = null;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        startOnce();
                    } catch (FaceException e) {
                        e.printStackTrace();
                        mFaceException = e;
                        notifyThis();
                    }
                }
            }, 100);

            try {
                waitThis();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mFaceException != null) {
                throw mFaceException;
            }
            mCommonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_Res, result);
            ThreadManager.getInstance().getNewThread("语音执行", new Runnable() {
                @Override
                public void run() {
                    if (faceCommCallBack != null) faceCommCallBack.callBack(result);
                }
            }).start();

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
        SpeechUtility.createUtility(SuApplication.getInstance(), "appid=55753b73");
        mSpeechRecognizer = SpeechRecognizer.createRecognizer(SuApplication.getInstance(), mInitListener);
    }

    @Override
    public void stop() {

    }

    @Override
    public void onDestry() {
        stop();
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_voice_recognition_ac, false);
    }

    @Face_UnsolvedForDlp
    public static class VoiceRecognitionParameter extends DeviceCapacityInParameter implements Serializable {
        public VoiceRecognitionParameter() {
        }
    }

    //    =====================================================================

    transient Handler mHandler = new Handler(Looper.getMainLooper());
    transient FaceException mFaceException;
    // 语音听写对象
    transient SpeechRecognizer mSpeechRecognizer;
    transient boolean ismSpeechRecognizerInitOK;
    transient String result;
    transient InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                ismSpeechRecognizerInitOK = true;

            }
        }
    };

    public void setParam() {
        // 清空参数
        mSpeechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mSpeechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        // 设置返回结果格式
        mSpeechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = SharedPreferencesUtils.getString("iat_language_preference"
        );
        if (StringUtil.isEmpty(lag)) {
            lag = "mandarin";
        }
        if (lag.equals("en_us")) {
            // 设置语言
            mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mSpeechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mSpeechRecognizer.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, SuApplication.getInstance().getCacheDir().getAbsolutePath() + "/msc/iat.wav");
//        mSpeechRecognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
    }

    transient RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
            mFaceException = new FaceException(error.getMessage());
            notifyThis();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            result = printResult(results);
            if (isLast) {

            }
            notifyThis();
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume + "返回音频数据：" + data.length);
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


    private void startOnce() throws FaceException {
        mSpeechRecognizerResults.clear();
        setParam();
        int ret = mSpeechRecognizer.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            throw new FaceException("识别失败,错误码：" + ret);
        }

    }

    transient private HashMap<String, String> mSpeechRecognizerResults = new LinkedHashMap<String, String>();

    private String printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSpeechRecognizerResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mSpeechRecognizerResults.keySet()) {
            resultBuffer.append(mSpeechRecognizerResults.get(key));
        }

        String info = resultBuffer.toString();
        showTip(resultBuffer.toString());
        return info;

    }


    private void showTip(String info) {
        Su.log(info);
    }


}
