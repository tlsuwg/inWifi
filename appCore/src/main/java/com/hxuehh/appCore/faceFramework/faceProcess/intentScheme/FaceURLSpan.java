package com.hxuehh.appCore.faceFramework.faceProcess.intentScheme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.provider.Browser;
import android.text.style.URLSpan;
import android.view.View;

import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;

/**
 * Created by suwg on 2015/1/12.
 */
public class FaceURLSpan extends URLSpan {

    public static  int FaceURLSpanKey=1021;

    private boolean startActivityForResult,finish;
    String jid;
    public FaceURLSpan(String url,boolean startActivityForResult,boolean finish,String jid) {
        super(url);
        this.startActivityForResult=startActivityForResult;
        this.finish=finish;
        this.jid=jid;
        if(startActivityForResult){
            this.finish=false;
        }
    }

    public FaceURLSpan(Parcel src) {
        super(src);
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        if(!StringUtil.isEmpty(jid))intent.putExtra("JID",jid);
//        if(!startActivityForResult) {
        context.startActivity(intent);
//        }else {
//            ((Activity)context).startActivityForResult(intent,FaceURLSpanKey);
//        }

        if(finish){
            ((Activity)context).finish();
        }
    }
}
