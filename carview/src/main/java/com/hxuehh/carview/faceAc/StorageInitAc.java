package com.hxuehh.carview.faceAc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.view.View;
import android.widget.Button;

import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.carview.R;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.io.File;

/**
 * Created by suweiguang on 2016-08-08.
 */
public class StorageInitAc extends FaceBaseActivity_1 {

    public static final int Full_Size = 1;
    public static final int Fule_Scale = 2;

    @Override
    public int getViewKey() {
        return 0;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        initView();
        addListeners();
    }

    ProView mPro;


    @Override
    public void initView() {
        TitleView mTitleView = new TitleView(this);
        mTitleView.setTitle("存储相关初始化");
        mTitleView.addIntoView(this, R.id.title_lin);

        mPro = new ProView(getFaceContext());
        mPro.setLoadingName("");
        ViewKeys.addIntoLin(R.id.main_lin, mPro.getMainView(), this);

        findCanUsedFileDir();

    }


    private void findCanUsedFileDir() {
        StatFs sd = FileUtil.SDCardSizeStatFs();
        StatFs system = FileUtil.readSystem();
        String info = "本机存储：" + StringUtil.N + "系统" + FileUtil.getInfo(system) + (sd == null ? "" : StringUtil.N + "外部" + FileUtil.getInfo(sd));
        mPro.setOk(info, false);
        if (sd != null) {

            long blockSize;
            long totalBlocks;
            long availableBlocks;
// 由于API18（Android4.3）以后getBlockSize过时并且改为了getBlockSizeLong
// 因此这里需要根据版本号来使用那一套API
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            {
                blockSize = sd.getBlockSizeLong();
                totalBlocks = sd.getBlockCountLong();
                availableBlocks = sd.getAvailableBlocksLong();
            }
            else
            {
                blockSize = sd.getBlockSize();
                totalBlocks = sd.getBlockCount();
                availableBlocks = sd.getAvailableBlocks();
            }

            long sdCanUsed = totalBlocks* blockSize ;
            long sdAll = availableBlocks*blockSize;
            if (sdCanUsed >= AppStaticSetting.FullStorageSize * 1024 * 1024 * 1024) {
                saveDir();
            } else if (sdCanUsed / sdAll * 100 >= AppStaticSetting.SDStorageSize) {
                saveDir();
            } else {
                mPro.setErrorInfo("初始化失败，SD卡剩余空间不到" + AppStaticSetting.FullStorageSize + "G或者 SD使用率>" + AppStaticSetting.SDStorageSize + "%;请删除之后重试");
            }
        } else {
            mPro.setErrorInfo("初始化失败，SD卡不存在");
        }
    }

    private void saveDir() {
        File dir = FileUtil.getDiskCacheDir(SuApplication.getInstance(), AppStaticSetting.carDir);
        if (!dir.exists()) {//判断文件目录是否存在
            dir.mkdirs();
        }
        SharedPreferencesUtils.putString(SharedPreferencesKeys.carFullDir, dir.getAbsolutePath());
        showRecodeButton();
    }

    private void showRecodeButton() {
        Button mButton=new Button(getFaceContext());
        mButton.setText("初始化完成，进入拍摄界面");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getFaceContext(), OnTopActivity.class);
                startActivity(in);
            }
        });

        ViewKeys.addIntoLin(R.id.main_lin, mButton, this);
    }

}
