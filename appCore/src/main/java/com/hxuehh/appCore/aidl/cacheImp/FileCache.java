package com.hxuehh.appCore.aidl.cacheImp;


import android.content.Context;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Cacheable;
import com.hxuehh.reuse_Process_Imp.staicUtil.store.SharedPreferencesUtils;

public class FileCache implements Cacheable<Integer, BytesClassAidl>
{
	Context mContext;
	public FileCache(Context myServiceSetGetClass) {
		this.mContext=myServiceSetGetClass;
	}

    @Override
    public BytesClassAidl inToCache(Integer key, BytesClassAidl value) {
        SharedPreferencesUtils.putObject(value, key.toString());
        return value;
    }

    @Override
	public BytesClassAidl getValueByKey(Integer key) {
		// TODO Auto-generated method stub
		Object oo= SharedPreferencesUtils.getObject(key.toString());
		return (BytesClassAidl)oo;
	}

    @Override
    public BytesClassAidl removeFromCacheByKey(Integer key) {
        Object oo= SharedPreferencesUtils.getObject(key.toString());
        SharedPreferencesUtils.remove(key.toString());
        return (BytesClassAidl)oo;
    }




	@Override
	public int getSize() {
		return -1;
	}

}
