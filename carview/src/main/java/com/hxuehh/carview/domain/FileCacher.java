package com.hxuehh.carview.domain;

import android.app.Application;

import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by suweiguang on 2016-08-09.
 */
public class FileCacher {
    Application application;
    FileStorageMode mFileStorageMode;

    public FileCacher(Application application) {
        this.application = application;
    }

    File dir;
    Set<SuFile> fileSet=new TreeSet<SuFile>();

    public boolean checkDir() {
        String dirPath = SharedPreferencesUtils.getString(SharedPreferencesKeys.carFullDir);
        if (StringUtil.isEmpty(dirPath)) {
            return false;
        } else {
             dir = new File(dirPath);
            if (!dir.exists()) {
                return false;
            }
            findVideo();
            loadMode();
        }
        return true;
    }


    private void loadMode() {
         mFileStorageMode=new FileStorageMode();
    }


    private void findVideo() {
        File files[]=dir.listFiles();
        for(File f:files){
            if(f.isFile()&&f.getName().endsWith(AppStaticSetting.vedioEnd)){
               if(f.length()>0) {
                   fileSet.add(new SuFile(f));
               }else{
                   FileUtil.deleteFile(f.getAbsolutePath());
               }
            }
        }
    }


    public SuFile getNewFile() {
        if(fileSet.size()>=5){
            SuFile mSuFile= (SuFile) fileSet.toArray()[0];
            mSuFile.delete();
            fileSet.remove(mSuFile);
        }
        SuFile suFile=new SuFile(dir,new Date());
        suFile.setFileStorageMode(mFileStorageMode);
        fileSet.add(suFile);
        return suFile;
    }
}
