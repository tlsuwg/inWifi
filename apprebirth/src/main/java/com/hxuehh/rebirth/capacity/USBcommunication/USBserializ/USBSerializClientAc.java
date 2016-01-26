package com.hxuehh.rebirth.capacity.USBcommunication.USBserializ;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.FaceView.FTDIusb.ConNView;
import com.hxuehh.rebirth.capacity.USBcommunication.USBcommunicationKeys;
import com.hxuehh.rebirth.capacity.USBcommunication.view.ColorPickerView;
import com.hxuehh.rebirth.capacity.USBcommunication.view.OnColorChangedListener;
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
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.devInfo.ScreenUtil;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.util.ArrayList;

/**
 * Created by suwg on 2015/9/11.
 */


public class USBSerializClientAc extends BaseDeviceCapacityAc_2 {

    @Override
    public int getViewKey() {
        return ViewKeys.USBSerializClientAc;
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
                    if (kk == USBSerializ.USBSerializParameter.Type_Get_40_120IOGet) {
                        JsonElement mDeviceInfoJson = new Gson().toJsonTree(info);
                        ArrayList baseInfo = new Gson().fromJson(mDeviceInfoJson, ArrayList.class);
                        if (baseInfo.size() > 0) {
                            int i = 0;
                            double d0= (double) baseInfo.get(0);
                            if(d0==0x27){
                                baseInfo.remove(0);
                            }
                            byte[] bs = new byte[baseInfo.size()];
                            for (Object oo : baseInfo) {
                                    double d = (double) oo;
                                    bs[i++] = (byte) d;
                                }
                                byte[] bbs = MathUtil.bytesTobit(bs);
                            if(bbs.length==40)
                                mCon40View.setDataInMainThread(bbs);
                          else  if(bbs.length==120)
                                mCon120View.setDataInMainThread(bbs);

                        }
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


    public void setMainView() {
        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.main_lin, mProView.getMainView(), this);
        mProView.gone_all();

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChange((Integer) v.getTag());
            }
        };

        int  getmSuUsbAccessoryKey = (((USBSerializ) mDeviceCapacity).getmSuUsbAccessoryKey());

        switch (getmSuUsbAccessoryKey){
            case USBcommunicationKeys.FT_315M:{
                showTF315MViews(mOnClickListener);
            }break;
            case USBcommunicationKeys.FT_40IO:{
                showTF40Views(mOnClickListener);
            }break;
            case USBcommunicationKeys.WCH_120IO:{
                showWCH120Views(mOnClickListener);
            }break;
            case USBcommunicationKeys.WCH_Light_Machine:{
                showWch11000Views(mOnClickListener);
            }break;
        }
    }



//    ================================================================================
    EditText rotate_edit, R_edit, G_edit, B_edit;
    byte[] bbsColor;
    byte[] io2=new byte[2];
    Button setRGB_button,setIOs2_button_1,setIOs2_button_2,setIOs2_button;
    CheckBox mCheckBox_io;
    boolean mCheckBox_io_check;

    private void showWch11000Views(View.OnClickListener mOnClickListener) {
        View.inflate(this, R.layout.usb_wch_colour_seri_clinet_lin, (ViewGroup) findViewById(R.id.main_lin));
        String names[] = new String[]{"获取链接状态","配置：旋转（0-255）","开始转动","停止转动","设置灯流动1", "设置灯流动2", "打开灯","关闭灯","查看变化",
        "温度湿度","红外状态"};
        int keys[] = new int[]{
                USBSerializ.USBSerializParameter.Type_GetUSBLinkStatus,
                USBSerializ.USBSerializParameter.Type_WCH_Rotate_Setting,
                2001,
                2002,
                USBSerializ.USBSerializParameter.Type_WCH_Colour_automatic_1,
                USBSerializ.USBSerializParameter.Type_WCH_Colour_automatic_2,
                USBSerializ.USBSerializParameter.Type_ON,
                USBSerializ.USBSerializParameter.Type_OFF,
                USBSerializ.USBSerializParameter.Type_WCH_Colour_automatic_show,
                USBSerializ.USBSerializParameter.Type_WCH_Temperature_humidity_Sensor,
                USBSerializ.USBSerializParameter.Type_WCH_HongWai_Sensor,
        };

        rotate_edit = (EditText) findViewById(R.id.rotate_edit);
        R_edit = (EditText) findViewById(R.id.R_edit);
        G_edit = (EditText) findViewById(R.id.G_edit);
        B_edit = (EditText) findViewById(R.id.B_edit);
        setRGB_button= (Button) findViewById(R.id.setRGB_button);
        setRGB_button.setTag(2000);
        setRGB_button.setOnClickListener(mOnClickListener);

        mCheckBox_io= (CheckBox) findViewById(R.id.CheckBox_io);
        mCheckBox_io.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckBox_io_check=isChecked;
            }
        });

        setIOs2_button_1= (Button) findViewById(R.id.setIOs2_button_1);
        setIOs2_button_2= (Button) findViewById(R.id.setIOs2_button_2);
        setIOs2_button= (Button) findViewById(R.id.setIOs2_button);
        setIOs2_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(io2[0]==0){
                    io2[0]=1;
                }else{
                    io2[0]=0;
                }
                setIOs2_button_1.setText(""+io2[0]);
                if(mCheckBox_io_check){
                    doChange(USBSerializ.USBSerializParameter.Type_WCH_IO2);
                }
            }
        });
        setIOs2_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(io2[1]==0){
                    io2[1]=1;
                }else{
                    io2[1]=0;
                }
                setIOs2_button_2.setText(""+io2[1]);
                if(mCheckBox_io_check){
                    doChange(USBSerializ.USBSerializParameter.Type_WCH_IO2);
                }
            }
        });


        setIOs2_button.setTag(USBSerializ.USBSerializParameter.Type_WCH_IO2);
        setIOs2_button.setOnClickListener(mOnClickListener);


        ColorPickerView mColorPickerView = new ColorPickerView(getFaceContext(), ScreenUtil.WIDTH / 2, ScreenUtil.WIDTH / 2, new OnColorChangedListener() {
            @Override
            public void colorChanged(int color) {
                int b = 0xFF & color;
                int g = 0xFF00 & color;
                g >>= 8;
                int r = 0xFF0000 & color;
                r >>= 16;
                Su.log(r + " " + g + " " + b);
                R_edit.setText(r + "");
                G_edit.setText(g+"");
                B_edit.setText(b+"");

                if(bbsColor==null)
                bbsColor = new byte[4];

                bbsColor[1] = MathUtil.int2_4bytes(r)[3];
                bbsColor[2] = MathUtil.int2_4bytes(g)[3];
                bbsColor[3] = MathUtil.int2_4bytes(b)[3];

                Su.log(bbsColor[1] + " " + bbsColor[2] + " " + bbsColor[3]);


                doChange(USBSerializ.USBSerializParameter.Type_WCH_Colour_setting);

            }
        });
        ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, mColorPickerView, this);
        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, button1, this);
            button1.setOnClickListener(mOnClickListener);
        }
    }

//====================================================================
    ConNView mCon40View;
    private void showTF40Views(View.OnClickListener mOnClickListener) {
        View.inflate(this, R.layout.usb_tf_seri_clinet_lin, (ViewGroup) findViewById(R.id.main_lin));
        String names[] = new String[]{"获取运行状态", "获取IO情况", "设置IO", "全亮", "全暗"};
        int keys[] = new int[]{
                USBSerializ.USBSerializParameter.Type_GetUSBLinkStatus,
                USBSerializ.USBSerializParameter.Type_Get_40_120IOGet,
                USBSerializ.USBSerializParameter.Type_Set40IO,
                USBSerializ.USBSerializParameter.Type_ON,
                USBSerializ.USBSerializParameter.Type_OFF,
        };

        mCon40View = new ConNView(getFaceContext(),32,8,4,true);
        ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, mCon40View, this);

        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, button1, this);
            button1.setOnClickListener(mOnClickListener);
        }
    }


    //====================================================================
    ConNView mCon120View;
    private void showWCH120Views(View.OnClickListener mOnClickListener) {
        View.inflate(this, R.layout.usb_tf_seri_clinet_lin, (ViewGroup) findViewById(R.id.main_lin));
        String names[] = new String[]{"获取运行状态", "获取IO情况", "设置IO", "全亮", "全暗"};
        int keys[] = new int[]{
                USBSerializ.USBSerializParameter.Type_GetUSBLinkStatus,
                USBSerializ.USBSerializParameter.Type_Get_40_120IOGet,
                USBSerializ.USBSerializParameter.Type_Set120IO,
                USBSerializ.USBSerializParameter.Type_ON,
                USBSerializ.USBSerializParameter.Type_OFF,
        };

        mCon120View = new ConNView(getFaceContext(),80,40,8,false);
        ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, mCon120View, this);

        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            ViewKeys.addIntoLin(R.id.usb_main_client_ser_lin, button1, this);
            button1.setOnClickListener(mOnClickListener);
        }
    }


    private void showTF315MViews(View.OnClickListener mOnClickListener) {

        String names[] = new String[]{"随机乱撞 @Face_UnsolvedForDlp", "315M_1_0000_0000_0000","315M_1000"};
        int keys[] = new int[]{
                USBSerializ.USBSerializParameter.Type_Test315All,
                USBSerializ.USBSerializParameter.Type_315_1_0000_0000_0000,
                USBSerializ.USBSerializParameter.Type_315_1000
        };
        for (int i = 0; i < names.length; i++) {
            Button button1 = new Button(this);
            button1.setText(names[i]);
            button1.setTag(keys[i]);
            ViewKeys.addIntoLin(R.id.main_lin, button1, this);
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

        MidMessageOrder_2 midMessageOrder = null;
        try {
            midMessageOrder = getCmdForChange(changeKey);
        } catch (FaceException e) {
            e.printStackTrace();
            mProView.setErrorInfo(e.getMessage());
            return;
        }
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

    public MidMessageOrder_2 getCmdForChange(int changeKey) throws FaceException {
        DeviceCapacityInParameter mDeviceCapacityParameter = null;

        if (changeKey == USBSerializ.USBSerializParameter.Type_Set40IO||changeKey == USBSerializ.USBSerializParameter.Type_Set120IO){//其他的
            byte[] bbs=null;
            if(changeKey == USBSerializ.USBSerializParameter.Type_Set40IO) {
                 bbs = mCon40View.getDatas();
            }else{
                bbs = mCon120View.getDatasBinary();
            }
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
            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(changeKey, writeBuffer);
        } else if (changeKey == USBSerializ.USBSerializParameter.Type_WCH_Rotate_Setting) {
            String info = rotate_edit.getEditableText().toString();
            if (StringUtil.isEmpty(info)) {
                throw new FaceException("设置转速");
            }
            int o = 0;
            try {
                o =  Integer.parseInt(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_WCH_Rotate_Setting, o);
        } else if (changeKey == 2000) {
            String R = R_edit.getEditableText().toString();
            String G = G_edit.getEditableText().toString();
            String B = B_edit.getEditableText().toString();
            if (StringUtil.isEmpty(R) || StringUtil.isEmpty(B) || StringUtil.isEmpty(B)) {
                throw new FaceException("设置RGB");
            }
            byte r=0, g=0, b = 0;
            try {
                r = (byte) Integer.parseInt(R);
                g = (byte) Integer.parseInt(G);
                b = (byte) Integer.parseInt(B);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(bbsColor==null)
            bbsColor = new byte[4];
            bbsColor[1] = r;
            bbsColor[2] = g;
            bbsColor[3] = b;

            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_WCH_Colour_setting, bbsColor);

        } else if (changeKey == USBSerializ.USBSerializParameter.Type_WCH_Colour_setting) {
            if (bbsColor == null) {
                throw new FaceException("请选择颜色");
            }
            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_WCH_Colour_setting, bbsColor);
        } else if (changeKey == 2001){
            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_WCH_Rotate_Start);
        }else if (changeKey == 2002){
            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_WCH_Rotate_End);
        }else if (changeKey == USBSerializ.USBSerializParameter.Type_WCH_IO2) {
            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(USBSerializ.USBSerializParameter.Type_WCH_IO2, io2);
        }else {
            mDeviceCapacityParameter = new USBSerializ.USBSerializParameter(changeKey);
        }
        MidMessageOrderForDeviceCap_4 mid = new MidMessageOrderForDeviceCap_4(MidMessageCMDKeys.MidMessageCMD_Device_Cmd_Opstion,
                mDeviceInfo.getSU_UUID(),
                mDeviceCapacity.getType(), mDeviceCapacityParameter);
        mid.setTag(changeKey);
        mid.setmFaceCommCallBack(mFaceCommCallBackForLoad);
        return mid;
    }


}
