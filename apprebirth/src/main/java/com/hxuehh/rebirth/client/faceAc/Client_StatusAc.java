package com.hxuehh.rebirth.client.faceAc;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.rebirth.R;

import android.os.*;
import android.os.Process;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceContextWrapImp;
import com.hxuehh.rebirth.all.FaceView.SinitTestnetWifi2;
import com.hxuehh.rebirth.all.FaceView.SinitTobeLinkServer2Status;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.client.FaceView.SinitClientShow;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.server.FaceView.UDPbroadcast.UDPServerFinder;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.app.threadManager.ThreadManager;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DateUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.file.FileUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by suwg on 2015/8/13.
 */
public class Client_StatusAc extends FaceBaseActivity_1 {

    @Override
    public int getViewKey() {
        return ViewKeys.Client_StatusAc;
    }


    FaceCommCallBack FaceCommCallBackTCPListen = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            mSinitTobeLinkServer2Status.setStartErrType(2);
            stopClientService();
            findViewById(R.id.select_title_lin).setVisibility(View.VISIBLE);
            return false;
        }

    };

    FaceCommCallBack FaceCommCallBackTCPLink = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            mSinitTobeLinkServer2Status.setStartErrType(1);
            stopClientService();
            findViewById(R.id.select_title_lin).setVisibility(View.VISIBLE);
            return false;
        }
    };


    FaceCommCallBack FaceCommCallBackStatusErr = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            findViewById(R.id.select_title_lin).setVisibility(View.VISIBLE);
            if (mSinitTobeLinkServer2Status != null)
                mSinitTobeLinkServer2Status.setRunningStatus();
            return false;
        }
    };


    FaceCommCallBack FaceCommCallBackStatusOK = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            findViewById(R.id.select_title_lin).setVisibility(View.VISIBLE);
            mSinitTobeLinkServer2Status.setRunningStatus();
//            getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    findViewById(R.id.select_title_lin).setVisibility(View.GONE);
//                }
//            }, 1000);

            findViewById(R.id.main_lin).setVisibility(View.VISIBLE);
            clientShow();
            return false;
        }
    };


    private String actions[] = new String[]{
            ClientService_TCPLongLink_.TCP_Succeed,
            ClientService_TCPLongLink_.TCPListenErr,
            ClientService_TCPLongLink_.TCPLinkMainErr,
            UDPServerFinder.UDPServerFinder,//重连
            ClientService_TCPLongLink_.TCPLinkReLinkedOK,//链接OK
    };


    private FaceCommCallBack faceCommCallBack[] = new FaceCommCallBack[]{
            FaceCommCallBackStatusOK,
            FaceCommCallBackTCPListen,
            FaceCommCallBackTCPLink,
            FaceCommCallBackStatusErr,
            FaceCommCallBackStatusOK};

    private void stopClientService() {
        IntentChangeManger.unbindServiceToApp(IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink);
        IntentChangeManger.stopService(this, IntentChangeManger.Client_Service_UDPreviceback_TCPLongLink);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        setActionReceivers(actions, faceCommCallBack);
        initView();
        addListeners();
    }

    @Override
    public void initView() {
        super.initView();
        TitleView mTitle = new TitleView(this);
        mTitle.setTitle("控制端检测");
        mTitle.addIntoView(this, R.id.title_lin);

        String names[] = new String[]{"mi", "加速 驱动设置", "重新设置设备功用", "测试AIDL", "run java","amCppy_chmod_run"};


        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((Integer) v.getTag()) {
                    case 0: {
                        IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_mi_test, false);
                    }
                    break;
                    case 1: {
                        IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_acceleration_setting, false);
                    }
                    break;
                    case 2: {
                        IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_used_type_noSet, false);
                    }
                    break;

                    case 5: {
                        File fileTo=null;
                        try {
                            fileTo = FileUtil.getFileExist(SuApplication.getInstance().getCacheDir().getPath() + "/suSystem", "supm");
                            FileUtil.doFileCopy("sdcard/supm", fileTo);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
//                        String run="dalvikvm -Xmx540m  -Xms512m -classpath sdcard/sdex.jar com.hxuehh.rebirth.javarun.Su"
//                        final String cmd="dalvikvm  -classpath "+fileTo.getAbsoluteFile()+" com.hxuehh.rebirth.javarun.Su 3244444444444444444444";
//                        dalvikvm  -classpath /data/data/com.hxuehh.rebirth/cache/j_r_t/sudex com.hxuehh.rebirth.javarun.Su 3244444444444444444444
//                        final String run="su";
//                        final String run=fileTo.getAbsolutePath()+" startservice --user 0 -a com.hxuehh.rebirth.Service.SuReLife";
                        final String cmd="chmod 777 "+fileTo.getAbsoluteFile();

                        try {
                            java.lang.Process p = Runtime.getRuntime().exec(cmd);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        final String run=fileTo.getAbsolutePath()+" install  /sdcard/BusyBox.apk";

//

                        Runnable mRunnable = new Runnable() {
                            private void reStartAidl() {

                            }

                            @Override
                            public void run() {
                                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                                String result = "";
                                DataOutputStream dos = null;
                                DataInputStream dis = null;
                                DataInputStream disErr = null;

                                try {
                                    java.lang.Process p = Runtime.getRuntime().exec(run);
                                    dos = new DataOutputStream(p.getOutputStream());
                                    dis = new DataInputStream(p.getInputStream());
                                    disErr = new DataInputStream(p.getErrorStream());
//                                    String cmd="1111";
                                    String line = null;
//
//                                    dos.writeBytes(cmd +"\n");
//                                    dos.flush();
//                                    dos.writeBytes("exit\n");
//                                    dos.flush();

                                    while ((line = dis.readLine()) != null) {
                                        result += line;
                                        Su.log(line);
                                    }

                                    while ((line = disErr.readLine()) != null) {
                                        result += line;
                                        Su.log("ERR"+line);
                                    }


                                    while ((line = dis.readLine()) != null) {
                                        result += line;
                                        Su.log(line);
                                    }

//                                    int line = 0;
//                                    while ((line = dis.read()) != -1) {
//                                        result += line+"";
//                                        Su.log(line+"");
//                                    }
//                                    while ((line = disErr.read()) != -1) {
//                                        result += line+"";
//                                        Su.log(line+"");
//                                    }

                                    p.waitFor();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Su.log("");
                                } finally {
                                    if (dos != null) {
                                        try {
                                            dos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (dis != null) {
                                        try {
                                            dis.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        };

                        Thread mThread = new Thread(mRunnable, " su back thread");
                        mThread.setPriority(Thread.MAX_PRIORITY);
                        mThread.start();
                    }
                    break;

                    case 4: {
                        File fileTo=null;
                        try {
                             fileTo = FileUtil.getFileExist(SuApplication.getInstance().getCacheDir().getPath() + "/j_r_t", "sudex");
                            FileUtil.doFileCopy("sdcard/sdex.jar", fileTo);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
//                        String run="dalvikvm -Xmx540m  -Xms512m -classpath sdcard/sdex.jar com.hxuehh.rebirth.javarun.Su"
                        final String cmd="dalvikvm  -classpath "+fileTo.getAbsoluteFile()+" com.hxuehh.rebirth.javarun.Su 3244444444444444444444";
//                        dalvikvm  -classpath /data/data/com.hxuehh.rebirth/cache/j_r_t/sudex com.hxuehh.rebirth.javarun.Su 3244444444444444444444
                        final String run="su";

                        Runnable mRunnable = new Runnable() {
                            private void reStartAidl() {

                            }

                            @Override
                            public void run() {
                                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                                String result = "";
                                DataOutputStream dos = null;
                                DataInputStream dis = null;
                                DataInputStream disErr = null;

                                try {
                                    java.lang.Process p = Runtime.getRuntime().exec(cmd);
                                    dos = new DataOutputStream(p.getOutputStream());
                                    dis = new DataInputStream(p.getInputStream());
                                    disErr = new DataInputStream(p.getErrorStream());
//                                    String cmd="1111";
                                    String line = null;
//                                    while ((line = dis.readLine()) != null) {
//                                        result += line;
//                                        Su.log(line);
//                                    }
//
                                    dos.writeBytes(cmd +"\n");
                                    dos.flush();
                                    dos.writeBytes("exit\n");
                                    dos.flush();

                                    while ((line = dis.readLine()) != null) {
                                        result += line;
                                        Su.log(line);
                                    }

                                    while ((line = disErr.readLine()) != null) {
                                        result += line;
                                        Su.log(line);
                                    }

//                                    int line = 0;
//                                    while ((line = dis.read()) != -1) {
//                                        result += line+"";
//                                        Su.log(line+"");
//                                    }
//                                    while ((line = disErr.read()) != -1) {
//                                        result += line+"";
//                                        Su.log(line+"");
//                                    }

                                    p.waitFor();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Su.log("");
                                } finally {
                                    if (dos != null) {
                                        try {
                                            dos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (dis != null) {
                                        try {
                                            dis.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        };

                        Thread mThread = new Thread(mRunnable, " su back thread");
                        mThread.setPriority(Thread.MAX_PRIORITY);
                        mThread.start();
                    }
                    break;

                    case 3: {

                        try {
                            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.Thread_wait_oo, BytesClassAidl.To_Me, "Thread_wait_oo"));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }


                        Runnable mRunnable = new Runnable() {
                            private void reStartAidl() {
                                SuApplication.getInstance().bindAidlCache();
                            }

                            @Override
                            public void run() {
                                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
                                try {
                                    Su.log("sss");
                                    BytesClassAidl mBytesClassAidl = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.Thread_wait_oo, BytesClassAidl.To_Thread_Wait);
                                    Su.log(mBytesClassAidl + "null aidl");
//                                   直接使用kill 1233
                                } catch (DeadObjectException e) {
                                    e.printStackTrace();
                                    reStartAidl();
                                    Su.log("AIDL END");
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        Thread mThread = new Thread(mRunnable, " su back thread");
                        mThread.setPriority(Thread.MAX_PRIORITY);
                        mThread.start();

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            BytesClassAidl mBytesClassAidl = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.Thread_wait_oo, BytesClassAidl.To_Me);
                            Su.log(mBytesClassAidl + "aidl 222");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }

            }
        };

        for (int i = 0; i < names.length; i++) {
            Button setingButton = new Button(this);
            setingButton.setPadding(15, 5, 15, 5);
            setingButton.setText(names[i]);
            setingButton.setTag(i);
            setingButton.setOnClickListener(mOnClickListener);
            ViewKeys.addIntoLin(R.id.main_buttom_lin, setingButton, this);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (status == kinked_OK) return;
        status = testWifi;
        doStatus();
    }

    int status = testWifi;
    public static final int testWifi = 1;
    public static final int testHasLinkServerStarted = 2;
    public static final int kinked_OK = 3;

    private void doStatus() {
        switch (status) {
            case testWifi: {
                testWifi(status);
            }
            break;
            case testHasLinkServerStarted: {
                testHasLinkServerStarted(status);
            }
            break;
            case kinked_OK: {
                all_OK(status);
            }
            break;
        }
    }

    private void all_OK(int status) {
        FaceCommCallBackStatusOK.callBack();
    }


    private SinitTestnetWifi2 mSinitTestnetWifi2;
    private SinitTobeLinkServer2Status mSinitTobeLinkServer2Status;
    private SinitClientShow mSinitClientShow;


    FaceCommCallBack mFaceCommCallBackSucc = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object[] t) {
            status = (Integer) t[0];
            doStatus();
            return false;
        }
    };


    private void clientShow() {
        if (mSinitClientShow == null) {
            mSinitClientShow = new SinitClientShow(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSinitClientShow.addIntoView(getFaceContext(), R.id.main_lin);
        }
        mSinitClientShow.setLoadingView("查询已经连接设备...");
    }

    private void testHasLinkServerStarted(int status2) {
        if (mSinitTobeLinkServer2Status == null) {
            mSinitTobeLinkServer2Status = new SinitTobeLinkServer2Status(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSinitTobeLinkServer2Status.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSinitTobeLinkServer2Status.setStatus(status2);
            mSinitTobeLinkServer2Status.setDoSucceed(mFaceCommCallBackSucc);
        }
        if (!IntentChangeManger.isClientServiceStart()) {
            mSinitTobeLinkServer2Status.setLoadingView("链接中心服务...");
        } else {
            FaceCommCallBackStatusOK.callBack();
            mFaceCommCallBackSucc.callBack(status2 + 1);

        }
    }

    private void testWifi(final int status2) {
        if (mSinitTestnetWifi2 == null) {
            mSinitTestnetWifi2 = new SinitTestnetWifi2(new FaceContextWrapImp(getFaceContext()), getViewKey());
            mSinitTestnetWifi2.addIntoView(getFaceContext(), R.id.select_title_lin);
            mSinitTestnetWifi2.setStatus(status2);
            mSinitTestnetWifi2.setDoSucceed(mFaceCommCallBackSucc);
        }
        mSinitTestnetWifi2.setLoadingView("检测wifi下进行...", SharedPreferencesKeys.device_used_type_client);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
