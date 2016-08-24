package com.hxuehh.carview.domain;

import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;

import java.io.File;
import java.util.Date;

/**
 * Created by suweiguang on 2016-08-09.
 */
public class SuFile implements Comparable<SuFile> {


    File file;
    File dir;

    public SuFile(File f) {
        this.file = f;
        dir=f.getParentFile();
    }

    public SuFile(File dir, Date date) {
        this.dir=dir;
        String name = DateUtil.getCurrentTimeForname() + AppStaticSetting.vedioEnd;
        this.file = FileUtil.getFileExist(dir.getAbsolutePath(), name);
    }

    @Override
    public int compareTo(SuFile another) {
        return (int) (this.file.lastModified() - another.file.lastModified());
    }


    public void delete() {
        FileUtil.deleteFile(this.file.getAbsolutePath());
    }

    public String getPath() {
        return this.file.getAbsolutePath();
    }

    FileStorageMode mFileStorageMode;

    public void setFileStorageMode(FileStorageMode mFileStorageMode) {
        this.mFileStorageMode = mFileStorageMode;
    }

    public FileStorageMode getmFileStorageMode() {
        return mFileStorageMode;
    }

    public long getSize() {
        return file.length();
    }

    public String getDirPath() {
        return dir.getAbsolutePath();
    }
}
