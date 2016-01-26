package com.hxuehh.appCore.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hxuehh.appCore.aidl.BytesClass;
import com.hxuehh.appCore.develop.Su;
import com.hxuehh.reuse_Process_Imp.staticKey.IntentKeys;

public class SuNetReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Su.log("SuNetReceiveronReceive "+intent.getAction());

//        ConnectivityManager manager = (ConnectivityManager)
//                context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo activeInfo = manager.getActiveNetworkInfo();

//        Toast.makeText(context, "mobile:" + mobileInfo.isConnected() + "\n" + "wifi:" + wifiInfo.isConnected()
//                + "\n" + "active:" + activeInfo.getTypeName(), 1).show();

        Intent in = new Intent(context, SuLongTaskIntentService.class);
        in.putExtra(IntentKeys.obj_ClassKeyByte, (java.io.Serializable) new BytesClass(IntentKeys.NetChange));
        context.startService(in);

    }  //如果无网络连接activeInfo为null

}  