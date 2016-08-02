package com.hxuehh.rebirth.apFind.FaceView;

import android.view.View;

import com.google.gson.Gson;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
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
public class SendInfoToServerView extends FaceGetMainViewImp implements ProViewForStep {


    public static final int ServerPort = 6102;
    public static final int TryTimesMax = 4;

    public static final String OK = "OK";
    public static final String IP = "182.92.149.148";

//    public static final String IP = "192.165.1.113";

    public SendInfoToServerView(FaceContextWrapImp context, int Viewkey) {
        super(context, Viewkey);
        initView();
    }

    private ProView mProView;

    @Override
    protected void initView() {
        mainView = View.inflate(getFaceContext(), R.layout.select_init_backg, null);
        mProView = new ProView(getFaceContext());
        ViewKeys.addIntoLin(mainView.findViewById(R.id.main_view), mProView.getMainView(), getFaceContext());
    }

    int status;

    @Override
    public int setStatus(int status) {
        this.status = status;
        return status;
    }

    String sendInfo;

    @Override
    public void setLoadingView(String info) {
        mProView.setLoadingName(info);

        String phone = SharedPreferencesUtils.getString(SharedPreferencesKeys.SunShaoQingphoneNumberForServer);
        String MIregID = SharedPreferencesUtils.getString(SharedPreferencesKeys.MIregID);
        SendBody mSendBody = new SendBody("androidReg", phone, MIregID, 1);
        sendInfo = new Gson().toJson(mSendBody);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   boolean is= send();
                    if(is){
                        getFaceContext().getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mProView.setErrorInfo("上传成功！");
                                faceCommCallBackOK.callBack(status+1);
                            }
                        });
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    getFaceContext().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            mProView.setErrorInfo("上传失败！" + e.getMessage() + ",没有绑定成功。再次进入，自动重试");
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void onErr() {
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


    private boolean send() throws IOException {
        Socket socket = null;
        try {
            byte[] bs=sendInfo.getBytes();
            socket = new Socket(IP, ServerPort);
            socket.setSoTimeout(20000);
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

    static class SendBody {
        String action;
        String uid;
        String miuuid;
        int isreg;

        public SendBody(String action, String uid, String miuuid, int isreg) {
            this.action = action;
            this.uid = uid;
            this.miuuid = miuuid;
            this.isreg = isreg;
        }
    }
}
