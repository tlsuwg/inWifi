package com.hxuehh.appCore.aidl.cacheImp;

import com.hxuehh.appCore.aidl.BytesClassAidl;
import com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain.Cacheable;

import java.util.HashMap;

public class MemoryCache implements Cacheable<Integer, BytesClassAidl>
{
	private HashMap<Integer, BytesClassAidl> map=new HashMap<Integer, BytesClassAidl>();

    @Override
    public BytesClassAidl inToCache(Integer key, BytesClassAidl value) {
        return map.put(key, value);
    }

    @Override
	public BytesClassAidl getValueByKey(Integer key) {
		// TODO Auto-generated method stub
		return map.get(key);
	}

    @Override
    public BytesClassAidl removeFromCacheByKey(Integer key) {
        return map.remove(key);
    }

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return map.size();
	}

}
