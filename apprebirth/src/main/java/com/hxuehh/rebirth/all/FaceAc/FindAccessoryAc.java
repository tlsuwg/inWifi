package com.hxuehh.rebirth.all.FaceAc;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.app.SuApplication;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.appCore.faceFramework.faceUI.androidWrap.FaceBaseActivity_1;
import com.hxuehh.rebirth.R;
import com.hxuehh.rebirth.app.IntentChangeManger;
import com.hxuehh.rebirth.device.faceAc.AccessoryTestAc;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.ProView;
import com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.title.TitleView;
import com.hxuehh.reuse_Process_Imp.staicUtil.commonUtil.DialogUtil;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;
import com.hxuehh.reuse_Process_Imp.staticKey.AidlCacheKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.SharedPreferencesKeys;
import com.hxuehh.reuse_Process_Imp.staticKey.ViewKeys;

/**
 * Created by suwg on 2015/8/13.
 */
public class FindAccessoryAc extends FaceBaseActivity_1 implements View.OnClickListener {

    Button be_0, be_1;
    TextView show_status;
    ProView mProView;

    TextView show_no_key_text;
    EditText show_no_key_edit;

    public static final String ACTION_USB_PERMISSION = "com.hxuehh.rebirth.USB_PERMISSION";
    private UsbManager mUsbManager;
    boolean isTest;

    @Override
    public int getViewKey() {
        return ViewKeys.AccessoryFindAc;
    }

    UsbAccessory accessoryAddFor;//带来的
    UsbAccessory accessoryByFind;
    boolean ismUsbManagerHas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_usb_slect_device_type);
        Intent intent = getIntent();
        intent.getAction();
        accessoryAddFor = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
        initView();
    }

    int describeContents;

    @Override
    protected void onResume() {
        super.onResume();
        Su.log("onRes");
        checkUSBhasPermission();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void checkUSBhasPermission() {
        if (mUsbManager == null) return;
        Su.log("checkUSBhasPermission ");
        UsbAccessory[] accessories = mUsbManager.getAccessoryList();
        if (accessories != null) {
            for (UsbAccessory accessoryOne : accessories) {
                if (accessoryOne != null) {
                    if (AccessoryTestAc.checkUSBByInfo(accessoryOne)) {
                        accessoryByFind = accessoryOne;
                    }
                }
            }

            if (accessoryByFind == null) {
                showErr("USB 不存在");
                return;
            }

            describeContents = accessoryByFind.describeContents();
            if (describeContents <= 0) {
                describeContents = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.describeContents);
                if (describeContents == 0) {
                    show_no_key_text.setText("没有看到您设备的ID，请查看说明书，输入。（这个是存在巨大风险的，可能导致不能工作。）");
                } else {
                    show_no_key_text.setText("没有读取到类型，请检查ID是不是正确。（这个是存在巨大风险的，可能导致不能工作。）");
                }
            }

            show_no_key_edit.setText(describeContents+"");


            if (mUsbManager.hasPermission(accessoryByFind)) {
                DialogUtil.showLongToast(getFaceContext(), "已经授权");
                findViewById(R.id.main_lin_for_finder).setVisibility(View.VISIBLE);
                if (isTest)
                    mProView.setOk("直接授权" + (accessoryByFind == accessoryAddFor) + " \n" + accessoryByFind + "  \n" + accessoryAddFor, false);

            } else {
                addListenerFor_linkUsbPermissionReceiver();
                askPermissionRequestPending(accessoryByFind);
            }

        } else {
            findViewById(R.id.main_lin_for_finder).setVisibility(View.GONE);
            showErr("没有连接USB设备,或者已经断开，请重新插拔链接");
        }
    }


    private void addListenerFor_linkUsbPermissionReceiver() {
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);// 监听许可
        this.registerReceiver(mUsbPermissionReceiver, filter);
    }

    // 申请权限
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private void askPermissionRequestPending(UsbAccessory accessory) {
        synchronized (mUsbPermissionReceiver) {
            DialogUtil.showShortToast(this, "申请链接权限");
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
                    getFaceContext(), 0, new Intent(ACTION_USB_PERMISSION), 0);// 申请使用USB
            mUsbManager.requestPermission(accessory, mPermissionIntent);
        }
    }


    // 处理权限问题
    private final BroadcastReceiver mUsbPermissionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbAccessory accessory = (UsbAccessory) intent
                            .getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                    if (isTest)
                        mProView.setOk("授权" + (accessory == accessoryAddFor) + " \n" + accessory + "  \n" + accessoryAddFor, false);
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED, false)) {// 许可使用
                        DialogUtil.showShortToast(getFaceContext(), "授权连接");
                    } else {
                        showErr("拒绝给权限");
                    }
                    context.unregisterReceiver(mUsbPermissionReceiver);// 注销监听权限
                }
            }
        }
    };

    private void showErr(String info) {
        mProView.setErrorInfo(info);
    }


    @Override
    public void initView() {
        super.initView();
        new TitleView(getFaceContext()).setTitle("配件发现").addIntoView(getFaceContext(), R.id.title_lin);
        be_0 = (Button) findViewById(R.id.be_0);
        be_1 = (Button) findViewById(R.id.be_1);
        show_status = (TextView) findViewById(R.id.show_status);


        show_no_key_text = (TextView) findViewById(R.id.show_no_key_text);
        show_no_key_edit = (EditText) findViewById(R.id.show_no_key_edit);


        mProView = new ProView(this);
        ViewKeys.addIntoLin(R.id.select_title_lin, mProView.getMainView(), this);
        mProView.gone_all();

        try {
            mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            ismUsbManagerHas = true;
        } catch (Exception e) {
            mProView.setErrorInfo("可能因为Android版本问题，您的手机没有找到UsbManager，不能支持USB。\n请尝试其他功能。");
            findViewById(R.id.main_lin_for_finder).setVisibility(View.GONE);
        }


        int key = SharedPreferencesUtils.getInteger(SharedPreferencesKeys.device_used_type);
        switch (key) {
            case SharedPreferencesKeys.device_used_type_noSet:
                show_status.setText("");
                be_0.setText("去设置");
                be_1.setVisibility(View.GONE);
                break;
            case SharedPreferencesKeys.device_used_type_device:
                show_status.setText("已经设置作为设备端，被控制");
                be_0.setText("重新设置");
                if (!ismUsbManagerHas) {
                    be_1.setVisibility(View.GONE);
                } else {
                    be_1.setText("启动USB配件可用");
                }

                break;
            case SharedPreferencesKeys.device_used_type_only_server:
                show_status.setText("已经设置作为中心服务");
                be_0.setText("重新设置");
                be_1.setVisibility(View.GONE);
                break;
            case SharedPreferencesKeys.device_used_type_server_Device:
                show_status.setText("已经设置作为设备端，被控制");
                be_0.setText("重新设置");
                if (!ismUsbManagerHas) {
                    be_1.setVisibility(View.GONE);
                } else {
                    be_1.setText("启动USB配件可用");
                }
                break;
            case SharedPreferencesKeys.device_used_type_client:
                show_status.setText("已经设置作为控制端，操控其他设备");
                be_0.setText("重新设置");
                be_1.setVisibility(View.GONE);
                break;
        }
        addListeners();
    }

    @Override
    public void addListeners() {
        super.addListeners();
        be_0.setOnClickListener(this);
        be_1.setOnClickListener(this);
        findViewById(R.id.test_usb_dev).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String keys = show_no_key_edit.getEditableText().toString();
        try {
            describeContents = Integer.parseInt(keys);
            if(describeContents>0) {
                SharedPreferencesUtils.putInteger(SharedPreferencesKeys.describeContents,describeContents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (describeContents <= 0) {
            DialogUtil.showLongToast(getFaceContext(), "没有看到您设备的ID，请查看说明书，输入。（这个是存在巨大风险的，可能导致不能工作。）");
            return;
        }


        switch (v.getId()) {
            case R.id.be_0: {
                IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_device_used_type_noSet, false);
            }
            break;
            case R.id.be_1: {
                toLinkUSB();
            }
            break;
            case R.id.test_usb_dev: {
                IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_test_usb_dev, true);
            }
            break;
        }
    }

    private void toLinkUSB() {
        try {
            SuApplication.getInstance().putAidlValue(new BytesClassAidl(AidlCacheKeys.linkUSBNoUI, BytesClassAidl.To_Me, true));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        IntentChangeManger.jumpTo(getFaceContext(), IntentChangeManger.flag_test_usb_dev, true);
    }
}
