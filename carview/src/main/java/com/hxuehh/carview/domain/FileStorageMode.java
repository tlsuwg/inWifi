package com.hxuehh.carview.domain;

import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;

/**
 * Created by suweiguang on 2016-08-09.
 */
public class FileStorageMode {
    public static final int FileStorageMode_Size = 1;//按照单空间
    public static final int FileStorageMode_Time = 2;//按照时间

    long AllSize;//总空间
    public int mFileStorageMode = FileStorageMode_Size;

    public int mFileStorageMode_OneTime;//多少分钟一次
    int mFileStorageModeSizeFilesSize;//需要保存的文件，分段保存数量;
    public long oneFileStorageModeSizeOneFileSize= AppStaticSetting.OneCarFilezSize;

    public FileStorageMode() {
        AllSize = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.AllSize);
        int cc = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.FileStorageMode);
        if(cc>0) {
            mFileStorageMode=cc;
            switch (mFileStorageMode) {
                case FileStorageMode_Size: {//
                    mFileStorageModeSizeFilesSize = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.FileStorageModeSizeFilesSize);
                    oneFileStorageModeSizeOneFileSize = AllSize / mFileStorageModeSizeFilesSize;//单个大小
                }
                break;
                case FileStorageMode_Time: {
                    mFileStorageMode_OneTime = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.FileStorageMode_OneTime);
                }
                break;
            }
        }
    }
}
