package com.hxuehh.rebirth.push.MiPush;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.apFind.FaceAc.ApFindActivity;
import com.hxuehh.rebirth.push.MiPush.handerMessage.MiPushHandlerManger;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageP2POrder_3;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 1、本demo可以直接运行，设置topic和alias。
 * 服务器端使用appsecret即可以向demo发送广播和单点的消息。
 * 2、为了修改本demo为使用你自己的appid，你需要修改几个地方：DemoApplication.java中的APP_ID,
 * APP_KEY，AndroidManifest.xml中的packagename，和权限permission.MIPUSH_RECEIVE的前缀为你的packagename。
 *
 * @author wangkuiwei
 */
public class MiPushMainActivity extends FaceBaseActivity_1 {

    public static List<String> logList = new CopyOnWriteArrayList<String>();

    public static MiPushMainActivity sMainActivity = null;
    public TextView logView = null;

    Handler mHan=new Handler();

    @Override
    public int getViewKey() {
        return 0;
    }

    EditText mEditTextForImei;

//   String actions[]=new String[]{"com.mipush.regid.get"};
//    FaceCommCallBack FaceCommCallBackforgetMiRed=new FaceCommCallBack() {
//        @Override
//        public boolean callBack(Object[] t) {
//            return false;
//        }
//    };
//    FaceCommCallBack mFaceCommCallBacks[] =new FaceCommCallBack[]{
//            FaceCommCallBackforgetMiRed};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mipush_activity_main);
//        setActionReceivers(actions,mFaceCommCallBacks);

        sMainActivity = this;
        logView = (TextView) findViewById(R.id.log);
        mEditTextForImei= (EditText) findViewById(R.id.imei_to);
        mEditTextForImei.setText(SharedPreferencesUtils.getString(SharedPreferencesKeys.Mi_Imei_Phone_loc));


        // 设置别名
        findViewById(R.id.set_alias).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MiPushMainActivity.this);
                new AlertDialog.Builder(MiPushMainActivity.this)
                        .setTitle(R.string.set_alias)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.setAlias(MiPushMainActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 撤销别名
        findViewById(R.id.unset_alias).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MiPushMainActivity.this);
                new AlertDialog.Builder(MiPushMainActivity.this)
                        .setTitle(R.string.unset_alias)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.unsetAlias(MiPushMainActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 设置帐号
        findViewById(R.id.set_account).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MiPushMainActivity.this);
                new AlertDialog.Builder(MiPushMainActivity.this)
                        .setTitle(R.string.set_account)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.setUserAccount(MiPushMainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 撤销帐号
        findViewById(R.id.unset_account).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MiPushMainActivity.this);
                new AlertDialog.Builder(MiPushMainActivity.this)
                        .setTitle(R.string.unset_account)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.unsetUserAccount(MiPushMainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 设置标签
        findViewById(R.id.subscribe_topic).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MiPushMainActivity.this);
                new AlertDialog.Builder(MiPushMainActivity.this)
                        .setTitle(R.string.subscribe_topic)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.subscribe(MiPushMainActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 撤销标签
        findViewById(R.id.unsubscribe_topic).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MiPushMainActivity.this);
                new AlertDialog.Builder(MiPushMainActivity.this)
                        .setTitle(R.string.unsubscribe_topic)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.unsubscribe(MiPushMainActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 设置接收消息时间
        findViewById(R.id.set_accept_time).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimeIntervalDialog(MiPushMainActivity.this, new TimeIntervalDialog.TimeIntervalInterface() {

                    @Override
                    public void apply(int startHour, int startMin, int endHour,
                                      int endMin) {
                        MiPushClient.setAcceptTime(MiPushMainActivity.this, startHour, startMin, endHour, endMin, null);
                    }

                    @Override
                    public void cancel() {
                        //ignore
                    }

                })
                        .show();
            }
        });
        // 暂停推送
        findViewById(R.id.set_1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextForImei.setText("863175020720526");//daniu
            }
        });

        findViewById(R.id.set_2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextForImei.setText("99000554002431");//mi
            }
        });

        findViewById(R.id.set_3).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextForImei.setText("99000628544201");
            }
        });
        findViewById(R.id.set_4).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextForImei.setText("99000457586555");
            }
        });



        findViewById(R.id.register).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MidMessageP2POrder_3 midMessageOrder_2 = new MidMessageP2POrder_3(MidMessageCMDKeys.MidMessageCMD_P2P_Regist, DeviceInfo.getInstens().getSU_UUID());
                midMessageOrder_2.putKeyValue(MidMessage.Key_Mi_Reg_ID, SharedPreferencesUtils.getString(SharedPreferencesKeys.MIregID));
                midMessageOrder_2.putKeyValue(MidMessage.Key_Imei, DeviceUtil.getimei());
                midMessageOrder_2.setIsSendToNet(true);
                MiPushHandlerManger.getInstance().addOrder(midMessageOrder_2);
            }
        });


        findViewById(R.id.loc).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String info=mEditTextForImei.getText().toString();
                if(StringUtil.isEmpty(info)){
                    DialogUtil.showLongToast(getFaceContext(),"不能是null");
                    return;
                }
                SharedPreferencesUtils.putString(SharedPreferencesKeys.Mi_Imei_Phone_loc,info);

                MidMessageP2POrder_3 midMessageOrder_2 = new MidMessageP2POrder_3(MidMessageCMDKeys.MidMessageCMD_P2P_Get_ShowLoc, DeviceInfo.getInstens().getSU_UUID());
                midMessageOrder_2.setIsSendToNet(true);
                midMessageOrder_2.putKeyValue(MidMessage.Key_To_Which_DeviceID, info);
                midMessageOrder_2.setmFaceCommCallBack(mFaceCommCallBack);
                MiPushHandlerManger.getInstance().addOrder(midMessageOrder_2);

            }
        });

        findViewById(R.id.clear).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logList.clear();
                refreshLogInfo();
            }
        });
    }

    FaceCommCallBack mFaceCommCallBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {

            MidMessage mid= (MidMessage) t[0];
           String info= (String) mid.getByKey(MidMessage.Key_Res);
            logList.add(info);
            mHan.post(new Runnable() {
                @Override
                public void run() {
                    refreshLogInfo();
                }
            });

            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
        String info = SharedPreferencesUtils.getString(SharedPreferencesKeys.MIregID);
        Su.logIM(info);
    }



    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
        logView.setText(AllLog);
    }
}
