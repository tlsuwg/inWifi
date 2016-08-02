package com.hxuehh.rebirth.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.app.SuLongTaskIntentService;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.staticKey.IntentKeys;

public class SuNetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Su.log("SuNetReceiveronReceive " + intent.getAction());

    }
}  