package com.hxuehh.carview.faceAc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.carview.R;
import com.hxuehh.carview.domain.FileAdapter;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suweiguang on 2016-08-16.
 */
public class FileDirActivity extends FaceBaseActivity_1 {
    @Override
    public int getViewKey() {
        return 0;
    }

    List<File> list;
    FileAdapter mFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filrDir = getIntent().getStringExtra("file_dir");
        if (StringUtil.isEmpty(filrDir)) {
            finish();
            return;
        }
        File file = new File(filrDir);
        if (file == null && !file.exists()) {
            finish();
            return;
        }

        setContentView(R.layout.main_lin);

        TitleView mTitleView = new TitleView(this);
        mTitleView.setTitle("文件查看");
        mTitleView.addIntoView(this, R.id.title_lin);


        list = new ArrayList<>();
        for (File f : file.listFiles()) {
            list.add(f);
        }

        final ListView listview = new ListView(getFaceContext());

        mFileAdapter = new FileAdapter(list, getFaceContext());
        listview.setAdapter(mFileAdapter);
        ViewKeys.addIntoLin(R.id.main_lin, listview, this);

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                File f = list.get(position);
//                showVideo(f);
//            }
//        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                File f = list.get(position);
                showDialog(f);
                return false;
            }
        });
    }

    private void showDialog(final File f) {

        new AlertDialog.Builder(this)
                .setTitle("事项：").setItems(new String[]{"删除", "播放", "永久保存", "取消"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        FileUtil.deleteFile(f.getAbsolutePath());
                        list.remove(f);
                        mFileAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        showVideo(f);
                        break;
                    case 2:
                        saveForErer(f);
                        break;
                    case 3:

                        break;
                }
            }
        }).show();

    }

    private void saveForErer(File f) {
        File fileNew = FileUtil.getFileExist(f.getParentFile().getAbsolutePath(), AppStaticSetting.noDeleteFlag + f.getName());
        f.renameTo(fileNew);
    }


    private void showVideo(File f) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            Uri uri = Uri.fromFile(f);
            intent.setDataAndType(uri, "video/*");
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(getFaceContext(),"没有默认播放器",Toast.LENGTH_SHORT).show();
        }

    }

}
