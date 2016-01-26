package com.hxuehh.rebirth.capacity.record;

import android.media.MediaRecorder;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class Record extends DeviceCapacityBase {


    @Override
    public boolean isShowStatus() {
        return false;
    }

    @Override
    public int getType() {
        return DeviceCapacityBase.Type_RecordFile;
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
        if (!(t instanceof RecordParameter)) throw new FaceException("参数类型出错");
        final RecordParameter mVibrationP = (RecordParameter) t;
        CommonDeviceCapacityOutResult mCommonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        switch (mVibrationP.getType()) {
            case RecordParameter.Record_type_start: {
                if (isRecordering) throw new FaceException("已经在录音");
                mFaceException = null;
                ThreadManager.getInstance().getNewThread("Recording ", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            initializeAudio();
                            notifyThis();
                            newThreadToStop(mVibrationP.lastTime, mVibrationP);
                        } catch (IOException e) {
                            e.printStackTrace();
                            stopAudio();
                            mFaceException = new FaceException("录音出错" + e.getMessage());
                            notifyThis();
                        } catch (FaceException e) {
                            e.printStackTrace();
                            mFaceException = e;
                            notifyThis();
                        }
                    }
                }).start();

                try {
                    waitThis();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mFaceException != null) {
                    throw mFaceException;
                }
                mCommonDeviceCapacityOutResult.putKeyValue(MidMessage.Key_BackWarning, "正在录音");
                return mCommonDeviceCapacityOutResult;
            }

            case RecordParameter.Record_type_end: {
                if (isRecordering) {
                    stopAudio();
                } else {
                    throw new FaceException("没有启动录音或者已经到时停止");
                }
                return mCommonDeviceCapacityOutResult;
            }

            case RecordParameter.Record_type_end_send_last_back: {
                if (isRecordering) {
                    stopAudio();
                } else {
                    throw new FaceException("没有启动录音或者已经到时停止");
                }
                if (path == null) throw new FaceException("没有录音文件");
                try {
                    byte[] bs = FileUtil.getBytes(path);
                    mCommonDeviceCapacityOutResult.setBytes(bs);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new FaceException("获取文件内容出错");
                }
                return mCommonDeviceCapacityOutResult;
            }

        }


        return new CommonDeviceCapacityOutResult(true);
    }

    private void newThreadToStop(final long time, final RecordParameter mVibrationP) {
        ThreadManager.getInstance().getNewThread("ThreadToStopRecord", new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mVibrationP.isTheSameTag(getLastTagTime())) {
                    stop();
                }
            }
        }).start();

    }


    @Override
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.Type_Record_his_max;
    }


    @Override
    public void activeReportOfEvent(Object[] f) {

    }

    @Override
    public void onCreat() {

    }

    @Override
    public void stop() {
        stopAudio();
    }

    @Override
    public void onDestry() {
        stop();
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_record_ac, false);
    }

    @Face_UnsolvedForDlp
    public static class RecordParameter extends DeviceCapacityInParameter implements Serializable {

        long lastTime;
        public static final int Record_type_start = 1;
        public static final int Record_type_end = 2;
        public static final int Record_type_end_send_last_back = 3;

        @Override
        public String getTypeName() {
            if (getType() == Record_type_start) {
                return "开始录音";
            } else if (getType() == Record_type_end) {
                return "结束录音";
            } else if (getType() == Record_type_end_send_last_back) {
                return "结束并回传";
            }
            return super.getTypeName();
        }

        public RecordParameter(long lastTime, int type) {
            super(type);
            if (type == Record_type_start) {
                this.lastTime = lastTime;
            }
        }
    }

    //    ====================================
    transient MediaRecorder recorder;
    transient boolean isRecordering;

    transient String path;
    transient FaceException mFaceException;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isRecordering() {
        return isRecordering;
    }

    public void setIsRecordering(boolean isRecordering) {
        this.isRecordering = isRecordering;
    }

    public void initializeAudio() throws IOException, FaceException {
        if (isRecordering) throw new FaceException("正在录音");
        recorder = new MediaRecorder();// new出MediaRecorder对象
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        // 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置MediaRecorder录制音频的编码为amr.
        File file = FileUtil.getFileExist(SuApplication.getInstance().getCacheDir().getPath() + "/amr", DateUtil.getCurrentTimeForname() + ".amr");
        path = file.getAbsolutePath();
        recorder.setOutputFile(path);
        recorder.prepare();// 准备录制
        recorder.start();// 开始录制
        isRecordering = true;

    }

    public void stopAudio() {
        if (recorder != null) {
            try {
                recorder.stop();// 停止刻录
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recorder.release(); // 刻录完成一定要释放资源
                isRecordering = false;
                recorder = null;
            }
        }
    }

}
