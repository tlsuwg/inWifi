package com.hxuehh.rebirth.all.FaceView.FTDIusb;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_Serialize;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.MathUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/9/18.
 */
public class FTDISerViewForDevice extends FaceGetMainViewImp {
    ProView mProView;
    private USBLinker_Serialize mUSBLinker;
    USBSerializ mUSB;
    public FTDISerViewForDevice(FaceBaseActivity_1 accessoryTestAc, ProView mProView) {
        super(accessoryTestAc);
        this.mProView = mProView;
        initView();
    }

    @Override
    protected void initView() {
        mainView = View.inflate(getFaceContext(), R.layout.usb_su_lin_con_lin, null);
        mCon40View = new ConNView(getFaceContext(),32,8,4,true);
        mCon40View.setmSuCallBack(mSucaBack);
        LinearLayout con_right_lin= (LinearLayout)mainView.findViewById(R.id.con_right_lin);
        con_right_lin.addView(mCon40View, ViewKeys.getLinearLayoutP());
        set_bu_con30 = (Button) findViewById(R.id.send_bu_con_three_b);
        set_bu_con30.setVisibility(View.GONE);
        get_con_bu = (Button) findViewById(R.id.get_con_bu);
        get_con_on = (Button) findViewById(R.id.get_con_on);
        get_con_off = (Button) findViewById(R.id.get_con_off);
        addViewListener();
    }

    public void setDeviceCapacity(DeviceCapacityBase mDeviceCapacity) {
        mUSB = (USBSerializ) mDeviceCapacity;
        USBLinker_ mUSBLinker_SerializeShortMessage= mUSB.getmUSBLinker();
        if(mUSBLinker_SerializeShortMessage==null){
            if(AppStaticSetting.isTest)
            DialogUtil.showLongToast(getFaceContext(), "11111111");
            mProView.setErrorInfo("没有获取到USB联结者");
            return;
        }
        if(mUSBLinker_SerializeShortMessage.getStatus()!= USBLinker_.LinkStatus_GetData){
            mProView.setErrorInfo("链接状态" + mUSBLinker_SerializeShortMessage.getStatusName());
            if(AppStaticSetting.isTest)
            DialogUtil.showLongToast(getFaceContext(), "2222");
            return;
        }
        this.mUSBLinker = (USBLinker_Serialize) mUSBLinker_SerializeShortMessage;
    }

    FaceCommCallBack mSucaBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object... oo) {
            getmCon40ViewSend();
            return false;
        }
    };

    private void getmCon40ViewSend() {
        byte[] bbs = mCon40View.getDatas();
        String oostr = "";
        for (int i = 0; i <= bbs.length - 1; i++) {
            oostr += bbs[i];
        }
        byte[] bbsall = null;
        try {
            bbsall = MathUtil.bigIntStringToBytes(oostr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        oostr = "";
        for (int i = 0; i <= bbsall.length - 1; i++) {
            oostr += bbsall[i] + ",";
        }
        Su.log(oostr);
        byte writeBuffer[] = new byte[bbsall.length + 1];
        writeBuffer[0] = 0x22;
        System.arraycopy(bbsall, 0, writeBuffer, 1, bbsall.length);
        send(writeBuffer);
    }


    ConNView mCon40View;
    Button set_bu_con30, get_con_bu, get_con_on, get_con_off;

    private void addViewListener() {

        get_con_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUSB ==null){
                    mProView.setErrorInfo("没有 USB连接器");
                    return;
                }
                byte writeBuffer[] = new byte[1];
                writeBuffer[0] = 0x27;
                send(writeBuffer);
                getFaceContext().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byte bytes2[]= mUSB.getReadBytes();
                        if(bytes2!=null&&bytes2.length>0) {
                                byte bss[]=MathUtil.bytesTobit(bytes2);
                                mCon40View.setDataInMainThread(bss);
                        }
                    }
                },100);

            }
        });

        set_bu_con30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmCon40ViewSend();
            }
        });
        get_con_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte writeBuffer[] = new byte[]{0x22, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, (byte) 0xff};
                send(writeBuffer);
            }
        });
        get_con_off.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                byte writeBuffer[] = new byte[]{0x22, 00, 00, 00, 00};
                send(writeBuffer);
            }
        });
    }

    private void send(byte[] writeBuffer) {
        if (mUSBLinker != null) {
            boolean is = mUSBLinker.sendSerializDataBySuWay(writeBuffer.length, writeBuffer);
//            mProView.setLoadingName("发送  " + is);
        } else {
            mProView.setErrorInfo("没有 USB连接器");
        }
    }


}
