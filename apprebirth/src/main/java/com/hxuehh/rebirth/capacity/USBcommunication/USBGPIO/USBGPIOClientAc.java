package com.hxuehh.rebirth.capacity.USBcommunication.USBGPIO;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.commonInterface.FaceCommCallBack;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.FaceView.FTDIusb.Con3View;
import com.hxuehh.rebirth.client.service.ClientService_TCPLongLink_;
import com.hxuehh.rebirth.device.domain.DeviceCapacityInParameter;
import com.hxuehh.rebirth.device.faceAc.BaseDeviceCapacityAc_2;
import com.hxuehh.rebirth.suMessage.domain.MidMessage;
import com.hxuehh.rebirth.suMessage.domain.MidMessageCMDKeys;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageBack_2;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrderForDeviceCap_4;
import com.hxuehh.rebirth.suMessage.domain.imp.MidMessageOrder_2;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.MathUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.util.ArrayList;

/**
 * Created by suwg on 2015/9/11.
 */


public class USBGPIOClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.USBGPIOClientAc;
    }

    public void ShowBack(MidMessageBack_2 mMidMessageBack) {
        Object key = mMidMessageBack.getTag();
        if (key == null) {
            mProView.setErrorInfo("关联出错");
        } else {
            Object err = mMidMessageBack.getByKey(MidMessage.Key_ErrInfo);
            if (err != null) {
                mProView.setErrorInfo(err.toString() + "");
                return;
            }
            int kk = (Integer) key;
            if (mMidMessageBack.isOK()) {
                Object info = mMidMessageBack.getByKey(MidMessage.Key_Res);
                Object waring = mMidMessageBack.getByKey(MidMessage.Key_BackWarning);
                if (info != null) {
                    if (kk == USBGPIO.USBGPIOParameter.Type_GetIO) {
                        JsonElement mDeviceInfoJson = new Gson().toJsonTree(info);
                        ArrayList baseInfo = new Gson().fromJson(mDeviceInfoJson, ArrayList.class);
                        byte[] bs = new byte[baseInfo.size()];
                        int i = 0;
                        for (Object oo : baseInfo) {
                            double d = (double) oo;
                            bs[i++] = (byte) d;
                        }
                        byte[] bbs = MathUtil.bytesTobit(bs);
                        showCon3View(bbs);
                    }
                    mProView.setOk(info + (waring == null ? "" : "警告：" + waring), false);
                    return;
                }
                mProView.setOk(" 完成  " + mMidMessageBack.getByKey(MidMessage.Key_Res), false);
            } else {
                mProView.setErrorInfo("失败");
            }
        }
    }

    private void showCon3View(byte[] bbs) {
        byte[] ios = new byte[3];
        ios[0] = (byte) (bbs[3] == 0x01 ? 0x00 : 0x01);
        ios[1] = (byte) (bbs[5] == 0x01 ? 0x00 : 0x01);
        ios[2] = (byte) (bbs[6] == 0x01 ? 0x00 : 0x01);
        mCon3View.setDataInMainThread(ios);
    }

    Con3View mCon3View;

    public void setMainView() {
        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.main_lin, mProView.getMainView(), this);
        mProView.gone_all();
        View.inflate(this, R.layout.usb_tf_seri_clinet_lin, (ViewGroup) findViewById(R.id.main_lin));
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChange((Integer) v.getTag());
            }
        };

        String names[] = new String[]{"获取运行状态", "获取IO情况", "全亮", "全暗"};
        int keys[] = new int[]{
                USBGPIO.USBGPIOParameter.Type_GetStatus,
                USBGPIO.USBGPIOParameter.Type_GetIO,
                USBGPIO.USBGPIOParameter.Type_ON,
                USBGPIO.USBGPIOParameter.Type_OFF
        };
        mCon3View = new Con3View(getFaceContext());
        mCon3View.setmSuCallBack(new FaceCommCallBack() {
            @Override
            public boolean callBack(Object[] t) {
                doChange(USBGPIO.USBGPIOParameter.Type_SetIO);
                return false;
            }
        });
        ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, mCon3View, this);
        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, button1, this);
            button1.setOnClickListener(mOnClickListener);
        }
    }

    public void doChange(int changeKey) {
        if (mProView.isLoading()) {
            DialogUtil.showShortToast(getFaceContext(), "请稍后....");
            return;
        }
        mProView.setLoadingName("改变中...");
        ClientService_TCPLongLink_ mClientService_TCPLongLink_ = getLinkShow();
        if (mClientService_TCPLongLink_ == null) return;

        MidMessageOrder_2 midMessageOrder = getCmdForChange(changeKey);
        if (midMessageOrder == null) {
            mProView.setErrorInfo("没有设置此类动作的消息");
            return;
        }

        try {
            mClientService_TCPLongLink_.sendSyncAddCache(midMessageOrder);
        } catch (FaceException e) {
            e.printStackTrace();
            mProView.setErrorInfo(e.getMessage() + "");
        } catch (Exception e) {
            e.printStackTrace();
            mProView.setErrorInfo(e.getMessage() + "");
        }
    }

    int outMap = 0xe8;
    int inMap = 0xff17;
    int outData;

    private int getItemData(byte[] bs, int i, int i1) {
        if (bs[i] == 1) {
            outData &= ~(1 << i1);
        } else {
            outData |= (1 << i1);
        }
        return outData;
    }

    private void getmCon3ViewSend() {
        byte[] bs = mCon3View.getDatas();
        outData = getItemData(bs, 0, 3);
        outData = getItemData(bs, 1, 5);
        outData = getItemData(bs, 2, 6);
        outData &= outMap;
    }

    public MidMessageOrder_2 getCmdForChange(int changeKey) {
        DeviceCapacityInParameter mDeviceCapacityParameter = null;
        if (changeKey != USBGPIO.USBGPIOParameter.Type_SetIO) {
            mDeviceCapacityParameter = new USBGPIO.USBGPIOParameter(changeKey);
        } else {
            getmCon3ViewSend();
            mDeviceCapacityParameter = new USBGPIO.USBGPIOParameter(changeKey, outData);
        }
        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setTag(changeKey);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        return mid;
    }


}
