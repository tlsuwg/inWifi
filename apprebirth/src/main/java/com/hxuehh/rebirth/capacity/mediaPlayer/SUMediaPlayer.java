package com.hxuehh.rebirth.capacity.mediaPlayer;

import android.media.MediaPlayer;
import android.os.RemoteException;
import android.view.View;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceDomain.utilDomain.LoadCursorSetting;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.domain.DeviceCapacityOutResult_3;
import com.hxuehh.rebirth.device.domain.imp.CommonDeviceCapacityOutResult;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.DeviceCapacitySetting;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by suwg on 2015/8/15.
 */
public class SUMediaPlayer extends DeviceCapacityBase {


    @Override
    public int getType() {
        return DeviceCapacityBase.Type_MediaPlayer;
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
    public int getMAXHistorySize() {
        return DeviceCapacitySetting.MediaPlayer_his_max;
    }

    @Override
    public void activeReportOfEvent(Object[] f) throws FaceException {

    }

    transient MediaPlayer mp;

    @Override
    public void onCreat() {

    }

    @Override
    public void stop() {
        if (mp != null)
            mp.stop();
    }

    @Override
    public void onDestry() {
        stop();
        if (mp != null)
            mp.release();
    }

    @Override
    public DeviceCapacityOutResult_3 doChangeStatus(DeviceCapacityInParameter t) throws FaceException {

        DeviceCapacityOutResult_3 mDeviceCapacityOutResult_3=   super.doChangeStatus(t); if(mDeviceCapacityOutResult_3!=null)return mDeviceCapacityOutResult_3;
        if (!(t instanceof MediaPlayerParameter)) throw new FaceException("参数类型出错");

        final MediaPlayerParameter tt = (MediaPlayerParameter) t;

        CommonDeviceCapacityOutResult commonDeviceCapacityOutResult = new CommonDeviceCapacityOutResult(true);
        switch (tt.getType()) {
            case MediaPlayerParameter.TypePlay_Bytes: {
                byte bs[] = tt.bs;
                if (bs == null || bs.length == 0)
                    throw new FaceException("没有传递音频数据");
                if (StringUtil.isEmpty(tt.fileName))
                    throw new FaceException("没有传递音频文件配置");
                final File file = FileUtil.getFileExist(bs, SuApplication.getInstance().getCacheDir().getPath() + "/get_media", tt.fileName);

                ThreadManager.getInstance().getNewThread("playMediaPlayer", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            playMediaPlayer(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mLightSensorUnLock100();
                        }
                    }
                }).start();

            }
            break;
            case MediaPlayerParameter.TypePlay_Bytes_add: {
                if (true)
                    throw new FaceException("没有实现");
            }
            break;
            case MediaPlayerParameter.TypePlay_His: {
                if (true)
                    throw new FaceException("没有实现");
            }
            break;
            case MediaPlayerParameter.TypePlay_GetHis: {
                if (true)
                    throw new FaceException("没有实现");
            }
            break;
            case MediaPlayerParameter.TypePlay_Stop: {
                stopMediaPlayer();
            }
            break;

            case MediaPlayerParameter.TypePlay_Pause: {
                pauseMediaPlayer();
            }
            break;
            case MediaPlayerParameter.TypePlay_ReStart: {
                if (mp != null) mp.start();
                mAccelerationSensorLockLong();
            }
            break;
            case MediaPlayerParameter.TypePlay_SeekTo: {
                if (tt.seekTo < 0) throw new FaceException("进度大于必须0");
                if (mp != null && mp.isPlaying()) {
                    mp.seekTo(tt.seekTo);
                }
                mAccelerationSensorLockLong();
            }
            break;
            case MediaPlayerParameter.TypePlay_GetHis_one: {
                if (true)
                    throw new FaceException("没有实现");
            }
            break;
            case MediaPlayerParameter.TypePlay_His_Add: {
                if (true)
                    throw new FaceException("没有实现");
            }
            break;
            case MediaPlayerParameter.TypePlay_GetHis_one_Rebirth: {
                 File file = new File("mnt/sdcard2/rebirth.mp3");
                if(!file.exists()) {
                    file = new File("mnt/sdcard/rebirth.mp3");
                }
                if (!file.exists()) throw new FaceException("文件不存在");
                final File file1=file;
                ThreadManager.getInstance().getNewThread("playMediaPlayer", new Runnable() {
                    @Override
                    public void run() {
                        try {
                            playMediaPlayer(file1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mLightSensorUnLock100();
                        }
                    }
                }).start();
            }
            break;
        }

        return commonDeviceCapacityOutResult;
    }


    @Override
    public boolean isShowStatus() {
        return false;
    }


    @Override
    public void OnItemClickListener(FaceBaseActivity_1 mContext, View view, int position, Object allData, LoadCursorSetting mLoadCursorSetting, Object[] t) throws RemoteException, FaceException {
        super.OnItemClickListener(mContext, view, position, allData, mLoadCursorSetting, t);
        IntentChangeManger.jumpTo(mContext, IntentChangeManger.flag_SUMediaPlayer_ac, false);
    }


    public static class MediaPlayerParameter extends DeviceCapacityInParameter implements Serializable {
        public MediaPlayerParameter(boolean isAskOn) {
            super(isAskOn);
        }

        public static final int TypePlay_Bytes = 1;//只是播放一遍
        @Deprecated
        public static final int TypePlay_Bytes_add = 2;//一般都是可以添加的
        public static final int TypePlay_Stop = 3;
        public static final int TypePlay_Pause = 4;
        public static final int TypePlay_ReStart = 5;
        public static final int TypePlay_SeekTo = 6;

        @Deprecated
        public static final int TypePlay_His_Add = 15;//?需要参数  转移进去
        @Deprecated
        public static final int TypePlay_His_Remove = 17;//?需要参数  转移进去
        @Deprecated
        public static final int TypePlay_His_Only_Add = 16;//?需要参数  直接添加
        @Deprecated
        public static final int TypePlay_His = 10;
        @Deprecated
        public static final int TypePlay_GetHis = 11;
        @Deprecated
        public static final int TypePlay_GetHis_one = 12;

        public static final int TypePlay_GetHis_one_Rebirth = 13;

        @Override
        public String getTypeName() {
            if (getType() == TypePlay_Bytes) {
                return "播放采集来的声音";
            } else if (getType() == TypePlay_Bytes_add) {
                return "播放采集来的声音,并添加到历史库";
            } else if (getType() == TypePlay_His_Only_Add) {
                return "直接添加到文件库";
            } else if (getType() == TypePlay_Stop) {
                return "停止";
            } else if (getType() == TypePlay_Pause) {
                return "暂停";
            } else if (getType() == TypePlay_ReStart) {
                return "继续播放";
            } else if (getType() == TypePlay_SeekTo) {
                return "跳跃到" + seekTo;
            } else if (getType() == TypePlay_His_Add) {
                return "转移到文件库";
            } else if (getType() == TypePlay_His) {
                return "播放文件库";
            } else if (getType() == TypePlay_GetHis) {
                return "查看文件库";
            } else if (getType() == TypePlay_GetHis_one) {
                return "播放" + fileName;
            } else if (getType() == TypePlay_His_Remove) {
                return "删除" + fileName;
            }

            return super.getTypeName();
        }

        String fileName;
        int seekTo;
        byte bs[];

        public MediaPlayerParameter(int type) {
            super(type);
        }

        //        add
        public MediaPlayerParameter(int type, String fileName, byte bs[]) {
            super(type);
            this.bs = bs;
            this.fileName = fileName;
        }

        //        play one
        public MediaPlayerParameter(int type, String fileName) {
            super(type);
            this.fileName = fileName;
        }

        //  当前的跳跃
        public MediaPlayerParameter(int type, int seekTo) {
            super(type);
            this.seekTo = seekTo;
        }
    }

  transient   MediaPlayer.OnCompletionListener mMediaPlayerOnCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mAccelerationSensorUnLock100();
        }
    };



    public void playMediaPlayer(File file) throws IOException {
        if (mp == null) mp = new MediaPlayer();
        if (mp.isPlaying()) stop();
        mp.reset();
        mp.setDataSource(file.getAbsolutePath());
        mp.prepare();
        mAccelerationSensorLockLong();
        mp.start();
        mp.setOnCompletionListener(mMediaPlayerOnCompletionListener);

    }

    private void stopMediaPlayer() {
        if (mp != null) mp.stop();
        mAccelerationSensorUnLock100();
    }

    private void pauseMediaPlayer() {
        if (mp != null) mp.pause();
        mAccelerationSensorUnLock100();
    }




}
