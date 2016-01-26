package com.hxuehh.rebirth.apFind.FaceView;

import android.net.wifi.WifiInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.server.FaceView.ServerInit.ProViewForStep;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.NetStatusUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


/**
 * Created by suwg on 2015/8/13.
 */
public class SendtoApMyWifiInfosView extends FaceGetMainViewImp implements ProViewForStep {


    public static final int ServerPort = 8080;
    public static final int TryTimesMax = 1;
    public static final String OK = "OK";
    public static final char CharForAp  = 0;
    public SendtoApMyWifiInfosView(FaceContextWrapImp context, int Viewkey) {
        super(context, Viewkey);
        initView();
    }

    private ProView mProView;
    EditText wifi_name_edit,wifi_pass_edit,phone_edit;
    Button send_phone_info;
    LinearLayout main_view_2;

    String sendInfo;


    @Override
    protected void initView() {
        mainView = View.inflate(getFaceContext(), R.layout.ap_info, null);
        mProView = new ProView(getFaceContext());
        mProView.gone_all();

        ViewKeys.addIntoLin(mainView.findViewById(R.id.main_view), mProView.getMainView(), getFaceContext());

        wifi_name_edit= (EditText) findViewById(R.id.wifi_name_edit);
        wifi_pass_edit= (EditText) findViewById(R.id.wifi_pass_edit);
        phone_edit= (EditText) findViewById(R.id.phone_edit);
        send_phone_info= (Button) findViewById(R.id.send_phone_info);
        main_view_2= (LinearLayout) findViewById(R.id.main_view_2);

        String str=SharedPreferencesUtils.getString(SharedPreferencesKeys.SunShaoQingphoneNumberForServer);
        if(!StringUtil.isEmpty(str)){
            phone_edit.setText(str);
        }

         str=SharedPreferencesUtils.getString(SharedPreferencesKeys.SunShaoQingWifiPassWord);
        if(!StringUtil.isEmpty(str)){
            wifi_pass_edit.setText(str);
        }


        send_phone_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifi_name_edit_info=wifi_name_edit.getEditableText().toString();
                String wifi_pass_edit_info=wifi_pass_edit.getEditableText().toString();
                String phone_edit_info=phone_edit.getEditableText().toString();

                if(StringUtil.isEmpty(wifi_name_edit_info)||StringUtil.isEmpty(wifi_pass_edit_info)||StringUtil.isEmpty(phone_edit_info)){
                    DialogUtil.showLongToast(getFaceContext(),"请设置_wifi名称_wifi密码_手机号");
                    return;
                }

                SharedPreferencesUtils.putString(SharedPreferencesKeys.SunShaoQingphoneNumberForServer,phone_edit_info);
                SharedPreferencesUtils.putString(SharedPreferencesKeys.SunShaoQingWifiPassWord,wifi_pass_edit_info);

                SendBody mSendBody=new SendBody(phone_edit_info,wifi_name_edit_info.replace("\"",""),wifi_pass_edit_info,NetStatusUtil.getSecurityWay(mWifiInfo.getSSID()));
                sendInfo=mSendBody.getString();
                tryTimes=0;
                setLoadingView("连接:"+newIP+":"+ServerPort);
            }
        });
    }

    int status;

    @Override
    public int setStatus(int status) {
        this.status = status;
        return status;
    }


    boolean isNotget = true;
    int tryTimes;

    String info;

    @Override
    public void setLoadingView(final String info) {
        this.info = info;


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isNotget&&getFaceContext().isOnTop()) {

                    if(tryTimes!=0)
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    tryTimes++;
                    StringBuffer sb = new StringBuffer();
                    sb.append(info).append(",尝试次数:").append(tryTimes);
                    final String info = sb.toString();
                    if(getFaceContext().isOnTop())
                    getFaceContext().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mProView.setLoadingName(info);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    boolean isget = false;
                    try {
                        isget = send();
                        if (isget) {
                            if(getFaceContext().isOnTop())
                            getFaceContext().getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mProView.setOk("内网消息发送完成",false);
                                    main_view_2.setVisibility(View.GONE);
                                    faceCommCallBackOK.callBack(status + 1);
                                }
                            });
                            break;
                        }
                    } catch (IOException e) {

                        sb.append(",结果：").append(e.getMessage());
                        final String infoerr = sb.toString();

                        if(getFaceContext().isOnTop())
                        getFaceContext().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (tryTimes < TryTimesMax) {
                                    mProView.setLoadingName(infoerr + ",继续尝试");
                                } else {
                                    mProView.setErrorInfo(infoerr + ",结束尝试,失败！");
                                }
                            }
                        });

                        if (tryTimes >= TryTimesMax) {
                            break;
                        }

                    }
                }
            }
        }).start();


    }

    private boolean send() throws IOException {
        Socket socket = null;
        try {
            byte[] bs=sendInfo.getBytes();
            socket = new Socket(newIP, ServerPort);
            socket.setSoTimeout(40000);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write(bs);
            out.flush();
            byte[] bsb=new byte[1024];
            int getInt=  input.read(bsb);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(bsb, 0, getInt);
            String request = new String(baos.toByteArray());

//            byte getByte = input.readByte();
//            Su.log("get ap sendback "+getByte);
            boolean is=false;
            if ("SUCCESS".equals(request)) {
                is=true;
            }else{
                throw new IOException("服务端返回异常"+request);
            }
            out.close();
            input.close();
            return is;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                }
            }
        }
    }


    @Override
    public void onErr() {
        mProView.setErrorInfo("首次使用必须使用Wifi关联，请点击设置");
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetStatusUtil.openNetSetting(getFaceContext());
            }
        });
    }

    FaceCommCallBack faceCommCallBackOK;

    @Override
    public void setDoSucceed(FaceCommCallBack faceCommCallBack) {
        mainView.setOnClickListener(null);
        this.faceCommCallBackOK = faceCommCallBack;
    }

    @Override
    public void onOk(String info) {

    }


    WifiInfo mWifiInfo;

    public void setOldWifiInfo(WifiInfo mWifiInfo) {
        this.mWifiInfo = mWifiInfo;
        if(mWifiInfo!=null&&!StringUtil.isEmpty(mWifiInfo.getSSID()))
            wifi_name_edit.setText(mWifiInfo.getSSID());

        main_view_2.setVisibility(View.VISIBLE);
    }

    String newIP;

    public void setIP(String newIP) {
        this.newIP = newIP;
    }


    static class SendBody{
        String phone;
        String wifi_SSID;
        String wifi_pass_word;
        String wifi_mode="null";

        public SendBody(String phone, String wifi_SSID, String wifi_pass_word, String wifi_mode) {
            this.phone = phone;
//            this.wifi_SSID = "TP-LINK_CB2A";
            this.wifi_SSID=wifi_SSID;
            this.wifi_pass_word = wifi_pass_word;
            if(!StringUtil.isEmpty(wifi_mode))
            this.wifi_mode = wifi_mode;
        }

        String getString(){
            return
                    wifi_SSID+
                    CharForAp+wifi_pass_word+
                    CharForAp+phone+
                    CharForAp+wifi_mode;
        }
    }

}
