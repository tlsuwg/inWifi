package com.hxuehh.rebirth.capacity.USBcommunication;

import android.hardware.usb.UsbAccessory;

/**
 * Created by suwg on 2015/10/16.
 */
public class SuUsbAccessory  {
    UsbAccessory mUsbAccessory;
    int mSuUsbAccessoryKey=-1;

    public SuUsbAccessory(UsbAccessory mUsbAccessory, int suUsbAccessoryKey) {
        this.mUsbAccessory = mUsbAccessory;
        mSuUsbAccessoryKey = suUsbAccessoryKey;
    }
    public SuUsbAccessory(UsbAccessory mUsbAccessory) {
        this.mUsbAccessory = mUsbAccessory;
    }

    public UsbAccessory getmUsbAccessory() {
        return mUsbAccessory;
    }

    public void setmUsbAccessory(UsbAccessory mUsbAccessory) {
        this.mUsbAccessory = mUsbAccessory;
    }

    public int getSuUsbAccessoryKey() {
        return mSuUsbAccessoryKey;
    }

    public void setSuUsbAccessoryKey(int suUsbAccessoryKey) {
        mSuUsbAccessoryKey = suUsbAccessoryKey;
    }
}
