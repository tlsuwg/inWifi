package com.hxuehh.rebirth.device.faceAc;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Face_UnsolvedForDlp;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceEcxeption.FaceException;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.all.FaceView.FTDIusb.CHGPIOViewForDevice;
import com.hxuehh.rebirth.all.FaceView.FTDIusb.FTDISerViewForDevice;
import com.hxuehh.rebirth.capacity.USBcommunication.USBGPIO.USBGPIO;
import com.hxuehh.rebirth.capacity.USBcommunication.USBcommunicationKeys;
import com.hxuehh.rebirth.capacity.USBcommunication.USBserializ.USBSerializ;
import com.hxuehh.rebirth.device.DeviceCapacityBuilder;
import com.hxuehh.rebirth.device.domain.DeviceCapacityBase;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.StringUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suwg on 2015/9/17.
 */

@Face_UnsolvedForDlp  //只是支持单配件
public class AccessoryTestAc extends FaceBaseActivity_1 {

    public static final String ACTION_USB_PERMISSION = "com.hxuehh.rebirth.USB_PERMISSION";
    private UsbManager mUsbManager;
    private ProView mProView;
    private boolean isNoShowUI, isaddMainView;



    @Override
    public int getViewKey() {
        return ViewKeys.AccessoryTestAc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_lin);
        try {
            BytesClassAidl mBytesClassAidl = SuApplication.getInstance().getReMoveAidlValue(AidlCacheKeys.linkUSBNoUI, BytesClassAidl.To_Me);
            if (mBytesClassAidl != null) {
                isNoShowUI = (boolean) mBytesClassAidl.getTrue();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        initTitle();
        intProView();
        try {
            mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        } catch (Exception e) {
            mProView.setErrorInfo("可能因为Android版本问题，您的手机没有找到UsbManager，不能支持USB。\n请尝试其他功能。");
            return;
        }
    }

    private void intProView() {
        mProView = new ProView(getFaceContext());
        ViewKeys.addIntoLin(R.id.main_lin, mProView.getMainView(), this);
        mProView.gone_all();
    }

    private void initTitle() {
        TitleView m = new TitleView(this);
        if (!isNoShowUI) {
            m.setTitle("USB配件连接");
        } else {
            m.setTitle("USB配件连接,测试界面");
        }
        ViewKeys.addIntoLin(R.id.title_lin, m.getMainView(), this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Su.log("onRes");
        checkUSBhasPermission();
    }

    @Override
    protected void onDestroy() {
        if (changeLogReceiver != null)
            unregisterReceiver(changeLogReceiver);
        if (mUsbPermissionReceiver != null)
            unregisterReceiver(mUsbPermissionReceiver);
        sendBroadcast(new Intent(DeviceCapacityBase.USBLinkLog));//关掉日志
        super.onDestroy();

    }

    private BroadcastReceiver changeLogReceiver;

    private void addListenerFor_logReceiver() {
        IntentFilter filter = new IntentFilter();//状态变化的监听肯定是这样实现了；但是发出的命令 还可以直接使用bindservice的方式获取到service，直接调用发送的办法
        filter.addAction(DeviceCapacityBase.USBLinkDateChange+mDeviceCapacity.getType());
        if (changeLogReceiver == null) {
            changeLogReceiver = new BroadcastReceiver() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent==null)return;
                    String status = intent.getStringExtra("change");
                    showLog(status);
                    if (!isNoShowUI && !isaddMainView) {
                        showMainView(accessoryByFind.getManufacturer(), mDeviceCapacity);
                    }
                }
            };
        }
        registerReceiver(changeLogReceiver, filter);
    }

    UsbAccessory accessoryByFind;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void checkUSBhasPermission() {
        Su.log("checkUSBhasPermission ");
        if (mUsbManager == null) return;
        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        if (accessories != null) {
            for (UsbAccessory accessoryOne : accessories) {
                if (accessoryOne != null) {
                    if (checkUSBByInfo(accessoryOne)) {
                        accessoryByFind = accessoryOne;
                    }
                }
            }

            if (accessoryByFind == null) {
                showErr("USB配件不存在,请插入配件");
                return;
            }

            if (mUsbManager.hasPermission(accessoryByFind)) {
                DialogUtil.showShortToast(getFaceContext(), "已经授权");
                LinkService();
            } else {
                addListenerFor_linkUsbPermissionReceiver();
                askPermissionRequestPending(accessoryByFind);
            }

        } else {
            showErr("没有连接USB设备,或者已经断开，请重新插拔链接");
        }
    }

    DeviceCapacityBase mDeviceCapacity = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void LinkService() {
        int describeContents = accessoryByFind.describeContents();
        if(describeContents<=0)
            describeContents=SharedPreferencesUtils.getInteger(SharedPreferencesKeys.describeContents);
        switch (describeContents){
            case USBcommunicationKeys.WCH_Light_107:{
                mDeviceCapacity = DeviceCapacityBuilder.getInstance().getDeviceCapacityByKey(DeviceCapacityBase.Type_USB_GPIO);
                if (mDeviceCapacity == null) {
                    mDeviceCapacity = new USBGPIO();
                    DeviceCapacityBuilder.getInstance().putDeviceCapacity(mDeviceCapacity);
                }
            }
            break;
            case USBcommunicationKeys.FT_315M:
            case USBcommunicationKeys.FT_40IO:
            case USBcommunicationKeys.FT_Infrared:
            case USBcommunicationKeys.WCH_Light_Machine:
            case USBcommunicationKeys.WCH_120IO:
            {
                mDeviceCapacity = DeviceCapacityBuilder.getInstance().getDeviceCapacityByKey(DeviceCapacityBase.Type_USB_Serializ);
                if (mDeviceCapacity == null) {
                    mDeviceCapacity = new USBSerializ();
                    DeviceCapacityBuilder.getInstance().putDeviceCapacity(mDeviceCapacity);
                }
            }break;
            default:{
                mProView.setErrorInfo("没有找到合适的适配器，组织USB");
                return;
            }

        }


        try {
            mDeviceCapacity.testHardware_SDK();
            mDeviceCapacity.onCreat();
        } catch (FaceException e) {
            e.printStackTrace();
        }

        addListenerFor_logReceiver();

        Intent in = new Intent(DeviceCapacityBase.USBLinkAction + mDeviceCapacity.getType());
        in.putExtra("accessory", accessoryByFind);
        in.putExtra("log", true);
        in.putExtra("SuUsbAccessoryKey", describeContents);
        sendBroadcast(in);


    }

    private void showMainView(String key, DeviceCapacityBase mDeviceCapacity) {
        isaddMainView = true;

        if ("FTDI".equals(key)) {
            FTDISerViewForDevice mFTDIView = new FTDISerViewForDevice(getFaceContext(), mProView);
            ViewKeys.addIntoLin(R.id.main_lin, mFTDIView.getMainView(), getFaceContext());
            mFTDIView.setDeviceCapacity(mDeviceCapacity);
        } else if ("WCH".equals(key)) {
            CHGPIOViewForDevice mFTDIView = new CHGPIOViewForDevice(getFaceContext(), mProView);
            ViewKeys.addIntoLin(R.id.main_lin, mFTDIView.getMainView(), getFaceContext());
            mFTDIView.setDeviceCapacity(mDeviceCapacity);
        }
    }


    // 申请权限
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void askPermissionRequestPending(UsbAccessory accessory) {
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
                getFaceContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);// 申请使用USB
        mUsbManager.requestPermission(accessory, mPermissionIntent);

    }


    // 处理权限问题
    private BroadcastReceiver mUsbPermissionReceiver;

    private void addListenerFor_linkUsbPermissionReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);// 监听许可
        if (mUsbPermissionReceiver == null) {
            mUsbPermissionReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (ACTION_USB_PERMISSION.equals(action)) {
                        synchronized (this) {
                            UsbAccessory accessory = (UsbAccessory) intent
                                    .getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                            if (intent.getBooleanExtra(
                                    UsbManager.EXTRA_PERMISSION_GRANTED, false)) {// 许可使用
                                showLog("授权连接");
                                LinkService();
                            } else {
                                showErr("拒绝给权限");
                            }
                            context.unregisterReceiver(mUsbPermissionReceiver);// 注销监听权限
                            mUsbPermissionReceiver = null;
                        }
                    }
                }
            };
        }
        this.registerReceiver(mUsbPermissionReceiver, filter);
    }


    private void showErr(String s) {
        mProView.setErrorInfo(s);
    }


    List<String> listlog=new ArrayList<String>();
    private void showLog(String s) {
        listlog.add(s);
        if(listlog.size()>4){
            listlog.remove(0);
        }

        StringBuffer sb=new StringBuffer();
        for(String item:listlog){
            sb.append(item).append(StringUtil.N);
        }

        mProView.setOk(sb.toString(), false);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static boolean checkUSBByInfo(UsbAccessory accessory) {

        String info = "Description描述 " + accessory.getDescription() + ";"
                + "Manufacturer厂商 " + accessory.getManufacturer() + ";"
                + "Model模式 " + accessory.getModel() + ";" + "Serial "
                + accessory.getSerial() + ";" + "Uri " + accessory.getUri()
                + ";" + "Version " + accessory.getVersion() + ";"
                + "describeContents描述内容 " + accessory.describeContents();


        // Description Vinculum Accessory Test;
        // Manufacturer FTDI;
        // Model FTDIGPIODemo;
        // Serial VinculumAccessory1;
        // Uri http://www.ftdichip.com;
        // Version 1.0;
        // describeContents 0

        Su.log(info);
        // if (-1 == accessoryByFind.toString().indexOf(ManufacturerString)) {
        // Toast.makeText(context, "Manufacturer is not matched!",
        // Toast.LENGTH_SHORT).show();
        // return false;
        // }
        //
        // if (-1 == accessoryByFind.toString().indexOf(ModelString1)
        // && -1 == accessoryByFind.toString().indexOf(ModelString2)) {
        // Toast.makeText(context, "Model is not matched!", Toast.LENGTH_SHORT)
        // .show();
        // return false;
        // }
        //
        // if (-1 == accessoryByFind.toString().indexOf(VersionString)) {
        // Toast.makeText(context, "Version is not matched!",
        // Toast.LENGTH_SHORT).show();
        // return false;
        // }
        //
        // Toast.makeText(context, "Manufacturer, Model & Version are matched!",
        // Toast.LENGTH_SHORT).show();

        return true;
    }
}
