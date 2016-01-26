package com.hxuehh.rebirth.all.FaceView.FTDIusb;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.appCore.faceFramework.faceUI.progressView.progressViewImp.FaceGetMainViewImp;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_;
import com.hxuehh.rebirth.all.usblink.linkAbstract.USBLinker_GPIO;
import com.hxuehh.rebirth.capacity.USBcommunication.USBGPIO.USBGPIO;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.appSetting.AppStaticSetting;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.MathUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/9/18.
 */
public class CHGPIOViewForDevice extends FaceGetMainViewImp {
    ProView mProView;
    private USBLinker_GPIO mUSBLinker;
    USBGPIO mUSB;

    public CHGPIOViewForDevice(FaceBaseActivity_1 accessoryTestAc, ProView mProView) {
        super(accessoryTestAc);
        this.mProView = mProView;
        initView();
    }

    public void setDeviceCapacity(DeviceCapacityBase mDeviceCapacity) {
        mUSB = (USBGPIO) mDeviceCapacity;
        USBLinker_ mUSBLinker_SerializeShortMessage = mUSB.getmUSBLinker();
        if (mUSBLinker_SerializeShortMessage == null) {
            if(AppStaticSetting.isTest)
            DialogUtil.showLongToast(getFaceContext(), "1111111");
            mProView.setErrorInfo("没有获取到USB联结者");
            return;
        }
        if (mUSBLinker_SerializeShortMessage.getStatus() != USBLinker_.LinkStatus_GetData) {
            mProView.setErrorInfo("链接状态" + mUSBLinker_SerializeShortMessage.getStatusName());
            if(AppStaticSetting.isTest)
            DialogUtil.showLongToast(getFaceContext(), "2222222");
            return;
        }
        this.mUSBLinker = (USBLinker_GPIO) mUSBLinker_SerializeShortMessage;
    }

    @Override
    protected void initView() {
        mainView = View.inflate(getFaceContext(), R.layout.usb_su_lin_con_lin, null);
        mCon3View = new Con3View(getFaceContext());
        mCon3View.setmSuCallBack(mSucaBack);
        LinearLayout con_right_lin = (LinearLayout) mainView.findViewById(R.id.con_right_lin);
        con_right_lin.addView(mCon3View, ViewKeys.getLinearLayoutP());
        set_bu_con30 = (Button) findViewById(R.id.send_bu_con_three_b);
        set_bu_con30.setVisibility(View.GONE);
        get_con_bu = (Button) findViewById(R.id.get_con_bu);
        get_con_on = (Button) findViewById(R.id.get_con_on);
        get_con_off = (Button) findViewById(R.id.get_con_off);
        addViewListener();
    }

    FaceCommCallBack mSucaBack = new FaceCommCallBack() {
        @Override
        public boolean callBack(Object... oo) {
            getmCon3ViewSend();
            return false;
        }
    };

    int outMap = 0xe8;
    int inMap = 0xff17;

    private void getmCon3ViewSend() {
        byte[] bs = mCon3View.getDatas();
        outData = getItemData(bs, 0, 3);
        outData = getItemData(bs, 1, 5);
        outData = getItemData(bs, 2, 6);
        outData &= outMap;
        send(outData);
    }


    Con3View mCon3View;
    Button set_bu_con30, get_con_bu, get_con_on, get_con_off;
    int outData;

    private void addViewListener() {

        get_con_bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUSBLinker != null) {
                    try {
                        byte[] bs = mUSBLinker.readGPIOPort();
                        byte[] bbs= MathUtil.bytesTobit(bs);
                        byte[] ios=new byte[3];
                        ios[0]= (byte) (bbs[3]==0x01?0x00:0x01);
                        ios[1]= (byte) (bbs[5]==0x01?0x00:0x01);
                        ios[2]= (byte) (bbs[6]==0x01?0x00:0x01);
                        mCon3View.setDataInMainThread(ios);
                    } catch (FaceException e) {
                        e.printStackTrace();
                        mProView.setErrorInfo(e.getMessage());
                    }
                } else {
                    mProView.setErrorInfo("没有 USB连接器");
                }
            }
        });

        set_bu_con30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getmCon3ViewSend();
            }
        });
        get_con_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outData = 0x00;
                outData &= outMap;
                send(outData);
            }
        });
        get_con_off.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                outData = 0xe8;
                outData &= outMap;
                send(outData);
            }
        });
    }

    private int getItemData(byte[] bs, int i, int i1) {
        if (bs[i] == 1) {
            outData &= ~(1 << i1);
        } else {
            outData |= (1 << i1);
        }
        return outData;
    }

    private void send(int portData) {
        if (mUSBLinker != null) {
            mUSBLinker.setGPIOPort(portData);
        } else {
            mProView.setErrorInfo("没有 USB连接器");
        }
    }


}
