package com.hxuehh.appCore.aidl;

import com.hxuehh.appCore.aidl.BytesClassAidl;

interface IMyServiceSetGetClass{
BytesClassAidl getValue(int key);
BytesClassAidl putValue(in BytesClassAidl mBytesClassAidl);
BytesClassAidl getReMoveValue(int key,int meOrFile);
int getMemoryCacheSize();
}