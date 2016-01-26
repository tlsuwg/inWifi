package com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;


/**
 * Created by IntelliJ IDEA.
 * User: kait
 * Date: 12-5-19
 * Time: 下午1:40
 * To change this template use File | SettingsActivity | File Templates.
 */
public class DialogUtil {


    public interface ICallback {
        public void clear();
    }


    public static void showDelDialog(final Activity context, String title, String message, int icon, String trueStr, final FaceCommCallBack m1,
                                     String falseStr, final FaceCommCallBack m2) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        if (title != null)
            alertDialog.setTitle(title);
        if (message != null)
            alertDialog.setMessage(message);
        if (icon > 0)
            alertDialog.setIcon(icon);
        alertDialog.
                setPositiveButton(trueStr, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (m1 != null) {
                            m1.callBack();
                        }
                    }
                });

        if(falseStr!=null)
        alertDialog.
                setNegativeButton(falseStr, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (m2 != null) {
                            m2.callBack();
                        }
                    }
                });
//                setNeutralButton("查看详情", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                    }
//                }).
        alertDialog.create().show();


    }

    public static void showShortToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }



}
