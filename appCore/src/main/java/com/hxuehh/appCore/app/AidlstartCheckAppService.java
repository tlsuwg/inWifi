package com.hxuehh.appCore.app;

import android.content.Intent;
import android.os.IBinder;

/**
 * Created by suwg on 2015/10/17.
 */
//空实现
public class AidlstartCheckAppService extends SuSuperService {
    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }
}
