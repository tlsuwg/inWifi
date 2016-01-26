package com.hxuehh.appCore.faceFramework.faceDomain.interfacesDomain;

/**
 * Created by suwg on 2014/5/24.
 */


public interface Cacheable<CacheK, CacheV> {

    CacheV inToCache(CacheK key, CacheV value);
    CacheV getValueByKey(CacheK key);
    CacheV removeFromCacheByKey(CacheK key1);
    int getSize();

}
