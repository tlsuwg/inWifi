package com.hxuehh.rebirth.push.MiPush;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.domain.DeviceInfo;
import com.hxuehh.rebirth.apFind.FaceAc.ApFindActivity;
import com.hxuehh.rebirth.push.MiPush.handerMessage.MiPushHandlerManger;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_Err_3;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageP2POrder_3;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.DeviceUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

/**
 * 1、PushMessageReceiver是个抽象类，该类继承了BroadcastReceiver。
 * 2、需要将自定义的DemoMessageReceiver注册在AndroidManifest.xml文件中 <receiver
 * android:exported="true"
 * android:name="com.xiaomi.mipushdemo.MIPushMessageReceiver"> <intent-filter>
 * <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" /> </intent-filter>
 * <intent-filter> <action android:name="com.xiaomi.mipush.ERROR" />
 * </intent-filter> <intent-filter>
 * <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" /></intent-filter>
 * </receiver>
 * <p/>
 * 3、DemoMessageReceiver的onReceivePassThroughMessage方法用来接收服务器向客户端发送的透传消息
 * <p/>
 * 4、DemoMessageReceiver的onNotificationMessageClicked方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发
 * 5、DemoMessageReceiver的onNotificationMessageArrived方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数
 * 6、DemoMessageReceiver的onCommandResult方法用来接收客户端向服务器发送命令后的响应结果
 * 7、DemoMessageReceiver的onReceiveRegisterResult方法用来接收客户端向服务器发送注册命令后的响应结果
 * 8、以上这些方法运行在非UI线程中
 *
 * @author mayixiang
 */
public class MIPushMessageReceiver extends PushMessageReceiver {

    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;


    //    正式的处理消息
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        Su.logPush("onReceivePassThroughMessage is called. " + message.toString());

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
        showLog(context, R.string.recv_passthrough_message, message);
        String info = message.getContent();
        if (!StringUtil.isEmpty(info)) {
            Su.logPush(info);
            HashMap baseInfo = new Gson().fromJson(info, HashMap.class);

            if (AppStaticSetting.isSunShaoQingPro) {//孙绍清的东西
                if (baseInfo.containsKey("android_device")) {
                    SuApplication.getInstance().sendBroadcast(new Intent(ApFindActivity.SunShaoQingApDeviceRegsetInServerOK));
                }
            } else {
                String cmd = (String) baseInfo.get(MidMessage.Key_CMD);
                int cmdint = 0;
                if (cmd != null) {
                    cmdint = Integer.parseInt(cmd);
                }
                boolean isOrder = false;
                MidMessage mid = null;
                if (cmdint == 1 || cmdint == 0) {
                    if (baseInfo.containsKey(MidMessage.Key_ErrInfo)) {
                        mid = new MidMessageBack_Err_3();
                    } else {
                        mid = new MidMessageBack_2();
                    }
                } else {
                    String from = (String) baseInfo.get(MidMessage.Key_DeviceID);
                    mid = new MidMessageP2POrder_3(cmdint, from);
                    isOrder = true;
                }
                mid.setOutMap(baseInfo);
                if (isOrder) {
                    MiPushHandlerManger.getInstance().addOrder(mid);
                } else {
                    MiPushHandlerManger.getInstance().resCallBack(mid);
                }
            }
        }
    }


    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        Su.logPush("onNotificationMessageClicked is called. " + message.toString());

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        showLog(context, R.string.click_notification_message, message);

    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        Su.logPush(
                "onNotificationMessageArrived is called. " + message.toString());

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        showLog(context, R.string.arrive_notification_message, message);

    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Su.logPush(
                "onCommandResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log = "";
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = context.getString(R.string.register_success) + "；ID:" + cmdArg1;
            } else {
                log = context.getString(R.string.register_fail);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = context.getString(R.string.set_alias_success, mAlias);
            } else {
                log = context.getString(R.string.set_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = context.getString(R.string.unset_alias_success, mAlias);
            } else {
                log = context.getString(R.string.unset_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                log = context.getString(R.string.set_account_success, mAccount);
            } else {
                log = context.getString(R.string.set_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                log = context.getString(R.string.unset_account_success, mAccount);
            } else {
                log = context.getString(R.string.unset_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = context.getString(R.string.subscribe_topic_success, mTopic);
            } else {
                log = context.getString(R.string.subscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                log = context.getString(R.string.unsubscribe_topic_success, mTopic);
            } else {
                log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
                log = context.getString(R.string.set_accept_time_success, mStartTime, mEndTime);
            } else {
                log = context.getString(R.string.set_accept_time_fail, message.getReason());
            }
        } else {
            log = message.getReason();
        }

        if (!AppStaticSetting.isMiTestLog) return;
        MiPushMainActivity.logList.add(0, getSimpleDate() + "    " + log);
        UIshow(log);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Su.logPush("onReceiveRegisterResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                SharedPreferencesUtils.putString(SharedPreferencesKeys.MIregID, mRegId);

                MidMessageP2POrder_3 midMessageOrder_2 = new MidMessageP2POrder_3(MidMessageCMDKeys.MidMessageCMD_P2P_Regist, DeviceInfo.getInstens().getSU_UUID());
                midMessageOrder_2.putKeyValue(MidMessage.Key_Mi_Reg_ID, mRegId);
                midMessageOrder_2.putKeyValue(MidMessage.Key_Imei, DeviceUtil.getimei());
                midMessageOrder_2.setIsSendToNet(true);
                MiPushHandlerManger.getInstance().addOrder(midMessageOrder_2);

                String de = DeviceUtil.getimei();
                if (!StringUtil.isEmpty(de)) {
                    MiPushClient.setAlias(context, de, null);
                }
                log = context.getString(R.string.register_success);


            } else {
                log = context.getString(R.string.register_fail);
            }
        } else {
            log = message.getReason();

        }
        UIshow(log);
    }

    private void showLog(Context context, int recv_passthrough_message, MiPushMessage message) {
        if (!AppStaticSetting.isMiTestLog) return;
        String log = context.getString(recv_passthrough_message, message.getContent() + "  " + message.getDescription());
        MiPushMainActivity.logList.add(0, getSimpleDate() + " " + log);
        UIshow(log);
    }

    private void UIshow(String log) {
        if (!AppStaticSetting.isMiTestLog) return;
        Message msg = Message.obtain();
        msg.obj = log;
        getHandler().sendMessage(msg);
    }


    DemoHandler mDemoHandler = new DemoHandler(SuApplication.getInstance());

    private Handler getHandler() {
        return mDemoHandler;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }

    public static class DemoHandler extends Handler {
        private Context context;

        public DemoHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            if (MiPushMainActivity.sMainActivity != null) {
                MiPushMainActivity.sMainActivity.refreshLogInfo();
            }
//            if (!TextUtils.isEmpty(s)) {
//                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
//            }
        }
    }

}
